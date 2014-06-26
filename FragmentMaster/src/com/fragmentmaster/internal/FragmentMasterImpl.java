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
		mAdapter = new FragmentsAdapter();
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
	protected int getFragmentContainerId() {
		return R.id.fragment_container;
	}

	@Override
	protected void performStartFragmentForResult(IMasterFragment fragment) {
		mAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(mAdapter.getCount() - 1);
	}

	@Override
	public void finishFragment(IMasterFragment fragment, int resultCode,
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
	protected void performFinishFragment(IMasterFragment fragment) {
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
