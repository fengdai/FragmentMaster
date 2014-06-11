package com.fragmentmaster.internal;

/**
 * Real container of fragments.
 */
import android.content.Context;
import android.support.v4.view.ViewPagerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;

class FmPager extends ViewPagerCompat {

	private boolean mIsSlideEnable = false;

	public FmPager(Context context) {
		super(context);
	}

	public FmPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setSlideEnable(boolean enable) {
		mIsSlideEnable = enable;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mIsSlideEnable) {
			return super.onTouchEvent(ev);
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mIsSlideEnable) {
			return super.onInterceptTouchEvent(ev);
		}
		return false;
	}
}
