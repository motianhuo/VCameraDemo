package com.yixia.camera.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wechat01.R;
import com.yixia.camera.demo.VCameraDemoApplication;

public class ToastUtils {

	public static void showToast(int resID) {
		showToast(VCameraDemoApplication.getContext(), Toast.LENGTH_SHORT,
				resID);
	}

	public static void showToast(String text) {
		showToast(VCameraDemoApplication.getContext(), Toast.LENGTH_SHORT, text);
	}

	public static void showToast(Context ctx, int resID) {
		showToast(ctx, Toast.LENGTH_SHORT, resID);
	}

	public static void showToast(Context ctx, String text) {
		showToast(ctx, Toast.LENGTH_SHORT, text);
	}

	public static void showLongToast(Context ctx, int resID) {
		showToast(ctx, Toast.LENGTH_LONG, resID);
	}

	public static void showLongToast(int resID) {
		showToast(VCameraDemoApplication.getContext(), Toast.LENGTH_LONG, resID);
	}

	public static void showLongToast(Context ctx, String text) {
		showToast(ctx, Toast.LENGTH_LONG, text);
	}

	public static void showLongToast(String text) {
		showToast(VCameraDemoApplication.getContext(), Toast.LENGTH_LONG, text);
	}

	public static void showToast(Context ctx, int duration, int resID) {
		showToast(ctx, duration, ctx.getString(resID));
	}

	/** Toast一个图片 */
	public static Toast showToastImage(Context ctx, int resID) {
		final Toast toast = Toast.makeText(ctx, "", Toast.LENGTH_SHORT);
		View mNextView = toast.getView();
		if (mNextView != null)
			mNextView.setBackgroundResource(resID);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return toast;
	}

	public static void showToast(final Context ctx, final int duration,
			final String text) {
		final Toast toast = Toast.makeText(ctx, text, duration);
		View view = RelativeLayout.inflate(ctx, R.layout.toast_layout, null);
		TextView mNextView = (TextView) view.findViewById(R.id.toast_name);
		toast.setView(view);
		mNextView.setText(text);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/** 在UI线程运行弹出 */
	public static void showToastOnUiThread(final Activity ctx, final String text) {
		if (ctx != null) {
			ctx.runOnUiThread(new Runnable() {
				public void run() {
					showToast(ctx, text);
				}
			});
		}
	}

}
