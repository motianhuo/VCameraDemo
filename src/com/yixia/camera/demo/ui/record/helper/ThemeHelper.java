package com.yixia.camera.demo.ui.record.helper;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.example.wechat01.R;
import com.yixia.camera.demo.log.Logger;
import com.yixia.camera.demo.po.POThemeSingle;
import com.yixia.camera.demo.preference.PreferenceKeys;
import com.yixia.camera.demo.preference.PreferenceUtils;
import com.yixia.camera.demo.utils.AndroidBmpUtil;
import com.yixia.camera.demo.utils.ConvertToUtils;
import com.yixia.camera.demo.utils.IsUtils;
import com.yixia.camera.demo.utils.ResourceUtils;
import com.yixia.camera.demo.utils.ZipUtils;
import com.yixia.camera.util.FileUtils;
import com.yixia.camera.util.StringUtils;
import com.yixia.videoeditor.adapter.UtilityAdapter;

public class ThemeHelper {
	/** 秒拍作者 */
	public static final String THEME_VIDEO_AUTHOR = "theme_author.bmp";
	/** 秒拍主题公共包 */
	public static final String THEME_VIDEO_COMMON = "Common";
	/** 内置MV主题 */
	public static final String THEME_MUSIC_VIDEO_ASSETS = "MusicVideoAssets";
	/** 空主题 */
	public static final String THEME_EMPTY = "Empty";
	/** 内置滤镜 */
	public static final String THEME_FILTER_ASSETS = "FilterAssets";
	/** 内置音乐 */
	public static final String THEME_MUSIC_ASSETS = "MusicAssets";

	/** 解析通过主题 */
	public static ArrayList<POThemeSingle> parseTheme(final Context context,
			File mThemeCacheDir, String type, int orderArrayId) {
		ArrayList<POThemeSingle> result = new ArrayList<POThemeSingle>();
		File themeDir = new File(mThemeCacheDir, type);
		if (FileUtils.checkFile(themeDir)) {
			int index = 0;
			File[] files = null;

			if (orderArrayId > 0) {
				String[] fileNames = context.getResources().getStringArray(
						orderArrayId);
				// 初始化
				if (fileNames != null && fileNames.length > 0) {
					files = new File[fileNames.length];
					for (int i = 0, j = fileNames.length; i < j; i++) {
						files[i] = new File(themeDir, fileNames[i]);
					}
				}
			}

			if (files == null || files.length < 1) {
				files = themeDir.listFiles();
			}

			for (File themeFile : files) {
				POThemeSingle theme = loadThemeJson(mThemeCacheDir, themeFile);
				if (theme != null) {
					theme.index = index++;
					result.add(theme);
				}
			}
		}
		return result;
	}

	/** 预处理音乐路径 */
	public static void prepareMusicPath(File mThemeCacheDir, POThemeSingle theme) {
		if (theme != null && FileUtils.checkFile(theme.themeFolder)) {
			prepareMusicPath(mThemeCacheDir, new File(theme.themeFolder), theme);
		}
	}

	/** 预处理音乐路径 */
	public static void prepareMusicPath(File mThemeCacheDir, File themeDir,
			POThemeSingle theme) {
		// 音乐
		if (theme != null && mThemeCacheDir != null && themeDir != null
				&& StringUtils.isNotEmpty(theme.musicName)) {
			// MV音效，从当前文件夹拿
			final String fName = theme.musicName + ".mp3";
			File audioFile = new File(themeDir, fName);
			if (audioFile.exists()) {
				theme.musicPath = audioFile.getPath();
			} else if (StringUtils.isNotEmpty(theme.category)) {
				// 从公共的音乐库里面拿
				String music = FileUtils.concatPath(mThemeCacheDir.getPath(),
						THEME_MUSIC_ASSETS, theme.category, fName);
				// Logger.e("[ThemeHelper]loadThemeJson..." + music);
				if (FileUtils.checkFile(music)) {
					theme.musicPath = music;
				}
			}
		}
	}

	/** 根据json加载主题信息 */
	public static POThemeSingle loadThemeJson(File mThemeCacheDir,
			File themeFile) {
		String themeName = themeFile.getName();
		File jsonFile = new File(themeFile, themeName + ".json");
		if (jsonFile.exists()) {
			try {
				JSONObject jsonObject = new JSONObject(FileUtils.readFile(
						jsonFile).toString());
				POThemeSingle theme = new POThemeSingle(jsonObject);
				// 主题路径
				theme.themeFolder = themeFile.getPath();
				// 音乐
				prepareMusicPath(mThemeCacheDir, themeFile, theme);

				// 图标
				File iconFile = new File(themeFile, themeName + ".png");
				if (FileUtils.checkFile(iconFile)) {
					theme.themeIcon = iconFile.getAbsolutePath();
				} else {
					iconFile = new File(themeFile, "icon-" + themeName
							+ "@2x.png");
					if (FileUtils.checkFile(iconFile)) {
						theme.themeIcon = iconFile.getAbsolutePath();
					}
				}

				return theme;
			} catch (Exception ex) {
				Logger.e(ex);
			}
		}
		return null;
	}

	/** 预处理主题 */
	public static File prepareTheme(final Context context, File mThemeCacheDir) {
		if (context == null) {
			return null;
		}

		if (!mThemeCacheDir.exists()) {
			if (!mThemeCacheDir.mkdirs()) {
				// ToastUtils.showToast(R.string.theme_sdcard_faild);
				return null;
			}
		}

		// 拷贝片尾、无主题带logo、公共包
		final String[] assets = { THEME_MUSIC_ASSETS, THEME_EMPTY,
				THEME_VIDEO_COMMON, THEME_FILTER_ASSETS,
				THEME_MUSIC_VIDEO_ASSETS };
		final int[] versions = context.getResources().getIntArray(
				R.array.theme_version);
		for (int i = 0, j = assets.length; i < j; i++) {
			String name = assets[i];
			File resource = new File(mThemeCacheDir, name);
			if (resource.exists()) {
				// 检查更新
				if (resource.isFile())
					continue;
				if (versions[i] > PreferenceUtils.getInt(
						PreferenceKeys.THEME_CURRENT_VERSION + "_" + name, 0)) {
					FileUtils.deleteDir(resource);
				} else if (resource.isDirectory() && resource.list() != null
						&& resource.list().length > 0) {
					continue;
				}
			}
			// 需要拷贝
			if (name.endsWith(".png")) {
				ResourceUtils.copyToSdcard(context, name,
						resource.getAbsolutePath());
			} else {
				File zip = new File(mThemeCacheDir, name + ".zip");
				if (ResourceUtils.copyToSdcard(context, zip.getName(),
						zip.getAbsolutePath())) {
					try {
						ZipUtils.UnZipFolder(zip.getAbsolutePath(),
								mThemeCacheDir.getAbsolutePath());
						FileUtils.deleteFile(zip);
						PreferenceUtils.put(
								PreferenceKeys.THEME_CURRENT_VERSION + "_"
										+ name, versions[i]);
					} catch (Exception ex) {
						// ToastUtils.showToast(R.string.theme_unzip_faild);
						// return c;
						continue;
					}
				}
			}
		}

		File theme = new File(mThemeCacheDir, THEME_MUSIC_VIDEO_ASSETS);
		if (FileUtils.checkFile(theme)) {
			return new File(theme, "default");// 所有主题系列内文件夹都是default，再往下才是单个主题
		}
		return null;
	}

	/** 读取音乐列表 */
	public static ArrayList<POThemeSingle> parseMusic(final Context context,
			File mThemeCacheDir) {
		ArrayList<POThemeSingle> result = new ArrayList<POThemeSingle>();

		// 从音乐文件夹读取音乐列表
		if (mThemeCacheDir != null) {
			File resource = new File(mThemeCacheDir, THEME_MUSIC_ASSETS);
			if (FileUtils.checkFile(resource) && resource.listFiles() != null) {
				for (File dir : resource.listFiles()) {
					if (dir.isDirectory()) {
						for (File file : dir.listFiles()) {
							if (file != null && file.getPath().endsWith(".mp3")) {
								POThemeSingle music = new POThemeSingle();
								music.category = dir.getName();
								music.themeName = file.getName().replace(
										".mp3", "");
								music.themeDisplayName = music.themeName
										.replace("_", " ");
								music.themeUrl = file.getPath();
								music.themeFolder = dir.getPath();
								result.add(music);
							}
						}
					} else if (dir.isFile() && dir.getPath().endsWith(".mp3")) {
						POThemeSingle music = new POThemeSingle();
						music.themeName = dir.getName().replace(".mp3", "");
						music.themeDisplayName = music.themeName.replace("_",
								" ");
						music.themeUrl = dir.getPath();
						music.themeFolder = resource.getPath();
						result.add(music);
					}
				}
			}

			// 从MV主题文件夹读取音乐列表
			readThemeMusics(mThemeCacheDir, result, THEME_MUSIC_VIDEO_ASSETS);
		}

		return result;
	}

	/** 读取主题音乐 */
	private static void readThemeMusics(File mThemeCacheDir,
			ArrayList<POThemeSingle> result, String themeRootDir) {
		File resource = new File(mThemeCacheDir, themeRootDir);
		if (FileUtils.checkFile(resource)) {
			for (File dir : resource.listFiles()) {
				if (dir != null && dir.isDirectory()) {
					final String themeName = dir.getName();
					File jsonFile = new File(dir, themeName + ".json");
					if (jsonFile != null && jsonFile.exists()) {
						try {
							JSONObject jsonObject = new JSONObject(FileUtils
									.readFile(jsonFile).toString());
							if (jsonObject != null) {
								boolean isMP3 = jsonObject.optBoolean("isMP3");
								String musicName = isMP3 ? jsonObject
										.optString("themeName") : jsonObject
										.optString("musicName");
								String themeDisplayName = jsonObject
										.optString("themeDisplayName");
								if (StringUtils.isNotEmpty(musicName)) {
									String musicCategory = isMP3 ? jsonObject
											.optString("category") : jsonObject
											.optString("musicCategory");
									File file = new File(dir, musicName
											+ ".mp3");
									if (FileUtils.checkFile(file)) {
										POThemeSingle music = new POThemeSingle();
										music.category = musicCategory;
										music.themeName = musicName;
										if (isMP3) {
											music.themeDisplayName = themeDisplayName;// mp3主题
										} else {
											music.themeDisplayName = musicName
													.replace("_", " ");
										}
										// music.themeName =
										// music.themeDisplayName;
										music.themeUrl = file.getPath();
										music.themeFolder = dir.getPath();
										if (jsonObject.has("musicTitle")) {
											music.themeDisplayName = jsonObject
													.optString("musicTitle");
										}
										result.add(music);
									}
								}
							}
						} catch (Exception e) {
							Logger.e(e);
						}
					}
				}
			}
		}
	}

	/** 生成图片 */
	public static boolean buildThemeTextPicture(final POThemeSingle theme,
			final String text, final String targetPath) {
		if (theme == null || StringUtils.isEmpty(targetPath))
			return false;

		TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		paint.setTypeface(Typeface.SANS_SERIF);
		paint.setTextSize(theme.textSize);// 设置字体
		paint.setColor(ConvertToUtils.toColor(theme.textColor, Color.WHITE));
		// paint.setShadowLayer(2f, 1f, 1f, Color.BLACK);//设置阴影

		// http://mikewang.blog.51cto.com/3826268/871765
		FontMetrics fm = paint.getFontMetrics();
		final int bitmapWidth = 480, bitmapHeight = 480;
		int fontWidth = (int) paint.measureText(text);
		int fontHeight = (int) (fm.leading + Math.abs(fm.ascent) + fm.descent);// (Math.ceil(fm.descent
																				// -
																				// fm.ascent)
																				// +
																				// 2);//
																				// fm.leading多行可以取到这个值
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			if (StringUtils.isNotEmpty(theme.textBackground)
					&& FileUtils.checkFile(new File(theme.themeFolder,
							theme.textBackground))) {
				Bitmap background = BitmapFactory.decodeFile(FileUtils
						.concatPath(theme.themeFolder, theme.textBackground));
				if (background != null) {
					if (!background.isRecycled()) {
						canvas.drawBitmap(background, 0, 0, null);
						background.recycle();
					}
					background = null;
				}
			}
			float x = 0;
			float y = 0;
			if ("center_horizontal".equals(theme.textGravity)) {// 水平居中
				x = (bitmapWidth - fontWidth) / 2;
				y = theme.textY;
			} else if ("center".equals(theme.textGravity)) {// 完全居中
				x = (bitmapWidth - fontWidth) / 2;
				y = (bitmapWidth - fontHeight) / 2;
			} else if ("text_center".equals(theme.textGravity)) {// 文字中心点
				x = theme.textX - (fontWidth / 2);
				y = theme.textY - (fontHeight / 2);
			} else {// 坐标控制
				x = theme.textX;
				y = theme.textY;
			}
			canvas.drawText(text, x,
					y + (Math.abs(fm.ascent) / 2 + fm.descent), paint);// Math.abs(fm.top)
																		// +
			if (AndroidBmpUtil.save(bitmap, targetPath)) {
				return true;
			}
		} catch (Exception e) {
			Logger.e(e);
		} finally {
			if (bitmap != null) {
				if (!bitmap.isRecycled())
					bitmap.recycle();
				bitmap = null;
			}
		}
		return false;
	}

	/**
	 * 更新用户拍摄视频logo
	 * 
	 * @param force
	 *            是否重新生成
	 * @return
	 */
	public static String updateVideoAuthorLogo(File mThemeCacheDir,
			String nickname, boolean force) {
		if (mThemeCacheDir != null && mThemeCacheDir.exists()) {
			File saveFile = new File(mThemeCacheDir, THEME_VIDEO_AUTHOR);
			String oldName = PreferenceUtils.getString(
					PreferenceKeys.THEME_LOGO_AUTHOR_NAME, nickname);
			// 如果不需要强制更新那就直接返回
			if (!force
					&& saveFile.exists()
					&& !IsUtils.equals(nickname, oldName)
					&& PreferenceUtils.getInt(
							PreferenceKeys.THEME_LOGO_AUTHOR_WIDTH, 0) > 0) {
				return saveFile.toString();
			}

			// FileOutputStream out = null;
			TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.WHITE);
			paint.setTypeface(Typeface.SANS_SERIF);
			paint.setTextSize(20);// 设置字体
			paint.setShadowLayer(2f, 1f, 1f, Color.BLACK);// 设置阴影

			// http://mikewang.blog.51cto.com/3826268/871765
			FontMetrics fm = paint.getFontMetrics();
			int width = (int) paint.measureText(nickname);
			int fontHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2);// fm.leading多行可以取到这个值
			int height = 30;

			if (width % 16 != 0)
				width += width % 16;

			if (width > 0 && height > 0) {
				Bitmap bitmap = null;
				try {
					bitmap = Bitmap.createBitmap(width, height,
							Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(bitmap);
					canvas.drawText(nickname, 0, Math.abs(fm.top)
							+ ((height - fontHeight) / 2), paint);

					int[] pixels = new int[width * height];
					bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

					if (UtilityAdapter.SaveData(saveFile.toString(), pixels,
							UtilityAdapter.OUTPUTFORMAT_MASK_ZIP)) {
						PreferenceUtils.put(
								PreferenceKeys.THEME_LOGO_AUTHOR_WIDTH, width);
						PreferenceUtils
								.put(PreferenceKeys.THEME_LOGO_AUTHOR_HEIGHT,
										height);
						return saveFile.toString();
					}

				} catch (Exception e) {
					Logger.e(e);
				} finally {
					if (bitmap != null) {
						if (!bitmap.isRecycled())
							bitmap.recycle();
						bitmap = null;
					}
				}
			}
		}
		return null;
	}
}
