package com.fragmentmaster.internal;

import java.util.List;

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
				onSlideIdle();
			}
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
		// Don't perform "smooth scroll" if we have a PageAnimator.
		mViewPager.setCurrentItem(mAdapter.getCount() - 1, hasPageAnimator());
	}

	@Override
	public void finishFragment(IMasterFragment fragment, int resultCode,
			Request data) {
		// If there's a PageAnimator, do finish after scrolling animation ends.
		if (hasPageAnimator()) {
			int index = getFragments().indexOf(fragment);
			if (index != 0 && mViewPager.getCurrentItem() == index) {
				mViewPager.setCurrentItem(index - 1);
				deliverFragmentResult(fragment, resultCode, data);
				return;
			}
		}
		super.finishFragment(fragment, resultCode, data);
	}

	@Override
	protected void onFragmentFinished(IMasterFragment fragment) {
		mAdapter.notifyDataSetChanged();
	}

	private void onSlideIdle() {
		List<IMasterFragment> fragments = getFragments();
		int currentItem = mViewPager.getCurrentItem();
		while (currentItem < mAdapter.getCount() - 1) {
			IMasterFragment f = fragments.get(fragments.size() - 1);
			if (f.isFinishing()) {
				doFinishFragment(f);
			} else {
				f.finish();
			}
		}
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
