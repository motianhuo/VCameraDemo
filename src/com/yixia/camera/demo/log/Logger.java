package com.yixia.camera.demo.log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;

import android.content.ActivityNotFoundException;
import android.database.sqlite.SQLiteFullException;
import android.util.Log;

import com.example.wechat01.BuildConfig;

public class Logger {
	/**
	 * 程序是否Debug版本
	 */
	public static final boolean IsDebug = BuildConfig.DEBUG;
	private static final String TAG = "[VCameraDemo]";

	public static void printStackTrace(String TAG, Exception e) {
		if (IsDebug) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	public static void printStackTrace(String TAG, IOException e) {
		if (IsDebug) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	public static void printStackTrace(String TAG, ClientProtocolException e) {
		if (IsDebug) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	public static void printStackTrace(String TAG, MalformedURLException e) {
		if (IsDebug) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	/**
	 * 非法参数
	 * 
	 * @param e
	 */
	public static void printStackTrace(String TAG, IllegalArgumentException e) {
		if (IsDebug) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	public static void printStackTrace(String TAG, HttpException e) {
		if (IsDebug) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	public static void printStackTrace(String TAG, ActivityNotFoundException e) {
		if (IsDebug) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	public static void printStackTrace(String TAG, IndexOutOfBoundsException e) {
		if (IsDebug) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	/**
	 * 
	 * @param e
	 */
	public static void printStackTrace(String TAG, FileNotFoundException e) {
		if (IsDebug) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	// ~~~ 数据库相关

	public static void printStackTrace(String TAG,
			android.database.sqlite.SQLiteException e) {
		if (IsDebug) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	/**
	 * 数据库文件已达到最大空间(数据库已满)
	 * 
	 * @param e
	 */
	public static void printStackTrace(String TAG, SQLiteFullException e) {
		if (IsDebug) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	/**
	 * 未捕获的异常
	 * 
	 * @param TAG
	 * @param e
	 */
	public static void printStackTrace(String TAG, Throwable e) {
		if (IsDebug) {
			e.printStackTrace();
		} else {
			logException(TAG, e);
		}
	}

	/**
	 * 记录错误日志
	 * 
	 * @param TAG
	 * @param ex
	 */
	private static void logException(String TAG, Throwable ex) {

	}

	public static void d(String tag, String msg) {
		if (IsDebug) {
			Log.d(tag, msg);
		}
	}

	public static void d(String msg) {
		Log.d(TAG, msg);
	}

	/**
	 * Send a {@link #DEBUG} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void d(String tag, String msg, Throwable tr) {
		if (IsDebug) {
			Log.d(tag, msg, tr);
		}
	}

	public static void e(Throwable tr) {
		if (IsDebug) {
			Log.e(TAG, "", tr);
		}
	}

	public static void i(String msg) {
		if (IsDebug) {
			Log.i(TAG, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (IsDebug) {
			Log.i(tag, msg);
		}
	}

	/**
	 * Send a {@link #INFO} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void i(String tag, String msg, Throwable tr) {
		if (IsDebug) {
			Log.i(tag, msg, tr);
		}

	}

	/**
	 * Send an {@link #ERROR} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void e(String tag, String msg) {
		if (IsDebug) {
			Log.e(tag, msg);
		}
	}

	public static void e(String msg) {
		if (IsDebug) {
			Log.e(TAG, msg);
		}
	}

	/**
	 * Send a {@link #ERROR} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void e(String tag, String msg, Throwable tr) {
		if (IsDebug) {
			Log.e(tag, msg, tr);
		}
	}

	public static void e(String msg, Throwable tr) {
		if (IsDebug) {
			Log.e(TAG, msg, tr);
		}
	}
}
