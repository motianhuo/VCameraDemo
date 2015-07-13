package com.yixia.camera.demo.ui.record.helper;

import com.google.gson.Gson;
import com.yixia.camera.demo.log.Logger;
import com.yixia.camera.demo.preference.PreferenceKeys;
import com.yixia.camera.demo.preference.PreferenceUtils;
import com.yixia.camera.util.FileUtils;
import com.yixia.camera.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 拍摄帮助类
 * 
 * @author tangjun
 *
 */
public class RecorderHelper {

	/** 获取视频码率 */
	public static int getVideoBitrate() {
		//		if (NetworkUtils.isWifiAvailable(VideoApplication.getContext()) || !PreferenceUtils.getBoolean(PreferenceKeys.VIDEO_BITRATE_3G_600K, PreferenceKeys.VIDEO_BITRATE_3G_600K_DEFAULT))
		//			return 1500;//MediaRecorder.VIDEO_BITRATE_MEDIUM;//WIFI下800码率
		//		else {
		//			return 800;//MediaRecorder.VIDEO_BITRATE_NORMAL;//3G、2G下600码率
		//		}
		return 1500;
	}

	/** 获取最大拍摄时长，默认10秒 */
	public static int getMaxDuration() {
		return PreferenceUtils.getIntProcess(PreferenceKeys.VIDEO_TIME_LIMIT, PreferenceKeys.VIDEO_TIME_LIMIT_DEFAULT);
	}

	/**
	 * 清除账号权限拍摄时长
	 */
	public static void removeDuration() {
		PreferenceUtils.remove(PreferenceKeys.VIDEO_TIME_LIMIT);
	}

	/** 讲对象实例化到磁盘 */
	public static boolean saveObject(Object obj, String target) {
		try {
			if (StringUtils.isNotEmpty(target)) {
				FileOutputStream out = new FileOutputStream(target);
				Gson gson = new Gson();
				out.write(gson.toJson(obj).getBytes());
				out.flush();
				out.close();
				return true;
			}
		} catch (Exception e) {
			Logger.e(e);
		}
		return false;
	}

	/** 从文件中反序列化对象 */
	public static <T> T restoreObject(Class<T> cls, String target) {
		try {
			String sb = FileUtils.readFile(new File(target));
			if (sb != null) {
				String str = sb.toString();
				Gson gson = new Gson();
				T result = gson.fromJson(str.toString(), cls);
				return result;
			}
		} catch (Exception e) {
			Logger.e(e);
		}
		return null;
	}


}
