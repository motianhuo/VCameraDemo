package com.yixia.camera.demo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.yixia.camera.demo.log.Logger;
import com.yixia.camera.util.StringUtils;

public class ResourceUtils {

	/** 从assets 文件夹中获取文件并读取数据 */
	public static String getTextFromAssets(final Context context, String fileName) {
		String result = "";
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "UTF-8");
			in.close();
		} catch (Exception e) {
			Logger.e("Assert:" + fileName);
			Logger.e(e);
		}
		return result;
	}

	/** 拷贝资源到sdcard */
	public static boolean copyToSdcard(final Context ctx, String fileName, String target) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = ctx.getAssets().open(fileName);
			out = new FileOutputStream(target);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
		} catch (Exception ex) {
			Logger.e(ex);
			return false;
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (Exception e) {

			}
			try {
				if (out != null)
					out.close();
			} catch (Exception e) {

			}
		}
		return true;
	}

	public static Drawable loadImageFromAsserts(final Context ctx, String fileName) {
		try {
			InputStream is = ctx.getResources().getAssets().open(fileName);
			return Drawable.createFromStream(is, null);
		} catch (IOException e) {
			if (e != null) {
				Logger.e("Assert:" + fileName);
				Logger.e(e);
			}
		} catch (OutOfMemoryError e) {
			if (e != null) {
				Logger.e("Assert:" + fileName);
				Logger.e(e);
			}
		} catch (Exception e) {
			if (e != null) {
				Logger.e("Assert:" + fileName);
				Logger.e(e);
			}
		}
		return null;
	}

	/** 从Asset从加载图片 */
	public static void loadImageFromAsserts(final Context ctx, ImageView view, String fileName) {
		try {
			if (ctx != null && !StringUtils.isEmpty(fileName)) {
				InputStream is = ctx.getResources().getAssets().open(fileName);
				view.setImageDrawable(Drawable.createFromStream(is, null));
			}
		} catch (IOException e) {
			if (e != null) {
				Logger.e("Assert:" + fileName);
				Logger.e(e);
			}
		} catch (OutOfMemoryError e) {
			if (e != null) {
				Logger.e("Assert:" + fileName);
				Logger.e(e);
			}
		} catch (Exception e) {
			if (e != null) {
				Logger.e("Assert:" + fileName);
				Logger.e(e);
			}
		}
	}

	/** 拷贝数据库 */
	public static void copyDatabase(final Context ctx, String dbName) {
		if (ctx != null) {
			File f = ctx.getDatabasePath(dbName);
			if (!f.exists()) {

				// 检测databases文件夹是否已创建
				if (!f.getParentFile().exists())
					f.getParentFile().mkdir();

				try {
					InputStream in = ctx.getAssets().open(dbName);
					OutputStream out = new FileOutputStream(f.getAbsolutePath());

					byte[] buffer = new byte[1024];
					int length;
					while ((length = in.read(buffer)) > 0) {
						out.write(buffer, 0, length);
					}
					in.close();
					out.close();
					Logger.i("Database copy successed! " + f.getPath());
				} catch (Exception ex) {
					Logger.e(ex);
				}
			}
		}
	}
}
