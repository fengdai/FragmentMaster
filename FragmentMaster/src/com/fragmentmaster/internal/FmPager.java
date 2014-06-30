package com.fragmentmaster.internal;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.support.v4.view.ViewPagerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fragmentmaster.animator.PageAnimator;

/**
 * Real container of fragments.
 */
class FmPager extends ViewPagerCompat {

	/**
	 * Indicate whether this view can be dragged by user.
	 */
	private boolean mIsSlideEnable = false;

	private static final int ANIMATION_NONE = 0;
	private static final int ANIMATION_ENTER = 1;
	private static final int ANIMATION_EXIT = 2;

	private int mAnimationState = ANIMATION_NONE;

	/**
	 * The position of primary item in the latest SCROLL_STATE_IDLE state.
	 */
	private int mLatestIdleItem = 0;
	private PageAnimator mPageAnim = null;
	private ViewPager.PageTransformer mPageTransformer = new ViewPager.PageTransformer() {
		@Override
		public void transformPage(View page, float position) {
			if (mPageAnim != null) {
				mPageAnim.transformPage(page, position,
						mAnimationState == ANIMATION_ENTER);
			}
		}
	};

	private OnPageChangeListener mWrappedOnPageChangeListener;

	public FmPager(Context context) {
		super(context);
		super.setOnPageChangeListener(new MyOnPageChangeListener(this));
	}

	public FmPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setOnPageChangeListener(new MyOnPageChangeListener(this));
	}

	public void setSlideEnable(boolean enable) {
		mIsSlideEnable = enable;
	}

	public boolean isSlideEnable() {
		return mIsSlideEnable;
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

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mWrappedOnPageChangeListener = listener;
	}

	@Override
	protected void onPageScrolled(int position, float offset, int offsetPixels) {
		if (mLatestIdleItem > position) {
			// The ViewPager is performing exiting.
			mAnimationState = ANIMATION_EXIT;
		} else if (mLatestIdleItem == position) {
			// The ViewPager is performing entering.
			mAnimationState = ANIMATION_ENTER;
		}
		super.onPageScrolled(position, offset, offsetPixels);
	}

	@Override
	public void setPageTransformer(boolean reverseDrawingOrder,
			PageTransformer transformer) {
		throw new UnsupportedOperationException();
	}

	public void setPageAnimation(PageAnimator anim) {
		super.setPageTransformer(false, anim == null ? null : mPageTransformer);
		mPageAnim = anim;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(state);
		mLatestIdleItem = getCurrentItem();
	}

	private static class MyOnPageChangeListener implements OnPageChangeListener {

		private FmPager mViewPager;

		public MyOnPageChangeListener(FmPager viewPage) {
			mViewPager = viewPage;
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (mViewPager.mWrappedOnPageChangeListener != null) {
				mViewPager.mWrappedOnPageChangeListener
						.onPageScrollStateChanged(state);
			}

			if (state == ViewPager.SCROLL_STATE_IDLE) {
				mViewPager.mLatestIdleItem = mViewPager.getCurrentItem();
				mViewPager.mAnimationState = ANIMATION_NONE;
			}
		}
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			if (mViewPager.mWrappedOnPageChangeListener != null) {
				mViewPager.mWrappedOnPageChangeListener.onPageScrolled(
						position, positionOffset, positionOffsetPixels);
			}
		}

		@Override
		public void onPageSelected(int position) {
			if (mViewPager.mWrappedOnPageChangeListener != null) {
				mViewPager.mWrappedOnPageChangeListener
						.onPageSelected(position);
			}
		}

	}

}
