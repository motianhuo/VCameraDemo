package com.yixia.camera.demo.ui.record;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.wechat01.R;
import com.yixia.camera.FFMpegUtils;
import com.yixia.camera.demo.VCameraDemoApplication;
import com.yixia.camera.demo.ui.BaseActivity;
import com.yixia.camera.demo.ui.record.views.ProgressView;
import com.yixia.camera.demo.ui.widget.VideoView;
import com.yixia.camera.demo.ui.widget.VideoView.OnPlayStateListener;
import com.yixia.camera.model.MediaObject;
import com.yixia.camera.model.MediaObject.MediaPart;
import com.yixia.camera.util.DeviceUtils;

public class ImportVideoActivity extends BaseActivity implements
		OnClickListener, OnPreparedListener, OnPlayStateListener {

	/** 视频预览 */
	private VideoView mVideoView;
	/** 暂停图标 */
	private View mRecordPlay;
	/** 视频总进度条 */
	private ProgressView mProgressView;

	/** 视频信息 */
	private MediaObject mMediaObject;
	private MediaPart mMediaPart;
	/** 窗体宽度 */
	private int mWindowWidth;
	private String mVideoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		VCameraDemoApplication.getInstance().addActivity(this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 防止锁屏

		String obj = getIntent().getStringExtra("obj");
		mVideoPath = getIntent().getStringExtra("path");
		mMediaObject = restoneMediaObject(obj);
		if (mMediaObject == null) {
			Toast.makeText(this, R.string.record_read_object_faild,
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		mWindowWidth = DeviceUtils.getScreenWidth(this);
		setContentView(R.layout.activity_import_video);

		// ~~~ 绑定控件
		mVideoView = (VideoView) findViewById(R.id.record_preview);
		mRecordPlay = findViewById(R.id.record_play);
		mProgressView = (ProgressView) findViewById(R.id.record_progress);

		// ~~~ 绑定事件
		mVideoView.setOnClickListener(this);
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnPlayStateListener(this);
		findViewById(R.id.title_left).setOnClickListener(this);
		findViewById(R.id.title_right).setOnClickListener(this);

		findViewById(R.id.record_layout).getLayoutParams().height = mWindowWidth;
		mVideoView.setVideoPath(mVideoPath);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			startEncoding();
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

		if (!isFinishing() && mMediaObject != null && mMediaPart != null) {
			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					showProgress("",
							getString(R.string.record_camera_progress_message));
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					return FFMpegUtils.importVideo(mMediaPart, mWindowWidth,
							mVideoView.getVideoWidth(),
							mVideoView.getVideoHeight(), 0, 0, true);
				}

				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					hideProgress();
					if (result) {
						saveMediaObject(mMediaObject);
						setResult(Activity.RESULT_OK);
						finish();
					} else {
						Toast.makeText(ImportVideoActivity.this,
								R.string.record_video_transcoding_faild,
								Toast.LENGTH_SHORT).show();
					}
				}
			}.execute();
		}
	}

	@Override
	public void onStateChanged(boolean isPlaying) {
		if (isPlaying)
			mRecordPlay.setVisibility(View.GONE);
		else
			mRecordPlay.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		if (!isFinishing()) {
			if (mVideoView.getVideoWidth() == 0
					|| mVideoView.getVideoHeight() == 0) {
				Toast.makeText(ImportVideoActivity.this,
						R.string.record_camera_import_video_faild,
						Toast.LENGTH_SHORT).show();
				finish();
				return;
			}

			mVideoView.start();
			mVideoView.setLooping(true);

			int duration = mMediaObject.getMaxDuration()
					- mMediaObject.getDuration();
			if (duration > mVideoView.getDuration())
				duration = mVideoView.getDuration();

			mMediaPart = mMediaObject.buildMediaPart(mVideoPath, duration,
					MediaObject.MEDIA_PART_TYPE_IMPORT_VIDEO);
			mProgressView.setData(mMediaObject);
		}
	}
}
