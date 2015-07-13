package com.yixia.camera.demo.ui;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;

import com.google.gson.Gson;
import com.yixia.camera.demo.log.Logger;
import com.yixia.camera.model.MediaObject;
import com.yixia.camera.model.MediaObject.MediaPart;
import com.yixia.camera.util.FileUtils;
import com.yixia.camera.util.Log;
import com.yixia.camera.util.StringUtils;

public class BaseActivity extends Activity {

	protected ProgressDialog mProgressDialog;

	public ProgressDialog showProgress(String title, String message) {
		return showProgress(title, message, -1);
	}

	public ProgressDialog showProgress(String title, String message, int theme) {
		if (mProgressDialog == null) {
			if (theme > 0)
				mProgressDialog = new ProgressDialog(this, theme);
			else
				mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
			mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
		}

		if (!StringUtils.isEmpty(title))
			mProgressDialog.setTitle(title);
		mProgressDialog.setMessage(message);
		mProgressDialog.show();
		return mProgressDialog;
	}

	public void hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		hideProgress();
		mProgressDialog = null;
	}

	/** 反序列化对象 */
	protected static MediaObject restoneMediaObject(String obj) {
		try {
			String str = FileUtils.readFile(new File(obj));
			Gson gson = new Gson();
			MediaObject result = gson.fromJson(str.toString(),
					MediaObject.class);
			result.getCurrentPart();
			preparedMediaObject(result);
			return result;
		} catch (Exception e) {
			if (e != null)
				Log.e("VCamera", "readFile", e);
		}
		return null;
	}

	/** 预处理数据对象 */
	public static void preparedMediaObject(MediaObject mMediaObject) {
		if (mMediaObject != null && mMediaObject.getMedaParts() != null) {
			int duration = 0;
			for (MediaPart part : mMediaObject.getMedaParts()) {
				part.startTime = duration;
				part.endTime = part.startTime + part.duration;
				duration += part.duration;
			}
		}
	}

	/** 序列号保存视频数据 */
	public static boolean saveMediaObject(MediaObject mMediaObject) {
		if (mMediaObject != null) {
			try {
				if (StringUtils.isNotEmpty(mMediaObject.getObjectFilePath())) {
					FileOutputStream out = new FileOutputStream(
							mMediaObject.getObjectFilePath());
					Gson gson = new Gson();
					out.write(gson.toJson(mMediaObject).getBytes());
					out.flush();
					out.close();
					return true;
				}
			} catch (Exception e) {
				Logger.e(e);
			}
		}
		return false;
	}
}
