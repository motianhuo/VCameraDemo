package com.example.wechat01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.wechat01.adpter.NewMsgAdpter;
import com.example.wechat01.widght.EyeView;
import com.example.wechat01.widght.PullDownListView;
import com.example.wechat01.widght.PullDownListView.OnPullHeightChangeListener;
import com.yixia.camera.demo.ui.record.MediaRecorderActivity;

/**
 * 消息界面
 * 
 * @author allenjuns@yahoo.com
 *
 */
public class Fragment_Msg extends Fragment {
	private Activity ctx;
	private View layout;
	private ListView listview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.framen_msg, null);
			initView();
			initPullDownView();
		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}

	private void initView() {
		// TODO 实现本页面的布局

	}

	private void initPullDownView() {
		final PullDownListView pullDownListView = (PullDownListView) layout
				.findViewById(R.id.pullDownListView);
		final EyeView eyeView = (EyeView) layout.findViewById(R.id.eyeView);

		pullDownListView.getListView().setAdapter(
				new NewMsgAdpter(getActivity()));

		pullDownListView
				.setOnPullHeightChangeListener(new OnPullHeightChangeListener() {

					@Override
					public void onTopHeightChange(int headerHeight,
							int pullHeight) {
						// TODO Auto-generated method stub
						float progress = (float) pullHeight
								/ (float) headerHeight;

						if (progress < 0.5) {
							progress = 0.0f;
						} else {
							progress = (progress - 0.5f) / 0.5f;
						}

						if (progress > 1.0f) {
							progress = 1.0f;
						}

						if (!pullDownListView.isRefreshing()) {
							eyeView.setProgress(progress);
						}
					}

					@Override
					public void onBottomHeightChange(int footerHeight,
							int pullHeight) {
						// TODO Auto-generated method stub
						float progress = (float) pullHeight
								/ (float) footerHeight;

						if (progress < 0.5) {
							progress = 0.0f;
						} else {
							progress = (progress - 0.5f) / 0.5f;
						}

						if (progress > 1.0f) {
							progress = 1.0f;
						}

						if (!pullDownListView.isRefreshing()) {

						}

					}

					@Override
					public void onRefreshing(final boolean isTop) {
						// TODO Auto-generated method stub
						if (isTop) {
							eyeView.startAnimate();
						} else {
							// progressView.startAnimate();
						}
						Intent intent = new Intent(ctx,
								MediaRecorderActivity.class);
						ctx.startActivity(intent);
						ctx.overridePendingTransition(R.anim.push_up_in,
								R.anim.push_up_out);
						pullDownListView.pullUp();
					}

				});

		pullDownListView.getListView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

					}

				});

	}

}
