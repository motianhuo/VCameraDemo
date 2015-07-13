package com.yixia.camera.demo.ui.record;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wechat01.R;
import com.yixia.camera.demo.VCameraDemoApplication;
import com.yixia.camera.demo.common.CommonIntentExtra;
import com.yixia.camera.demo.po.POThemeSingle;
import com.yixia.camera.demo.service.AssertService;
import com.yixia.camera.demo.ui.BaseActivity;
import com.yixia.camera.demo.ui.record.helper.ThemeHelper;
import com.yixia.camera.demo.ui.record.views.ThemeGroupLayout;
import com.yixia.camera.demo.ui.record.views.ThemeSufaceView;
import com.yixia.camera.demo.ui.record.views.ThemeView;
import com.yixia.camera.demo.utils.ConvertToUtils;
import com.yixia.camera.demo.utils.IsUtils;
import com.yixia.camera.demo.utils.ToastUtils;
import com.yixia.camera.model.MediaObject;
import com.yixia.camera.model.MediaThemeObject;
import com.yixia.camera.util.DeviceUtils;
import com.yixia.camera.util.FileUtils;
import com.yixia.camera.util.StringUtils;
import com.yixia.videoeditor.adapter.UtilityAdapter;

/**
 * 视频预览
 * 
 * @author tangjun@yixia.com
 *
 */
public class MediaPreviewActivity extends BaseActivity implements
		OnClickListener, UtilityAdapter.OnNativeListener {

	/** 开始转码 */
	private static final int HANDLER_ENCODING_START = 100;
	/** 转码进度 */
	private static final int HANDLER_ENCODING_PROGRESS = 101;
	/** 转码结束 */
	private static final int HANDLER_ENCODING_END = 102;
	/** 无主题放的位置 */
	private final static int NO_THEME_INDEX = 0;

	/** 播放按钮、主题音量按钮 */
	private ImageView mPlayStatus;
	/** 上一步、下一步 */
	private TextView mTitleLeft, mTitleNext, mTitleText, mVideoPreviewMusic;
	/** 主题音乐，原声音 */
	private CheckBox mThemeVolumn, mVideoVolumn;
	/** 正在加载 */
	private View mLoadingView;
	/** 主题、滤镜容器 */
	private View mThemeLayout, mFilterLayout;

	/** 主题容器 */
	private ThemeGroupLayout mThemes, mFilters;
	/** MV主题 */
	private ThemeSufaceView mThemeSufaceView;

	/** 主题缓存的目录 */
	private File mThemeCacheDir;
	/** 当前主题 */
	private POThemeSingle mCurrentTheme;

	/** 主题列表 */
	private ArrayList<POThemeSingle> mThemeList;
	/** 滤镜列表 */
	private ArrayList<POThemeSingle> mFilterList;

	/** 导演签名图片 */
	private String mAuthorBitmapPath;
	/** 导出视频，导出封面 */
	private String mVideoPath, mCoverPath;
	/** 临时合并ts流 */
	private String mVideoTempPath;
	/** 当前音乐路径 */
	private String mCurrentMusicPath;
	/** 当前音乐名称 */
	private String mCurrentMusicTitle;
	/** 当前音乐名称 */
	private String mCurrentMusicName;
	/** 是否需要回复播放 */
	private boolean mNeedResume;
	/** 是否停止播放 */
	private boolean mStopPlayer;
	/** 是否正在转码 */
	private boolean mStartEncoding;
	/** 窗体宽度 */
	private int mWindowWidth;
	/** 分块边距，默认10dip */
	private int mLeftMargin;
	/** 视频时长 */
	private int mDuration;
	/** 视频信息 */
	private MediaObject mMediaObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		VCameraDemoApplication.getInstance().addActivity(this);
		mMediaObject = (MediaObject) getIntent().getSerializableExtra(
				CommonIntentExtra.EXTRA_MEDIA_OBJECT);
		if (mMediaObject == null) {
			Toast.makeText(this, R.string.record_read_object_faild,
					Toast.LENGTH_SHORT).show();
			finish();
			overridePendingTransition(R.anim.push_bottom_in,
					R.anim.push_bottom_out);
			return;
		}

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 防止锁屏
		prepareActivity();
		prepareViews();
	}

	/** 预处理参数 */
	private boolean prepareActivity() {
		// 加载默认参数
		mWindowWidth = DeviceUtils.getScreenWidth(this);
		// 获取传入参数
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) && !isExternalStorageRemovable())
			mThemeCacheDir = new File(getExternalCacheDir(), "Theme");
		else
			mThemeCacheDir = new File(getCacheDir(), "Theme");
		mLeftMargin = ConvertToUtils.dipToPX(this, 8);

		mVideoPath = mMediaObject.getOutputVideoPath();
		if (StringUtils.isNotEmpty(mVideoPath)) {
			mCoverPath = mVideoPath.replace(".mp4", ".jpg");
		}
		mVideoTempPath = getIntent().getStringExtra("output");

		return true;
	}

	/** 预处理UI相关 */
	private void prepareViews() {
		setContentView(R.layout.activity_media_preview);
		// 绑定控件
		mPlayStatus = (ImageView) findViewById(R.id.play_status);
		mThemeSufaceView = (ThemeSufaceView) findViewById(R.id.preview_theme);
		mTitleLeft = (TextView) findViewById(R.id.titleLeft);
		mTitleNext = (TextView) findViewById(R.id.titleRight);
		mTitleText = (TextView) findViewById(R.id.titleText);
		mVideoPreviewMusic = (TextView) findViewById(R.id.video_preview_music);
		mThemes = (ThemeGroupLayout) findViewById(R.id.themes);
		mFilters = (ThemeGroupLayout) findViewById(R.id.filters);
		mThemeVolumn = (CheckBox) findViewById(R.id.video_preview_theme_volume);
		mVideoVolumn = (CheckBox) findViewById(R.id.video_preview_video_volume);
		mLoadingView = findViewById(R.id.loading);
		mThemeLayout = findViewById(R.id.theme_layout);
		mFilterLayout = findViewById(R.id.filter_layout);

		mTitleLeft.setOnClickListener(this);
		mTitleNext.setOnClickListener(this);
		mThemeSufaceView.setOnComplateListener(mOnComplateListener);
		mThemeSufaceView.setOnClickListener(this);
		findViewById(R.id.tab_theme).setOnClickListener(this);
		findViewById(R.id.tab_filter).setOnClickListener(this);
		mThemeVolumn.setOnClickListener(this);
		mVideoVolumn.setOnClickListener(this);

		mTitleText.setText(R.string.record_camera_preview_title);
		mTitleNext.setText(R.string.record_camera_preview_next);

		// 设置主题预览默认参数
		mThemeSufaceView.setIntent(getIntent());
		mThemeSufaceView.setOutputPath(mVideoPath);// 输出文件
		mThemeSufaceView.setMediaObject(mMediaObject);
		if (FileUtils.checkFile(mThemeCacheDir)) {
			mThemeSufaceView.setFilterCommomPath(new File(mThemeCacheDir,
					ThemeHelper.THEME_VIDEO_COMMON).getAbsolutePath());
		}
		/** 设置播放区域 */
		View preview_layout = findViewById(R.id.preview_layout);
		LinearLayout.LayoutParams mPreviewParams = (LinearLayout.LayoutParams) preview_layout
				.getLayoutParams();
		mPreviewParams.height = DeviceUtils.getScreenWidth(this);
		loadThemes();
	}

	@Override
	public void onResume() {
		super.onResume();
		UtilityAdapter.registerNativeListener(this);
		if (mThemeSufaceView != null && mNeedResume && mCurrentTheme != null) {
			restart();
		}
		mNeedResume = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		UtilityAdapter.registerNativeListener(null);
		if (mThemeSufaceView != null && mThemeSufaceView.isPlaying()) {
			mNeedResume = true;
			releaseVideo();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titleLeft:
			finish();
			break;
		case R.id.titleRight:
			startEncoding();
			break;
		case R.id.preview_theme:// 点击暂停视频播放
			if (isPlaying())
				stopVideo();
			else
				startVideo();
			break;
		case R.id.video_preview_theme_volume:// 静音主题音
			// 隐藏动画
			ToastUtils
					.showToastImage(
							this,
							mThemeVolumn.isChecked() ? R.drawable.priview_theme_volumn_close
									: R.drawable.priview_theme_volumn_open);
			mThemeSufaceView.setThemeMute(mThemeVolumn.isChecked());
			restart();
			break;
		case R.id.video_preview_video_volume:// 静音原声
			ToastUtils
					.showToastImage(
							this,
							mVideoVolumn.isChecked() ? R.drawable.priview_orig_volumn_close
									: R.drawable.priview_orig_volumn_open);
			mThemeSufaceView.setOrgiMute(mVideoVolumn.isChecked());
			restart();
			break;
		case R.id.tab_theme:
			mThemeLayout.setVisibility(View.VISIBLE);
			mFilterLayout.setVisibility(View.GONE);
			break;
		case R.id.tab_filter:
			mThemeLayout.setVisibility(View.GONE);
			mFilterLayout.setVisibility(View.VISIBLE);
			break;
		}
	}

	/** 开始转码 */
	private void startEncoding() {
		// 检测磁盘空间
		// if (FileUtils.showFileAvailable() < 200) {
		// Toast.makeText(this, R.string.record_camera_check_available_faild,
		// Toast.LENGTH_SHORT).show();
		// return;
		// }

		stopVideo();

		// 更新静音
		if (mMediaObject != null && mMediaObject.mThemeObject != null) {
			mMediaObject.mThemeObject.mThemeMute = mThemeVolumn.isChecked();
			mMediaObject.mThemeObject.mOrgiMute = mVideoVolumn.isChecked();
		}
		// 检测是否需要重新编译
		mStartEncoding = true;
		mHandler.removeMessages(HANDLER_ENCODING_START);
		mHandler.removeMessages(HANDLER_ENCODING_PROGRESS);
		mHandler.removeMessages(HANDLER_ENCODING_END);
		mHandler.sendEmptyMessage(HANDLER_ENCODING_START);
	}

	/** 加载主题 */
	private void loadThemes() {
		if (isFinishing() || mStartEncoding)
			return;

		new android.os.AsyncTask<Void, Void, File>() {

			@Override
			protected File doInBackground(Void... params) {
				// 预处理主题
				while (AssertService.isRunning()) {
					SystemClock.sleep(500);
				}

				// 预处理主题（解压更新主题包）
				File result = ThemeHelper.prepareTheme(
						MediaPreviewActivity.this, mThemeCacheDir);
				if (result != null) {
					// 主题列表
					mThemeList = ThemeHelper.parseTheme(
							MediaPreviewActivity.this, mThemeCacheDir,
							ThemeHelper.THEME_MUSIC_VIDEO_ASSETS,
							R.array.theme_order);

					// 空主题
					POThemeSingle orgiTheme = ThemeHelper.loadThemeJson(
							mThemeCacheDir, new File(mThemeCacheDir,
									ThemeHelper.THEME_EMPTY));
					if (orgiTheme != null)
						mThemeList.add(NO_THEME_INDEX, orgiTheme);
				}

				// 加载滤镜
				mFilterList = ThemeHelper.parseTheme(MediaPreviewActivity.this,
						mThemeCacheDir, ThemeHelper.THEME_FILTER_ASSETS,
						R.array.theme_filter_order);

				// 生成签名
				mAuthorBitmapPath = ThemeHelper
						.updateVideoAuthorLogo(
								mThemeCacheDir,
								getString(R.string.record_camera_author,
										getString(R.string.app_name)
												+ " By Juns Allen"), false);
				return result;
			}

			@Override
			protected void onPostExecute(File result) {
				super.onPostExecute(result);
				File themeDir = result;
				if (themeDir != null && !isFinishing() && mThemeList != null
						&& mThemeList.size() > 1) {
					/** 循环添加单个主题到主题容器中 */
					mThemes.removeAllViews();

					String themeName = getIntent().getStringExtra("theme");
					int defaultIndex = NO_THEME_INDEX, index = 0;
					if (mCurrentTheme != null) {
						themeName = mCurrentTheme.themeName;
					}
					for (POThemeSingle theme : mThemeList) {
						addThemeItem(theme, -1);// 顺序添加
						if (StringUtils.isNotEmpty(themeName)
								&& IsUtils.equals(theme.themeName, themeName)) {
							defaultIndex = index;
						}
						index++;
					}

					// 滤镜
					mFilters.removeAllViews();
					for (POThemeSingle theme : mFilterList) {
						addThemeItem(mFilters, theme, -1);// 顺序添加
					}

					mCurrentTheme = null;
					mThemes.getChildAt(defaultIndex).performClick();// 默认选中无主题
				}
			}

		}.execute();
	}

	private ThemeView addThemeItem(ThemeGroupLayout layout,
			POThemeSingle theme, int index) {
		ThemeView themeView = new ThemeView(MediaPreviewActivity.this, theme);
		if (theme.themeIconResource > 0) {
			themeView.getIcon().setImageResource(theme.themeIconResource);
		} else {
			if (StringUtils.isNotEmpty(theme.themeIcon)) {
				themeView.getIcon().setImagePath(theme.themeIcon);
			}
		}

		themeView.setOnClickListener(mThemeClickListener);
		themeView.setTag(theme);
		// Logger.e("[MediaPreviewActivity]addThemeItem..." +
		// theme.themeDisplayName + " themeFolder:" + theme.themeFolder);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);// mThemeItemWH,
														// mThemeItemWH
		lp.leftMargin = mLeftMargin;
		if (index == -1)
			layout.addView(themeView, lp);
		else
			layout.addView(themeView, index, lp);
		return themeView;
	}

	/** 添加当个主题到UI上 */
	private ThemeView addThemeItem(POThemeSingle theme, int index) {
		return addThemeItem(mThemes, theme, index);
	}

	/** 重新播放 */
	private synchronized void restart() {
		mStopPlayer = false;
		mHandler.removeMessages(UtilityAdapter.NOTIFYVALUE_PLAYFINISH);
		mHandler.sendEmptyMessageDelayed(UtilityAdapter.NOTIFYVALUE_PLAYFINISH,
				100);
	}

	private void releaseVideo() {
		mThemeSufaceView.pauseClearDelayed();
		mThemeSufaceView.release();
		mPlayStatus.setVisibility(View.GONE);
	}

	/** 开始播放 */
	private void startVideo() {
		mStopPlayer = false;
		mThemeSufaceView.start();
		mPlayStatus.setVisibility(View.GONE);
	}

	/** 暂停播放 */
	private void stopVideo() {
		mStopPlayer = true;
		mThemeSufaceView.pause();
		mPlayStatus.setVisibility(View.VISIBLE);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_ENCODING_START:
				if (!isFinishing()) {
					showProgress("",
							getString(R.string.record_preview_encoding));
					// WindowManager.LayoutParams lp =
					// dialog.getWindow().getAttributes();
					// lp.y = -ConvertToUtils.dipToPX(MediaPreviewActivity.this,
					// 49 + 30);
					// dialog.getWindow().setAttributes(lp);
					// showProgressLayout(false, false,
					// getString(R.string.progressbar_message_preview_making));
					releaseVideo();
					mThemeSufaceView.startEncoding();
					sendEmptyMessage(HANDLER_ENCODING_PROGRESS);
				}
				break;
			case HANDLER_ENCODING_PROGRESS:// 读取进度
				int progress = UtilityAdapter
						.FilterParserInfo(UtilityAdapter.FILTERINFO_PROGRESS);
				if (mProgressDialog != null) {
					mProgressDialog.setMessage(getString(
							R.string.record_preview_encoding_format, progress));
				}
				if (progress < 100)
					sendEmptyMessageDelayed(HANDLER_ENCODING_PROGRESS, 200);
				else {
					sendEmptyMessage(HANDLER_ENCODING_END);
				}
				break;
			case HANDLER_ENCODING_END:
				mDuration = UtilityAdapter
						.FilterParserInfo(UtilityAdapter.FILTERINFO_TOTALMS);
				mThemeSufaceView.release();
				onEncodingEnd();
				break;
			case UtilityAdapter.NOTIFYVALUE_BUFFEREMPTY:
				showLoading();
				break;
			case UtilityAdapter.NOTIFYVALUE_BUFFERFULL:
				hideLoading();
				break;
			case UtilityAdapter.NOTIFYVALUE_PLAYFINISH:
				/** 播放完成时报告 */
				if (!isFinishing() && !mStopPlayer) {
					showLoading();
					mThemeSufaceView.release();
					mThemeSufaceView.initFilter();
					mPlayStatus.setVisibility(View.GONE);
				}
				break;
			case UtilityAdapter.NOTIFYVALUE_HAVEERROR:
				/** 无法播放时报告 */
				if (!isFinishing()) {
					Toast.makeText(MediaPreviewActivity.this,
							R.string.record_preview_theme_load_faild,
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	/** 转码完成 */
	private void onEncodingEnd() {
		hideProgress();
		mStartEncoding = false;
		startActivity(new Intent(this, VideoPlayerActivity.class).putExtra(
				"path", mVideoPath));
	}

	/** 显示加载中 */
	private void showLoading() {
		// showProgress("", getString(R.string.record_preview_building));
		if (mLoadingView != null)
			mLoadingView.setVisibility(View.VISIBLE);
	}

	/** 隐藏加载中 */
	private void hideLoading() {
		if (mLoadingView != null)
			mLoadingView.setVisibility(View.GONE);
	}

	/** 是否正在播放 */
	private boolean isPlaying() {
		return mThemeSufaceView.isPlaying();
	}

	/** 响应主题点击事件 */
	private OnClickListener mThemeClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			POThemeSingle theme = (POThemeSingle) v.getTag();
			if (theme == null || mMediaObject == null)
				return;

			if (StringUtils.isNotEmpty(mAuthorBitmapPath)
					&& (mCurrentTheme == null || !IsUtils.equals(
							mCurrentTheme.themeName, theme.themeName))) {
				String key = theme.themeName;
				((ThemeGroupLayout) v.getParent()).mObservable
						.notifyObservers(key);

				mCurrentTheme = theme;

				if (mMediaObject.mThemeObject == null)
					mMediaObject.mThemeObject = new MediaThemeObject();

				if (theme.isMV()) {
					mMediaObject.mThemeObject.mMVThemeName = theme.themeName;
					mMediaObject.mThemeObject.mMusicThemeName = theme.musicName;
					mThemeSufaceView.reset();
					mThemeSufaceView.setMVPath(theme.themeFolder);
					mThemeSufaceView.setTheme(theme);
					mThemeSufaceView.setVideoEndPath(mAuthorBitmapPath);// 签名
					mThemeSufaceView.setInputPath(mVideoTempPath);// 输入文件
					// 添加音乐
					mCurrentMusicPath = mCurrentTheme.musicPath;
					mCurrentMusicTitle = mCurrentTheme.musicTitle;
					mCurrentMusicName = mCurrentTheme.musicName;
					mThemeSufaceView.setMusicPath(mCurrentMusicPath);

					updateMusicTextView();

					// 清空静音状态
					mThemeVolumn.setChecked(false);

					// 清除滤镜的选中状态
					if (mFilters != null) {
						mFilters.mObservable
								.notifyObservers(POThemeSingle.THEME_EMPTY);
					}
				}

				// 滤镜
				if (theme.isFilter()) {
					mMediaObject.mThemeObject.mFilterThemeName = theme.themeName;
					mThemeSufaceView.setFilterPath(theme.getFilterPath());
				}

				restart();
			}
		}
	};

	/** 更新音乐名称 */
	private void updateMusicTextView() {
		if (StringUtils.isEmpty(mCurrentMusicTitle)) {
			mVideoPreviewMusic.setText(R.string.record_preview_music_nothing);
			mThemeVolumn.setVisibility(View.GONE);
		} else {
			mVideoPreviewMusic.setText(mCurrentMusicTitle);
			mThemeVolumn.setVisibility(View.VISIBLE);
		}
	}

	/** 播放完成 */
	private ThemeSufaceView.OnComplateListener mOnComplateListener = new ThemeSufaceView.OnComplateListener() {

		@Override
		public void onComplate() {
			if (!isFinishing()) {
				mThemeSufaceView.release();
			}
		}

	};

	public static boolean isExternalStorageRemovable() {
		if (DeviceUtils.hasGingerbread())
			return Environment.isExternalStorageRemovable();
		else
			return Environment.MEDIA_REMOVED.equals(Environment
					.getExternalStorageState());
	}

	@Override
	public void ndkNotify(int key, int value) {
		if (!isFinishing())
			mHandler.sendEmptyMessage(value);
	}
}
