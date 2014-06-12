package com.fragmentmaster.sample;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentmaster.app.MasterFragment;

public class SlideEnable extends MasterFragment implements OnPageChangeListener {

	private static final int[] COLORS = new int[] { 0xFF666666, 0xFF96AA39,
			0xFFC74B46, 0xFFF4842D, 0xFF3F9FE0, 0xFF5161BC };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.slide_enable_fragment, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
		viewPager.setAdapter(new Adapter());
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void finish() {
		setSlideEnable(true);
		super.finish();
	}

	private class Adapter extends PagerAdapter {

		@Override
		public int getCount() {
			return COLORS.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.pager_card, container, false);
			view.findViewById(R.id.card).setBackgroundColor(COLORS[position]);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		setSlideEnable(arg0 == 0);
	}
}
