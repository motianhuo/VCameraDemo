package com.yixia.camera.demo;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.yixia.camera.VCamera;
import com.yixia.camera.demo.service.AssertService;
import com.yixia.camera.util.DeviceUtils;

public class VCameraDemoApplication extends Application {

	private static VCameraDemoApplication application;

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;

		// 设置拍摄视频缓存路径
		File dcim = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (DeviceUtils.isZte()) {
			if (dcim.exists()) {
				VCamera.setVideoCachePath(dcim + "/WeChatJuns/");
			} else {
				VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
						"/sdcard-ext/")
						+ "/WeChatJuns/");
			}
		} else {
			VCamera.setVideoCachePath(dcim + "/WeChatJuns/");
		}
		// 开启log输出,ffmpeg输出到logcat
		VCamera.setDebugMode(true);
		// 初始化拍摄SDK，必须
		VCamera.initialize(this);

		// 解压assert里面的文件
		startService(new Intent(this, AssertService.class));
	}

	public static Context getContext() {
		return application;
	}

	// 运用list来保存们每一个activity是关键
	private List<Activity> mList = new LinkedList<Activity>();
	private static VCameraDemoApplication instance;

	// 构造方法
	// 实例化一次
	public synchronized static VCameraDemoApplication getInstance() {
		if (null == instance) {
			instance = new VCameraDemoApplication();
		}
		return instance;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	// 关闭每一个list内的activity
	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

}
