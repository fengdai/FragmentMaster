package com.fragmentmaster.internal;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentmaster.R;
import com.fragmentmaster.app.FragmentMaster;
import com.fragmentmaster.app.IMasterFragment;
import com.fragmentmaster.app.MasterActivity;
import com.fragmentmaster.app.Request;

public class FragmentMasterImpl extends FragmentMaster {

	// The id of fragments' real container.
	public final static int FRAGMENT_CONTAINER_ID = R.id.fragment_container;

	private FragmentsAdapter mAdapter;
	private FragmentMasterPager mViewPager;
	private boolean mScrolling = false;

	private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				onScrollIdle();
			}
		}

	};

	private Runnable mCleanUpRunnable = new Runnable() {
		@Override
		public void run() {
			cleanUp();
		}
	};

	public FragmentMasterImpl(MasterActivity activity) {
		super(activity);
	}

	@Override
	protected void performInstall(ViewGroup container) {
		mAdapter = new FragmentsAdapter();
		mViewPager = new FragmentMasterPager(this);
		mViewPager.setId(FRAGMENT_CONTAINER_ID);
		mViewPager.setOffscreenPageLimit(Integer.MAX_VALUE);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(mOnPageChangeListener);

		container.addView(mViewPager);
	}

	@Override
	protected int getFragmentContainerId() {
		return FRAGMENT_CONTAINER_ID;
	}

	@Override
	protected void onFragmentStarted(IMasterFragment fragment) {
		mAdapter.notifyDataSetChanged();
		int nextItem = mAdapter.getCount() - 1;
		// Perform "smooth scroll" if the page has a PageAnimator and more than
		// one item.
		boolean smoothScroll = hasPageAnimator() && nextItem > 0;
		mViewPager.setCurrentItem(nextItem, smoothScroll);
		if (smoothScroll) {
			mScrolling = true;
		}
	}

	@Override
	public void finishFragment(final IMasterFragment fragment,
			final int resultCode, final Request data) {
		final int index = getFragments().indexOf(fragment);
		int curItem = mViewPager.getCurrentItem();

		if (index == 0) {
			setCallback(null);
		} else if (hasPageAnimator() && curItem == index) {
			// There's a PageAnimator, scroll back smoothly.
			// When scrolling is stopped, real finish will be done by
			// cleanUp method.
			mViewPager.setCurrentItem(index - 1, true);
			mScrolling = true;
		}
		if (mScrolling) {
			// If pager is scrolling, do real finish when cleanUp.
			deliverFragmentResult(fragment, resultCode, data);
			return;
		}
		super.finishFragment(fragment, resultCode, data);
	}

	@Override
	protected void onFragmentFinished(IMasterFragment fragment) {
		mAdapter.notifyDataSetChanged();
	}

	private void onScrollIdle() {
		mScrolling = false;
		// When scrolling stopped, do cleanup.
		mViewPager.removeCallbacks(mCleanUpRunnable);
		mViewPager.post(mCleanUpRunnable);
	}

	boolean isScrolling() {
		return mScrolling;
	}

	private class FragmentsAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return getFragments().size();
		}

		@Override
		public int getItemPosition(Object object) {
			int position = getFragments().indexOf(object);
			return position == -1 ? POSITION_NONE : position;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			IMasterFragment fragment = getFragments().get(position);
			return fragment;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			setPrimaryFragment((IMasterFragment) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return ((IMasterFragment) object).getView() == view;
		}
	}

}
