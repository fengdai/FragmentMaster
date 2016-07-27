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

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentmaster.R;

import java.util.ArrayList;
import java.util.List;

class FragmentMasterImpl extends FragmentMaster implements PagerController {

    // The id of fragments' real container.
    public final static int FRAGMENT_CONTAINER_ID = R.id.internal_fragment_container;

    private FragmentsAdapter mAdapter;

    private FragmentMasterPager mViewPager;

    private OnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
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

    FragmentMasterImpl(FragmentActivity activity) {
        super(activity);
    }

    @Override
    protected void performInstall(ViewGroup container) {
        mAdapter = new FragmentsAdapter();
        mViewPager = new FragmentMasterPager(getActivity(), this);
        mViewPager.setId(FRAGMENT_CONTAINER_ID);
        mViewPager.setOffscreenPageLimit(Integer.MAX_VALUE);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
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
    }

    @Override
    protected void onFinishFragment(final IMasterFragment fragment,
                                    final int resultCode, final Request data) {
        final int index = indexOf(fragment);
        if (index != 0 && isPrimaryFragment(fragment)) {
            // If there's a PageAnimator, and the fragment to finish is the
            // primary fragment, scroll back smoothly.
            // When scrolling is stopped, real finish will be done by
            // cleanUp method.
            if (hasPageAnimator()) {
                mViewPager.setCurrentItem(index - 1, true);
                // If pager is scrolling, do real finish when cleanUp.
                deliverFragmentResult(fragment, resultCode, data);
                return;
            }
            // If there isn't a PageAnimator, this fragment will be finished immediately.
            // Before finish this fragment, finish fragments above it.
            List<IMasterFragment> fragments = new ArrayList<>(getFragments());
            for (int i = fragments.size() - 1; i > index; i--) {
                IMasterFragment f = fragments.get(i);
                if (isInFragmentMaster(f)) {
                    if (isFinishPending(f)) {
                        doFinishFragment(f);
                    } else {
                        f.finish();
                    }
                }
            }
            mViewPager.setCurrentItem(index - 1, false);
        }
        super.onFinishFragment(fragment, resultCode, data);
    }

    @Override
    protected void onFragmentFinished(IMasterFragment fragment) {
        mAdapter.notifyDataSetChanged();
    }

    private void onScrollIdle() {
        // When scrolling stopped, do cleanup.
        mViewPager.removeCallbacks(mCleanUpRunnable);
        mViewPager.post(mCleanUpRunnable);
    }

    /**
     * check whether there are any fragments above the primary fragment,
     * and finish them.
     */
    private void cleanUp() {
        List<IMasterFragment> fragments = new ArrayList<>(getFragments());
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
                if (isFinishPending(f) && !mViewPager.isScrolling()) {
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
            IMasterFragment fragment = (IMasterFragment) object;
            int position = indexOf(fragment);
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
