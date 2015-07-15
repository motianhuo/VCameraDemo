package com.yixia.camera.demo.ui.record.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.Observable;

public class ThemeGroupLayout extends LinearLayout {

	public ThemeGroupLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ThemeGroupLayout(Context context) {
		super(context);
	}

	public void addView(ThemeView child, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, params);
		mObservable.addObserver(child);
	}
	
	public void addView(ThemeView child, int index, android.view.ViewGroup.LayoutParams params) {
	  super.addView(child, index, params);
	  mObservable.addObserver(child);
	}

	@Override
	public void removeAllViews() {
		super.removeAllViews();
		mObservable.deleteObservers();
	}

	public Observable mObservable = new Observable() {
		@Override
		public void notifyObservers() {
			setChanged();
			super.notifyObservers();
			clearChanged();
		}

		@Override
		public void notifyObservers(Object data) {
			setChanged();
			super.notifyObservers(data);
			clearChanged();
		};
	};
}
