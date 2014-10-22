package com.fragmentmaster.sample;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentmaster.app.MasterFragment;

public class Slideable extends MasterFragment implements OnPageChangeListener {

    private static final int[] COLORS = new int[]{0xFF666666, 0xFF96AA39,
            0xFFC74B46, 0xFFF4842D, 0xFF3F9FE0, 0xFF5161BC};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slideable_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new Adapter());
        viewPager.setOnPageChangeListener(this);
    }

    private class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return COLORS.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
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
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setSlideable(position == 0);
    }
}
