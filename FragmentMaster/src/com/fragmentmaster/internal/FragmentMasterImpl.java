package com.fragmentmaster.internal;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentmaster.R;
import com.fragmentmaster.app.FragmentMaster;
import com.fragmentmaster.app.MasterActivity;
import com.fragmentmaster.app.MasterFragment;
import com.fragmentmaster.app.Request;

public class FragmentMasterImpl extends FragmentMaster {

	private FragmentsAdapter mAdapter;
	private FmPager mViewPager;

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

	private ViewPager.PageTransformer mTransformerWrapper = new ViewPager.PageTransformer() {
		@Override
		public void transformPage(View page, float position) {
			performTransform(page, position);
		}
	};

	public FragmentMasterImpl(MasterActivity activity) {
		super(activity);
	}

	@Override
	protected void performInstall(ViewGroup container) {
		mAdapter = new FragmentsAdapter(getFragmentManager());
		mViewPager = new FmPager(getActivity());
		mViewPager.setId(R.id.fragment_container);
		mViewPager.setPageTransformer(isReverseDrawingOrder(),
				mTransformerWrapper);
		mViewPager.setOffscreenPageLimit(Integer.MAX_VALUE);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(mOnPageChangeListener);
		mViewPager.setSlideEnable(isSlideEnable());

		container.addView(mViewPager);
	}

	@Override
	protected void performStartFragmentForResult(MasterFragment fragment) {
		mAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(mAdapter.getCount() - 1);
	}

	@Override
	public void finishFragment(MasterFragment fragment, int resultCode,
			Request data) {
		int index = getFragments().indexOf(fragment);
		if (index != 0 && mViewPager.getCurrentItem() == index) {
			mViewPager.setCurrentItem(index - 1);
			deliverFragmentResult(fragment, resultCode, data);
		} else {
			super.finishFragment(fragment, resultCode, data);
		}
	}

	@Override
	protected void performFinishFragment(MasterFragment fragment) {
		mAdapter.notifyDataSetChanged();
	}

	private void onSlideIdle() {
		int currentItem = mViewPager.getCurrentItem();
		while (currentItem < mAdapter.getCount() - 1) {
			MasterFragment f = getActiveFragment();
			if (f.isFinishing()) {
				doFinishFragment(f);
			} else {
				f.finish();
			}
		}
	}

	@Override
	protected void onSlideEnableChanged(boolean enable) {
		super.onSlideEnableChanged(enable);
		if (isInstalled()) {
			mViewPager.setSlideEnable(enable);
		}
	}

	@Override
	protected void onSetPageTransformer(boolean reverseDrawingOrder,
			PageTransformer transformer) {
		super.onSetPageTransformer(reverseDrawingOrder, transformer);
		if (isInstalled()) {
			mViewPager.setPageTransformer(reverseDrawingOrder,
					mTransformerWrapper);
		}
	}

	private void performTransform(View page, float position) {
		if (getPageTransformer() != null) {
			getPageTransformer().transformPage(page, position);
		}
	}

	private class FragmentsAdapter extends FmPagerAdapter {

		public FragmentsAdapter(FragmentManager fm) {
			super(fm);
		}

		public MasterFragment getItem(int position) {
			MasterFragment fragment = getFragments().get(position);
			return fragment;
		}

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
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
			MasterFragment f = (MasterFragment) object;
			if (getFragments().contains(f)) {
				f.finish();
			}
		}

		@Override
		protected void onPrimaryItemChanged(ViewGroup container, int position,
				Object object) {
			super.onPrimaryItemChanged(container, position, object);
			setCallback((MasterFragment) object);
		}
	}

}
