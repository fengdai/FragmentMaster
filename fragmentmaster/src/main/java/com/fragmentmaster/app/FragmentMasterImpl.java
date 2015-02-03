/*
 * Copyright 2014 Feng Dai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fragmentmaster.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentmaster.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentMasterImpl extends FragmentMaster {

    // The id of fragments' real container.
    public final static int FRAGMENT_CONTAINER_ID = R.id.internal_fragment_container;

    private FragmentsAdapter mAdapter;

    private FragmentMasterPager mViewPager;

    private boolean mScrolling = false;

    private int mState = ViewPager.SCROLL_STATE_IDLE;

    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            if (mState == ViewPager.SCROLL_STATE_IDLE) {
                setUpAnimator(getPrimaryFragment());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mState = state;
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                mViewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        setUpAnimator(getPrimaryFragment());
                    }
                });
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

    FragmentMasterImpl(FragmentActivity activity) {
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
    protected void onFinishFragment(final IMasterFragment fragment,
                                    final int resultCode, final Request data) {
        final int index = getFragments().indexOf(fragment);
        int curItem = mViewPager.getCurrentItem();

        if (hasPageAnimator() && curItem == index && index != 0) {
            // If there's a PageAnimator, and the fragment to finish is the
            // primary fragment, scroll back smoothly.
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
        super.onFinishFragment(fragment, resultCode, data);
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

    /**
     * check whether there are any fragments above the primary fragment,
     * and finish them.
     */
    private void cleanUp() {
        List<IMasterFragment> fragments = new ArrayList<IMasterFragment>(getFragments());
        IMasterFragment primaryFragment = getPrimaryFragment();
        // determine whether f is above primary fragment.
        boolean abovePrimary = true;
        for (int i = fragments.size() - 1; i >= 0; i--) {
            IMasterFragment f = fragments.get(i);
            if (f == primaryFragment) {
                abovePrimary = false;
            }

            if (abovePrimary) {
                // All fragments above primary fragment should be finished.
                if (isInFragmentMaster(f)) {
                    if (isFinishPending(f)) {
                        doFinishFragment(f);
                    } else {
                        f.finish();
                    }
                }
            } else {
                if (isFinishPending(f) && !mScrolling) {
                    doFinishFragment(f);
                }
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
            return getFragments().get(position);
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
