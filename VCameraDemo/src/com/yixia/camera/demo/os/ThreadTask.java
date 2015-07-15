package com.yixia.camera.demo.os;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public abstract class ThreadTask<Params, Progress, Result> {

	private HandlerThread mHandlerThread;
	private TaskHandler mHandler;
	private TaskHandler mUiHandler;
	private Params[] mParams;
	private boolean mRunning = true;
	private long mStartTime;
	private long mEndTime;

	private static final int MESSAGE_INBACKGROUND = 0;
	private static final int MESSAGE_POSTEXECUTE = 1;
	private static final int MESSAGE_PROGRESS = 2;

	public ThreadTask() {
		//降低线程的优先级
		mHandlerThread = new HandlerThread("ThreadTask", android.os.Process.THREAD_PRIORITY_BACKGROUND);
		mHandlerThread.start();

		mHandler = new TaskHandler(mHandlerThread.getLooper());
		mUiHandler = new TaskHandler(Looper.getMainLooper());
	}

	public synchronized boolean isRunning() {
		return mRunning;
	}

	protected abstract Result doInBackground(Params... params);

	protected void onPreExecute() {
	}

	protected synchronized void onProgressUpdate(Progress... values) {
	}

	protected final void publishProgress(Progress... values) {
		mUiHandler.obtainMessage(MESSAGE_PROGRESS, values).sendToTarget();
	}

	protected synchronized void onPostExecute(Result result) {
	}

	public final boolean isCancelled() {
		return !mRunning;
	}

	public void cancel() {
		mRunning = false;
		mHandlerThread.quit();
	}

	public void execute(Params... params) {
		mRunning = true;
		mParams = params;
		onPreExecute();
		mHandler.sendEmptyMessage(MESSAGE_INBACKGROUND);
	}

	/** 获取doInBackground的执行时间 毫秒 */
	public long getExecuteTime() {
		return mEndTime - mStartTime;
	}

	private class TaskHandler extends Handler {

		public TaskHandler(Looper looper) {
			super(looper);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_INBACKGROUND:
				mStartTime = System.currentTimeMillis();
				mUiHandler.obtainMessage(MESSAGE_POSTEXECUTE, doInBackground(mParams)).sendToTarget();
				break;
			case MESSAGE_POSTEXECUTE:
				mEndTime = System.currentTimeMillis();
				mRunning = false;
				onPostExecute((Result) msg.obj);
				try {
					mHandlerThread.quit();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case MESSAGE_PROGRESS:
				onProgressUpdate((Progress[]) msg.obj);
				break;
			}
		}
	}
}
