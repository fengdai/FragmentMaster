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

import android.annotation.SuppressLint;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

/**
 * Real container of fragments.
 */
class FragmentMasterPager extends ViewPager {

    private FragmentMasterImpl mFragmentMasterImpl;

    private static final int ANIMATION_NONE = 0;

    private static final int ANIMATION_ENTER = 1;

    private static final int ANIMATION_EXIT = 2;

    private int mAnimationState = ANIMATION_NONE;

    // The position of primary item in the latest SCROLL_STATE_IDLE state.
    private int mLatestIdleItem = 0;

    private Runnable mIdleRunnable = new Runnable() {
        public void run() {
            mLatestIdleItem = getCurrentItem();
            setAnimationState(ANIMATION_NONE);
        }
    };

    private ViewPager.PageTransformer mPageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View page, float position) {
            resetPage(page, position);
            if (mFragmentMasterImpl.hasPageAnimator()) {
                if (position < -1 || position > 1) {
                    page.setVisibility(INVISIBLE);
                } else {
                    page.setVisibility(VISIBLE);
                    mFragmentMasterImpl.getPageAnimator().transformPage(page,
                            position, mAnimationState == ANIMATION_ENTER);
                }
            } else {
                page.setVisibility(VISIBLE);
            }
        }

        private void resetPage(View page, float position) {
            page.setAlpha(1);
            page.setTranslationX(0);
            page.setTranslationY(0);
            page.setScaleX(1);
            page.setScaleY(1);
            page.setRotation(0);
            page.setRotationX(0);
            page.setRotationY(0);
            page.setPivotX(page.getWidth() / 2f);
            page.setPivotY(page.getHeight() / 2f);
        }

    };

    // Internal listener
    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                post(mIdleRunnable);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }
    };

    public FragmentMasterPager(FragmentMasterImpl fragmentMaster) {
        super(fragmentMaster.getActivity());
        mFragmentMasterImpl = fragmentMaster;
        addOnPageChangeListener(mOnPageChangeListener);
        setPageTransformer(false, mPageTransformer);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent ev) {
        return mFragmentMasterImpl.isSlideable() && !mFragmentMasterImpl.isScrolling() && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mFragmentMasterImpl.isSlideable() && !mFragmentMasterImpl.isScrolling() && super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        if (mLatestIdleItem > position) {
            // The ViewPager is performing exiting.
            setAnimationState(ANIMATION_EXIT);
        } else if (mLatestIdleItem <= position) {
            // The ViewPager is performing entering.
            setAnimationState(ANIMATION_ENTER);
        }
        super.onPageScrolled(position, offset, offsetPixels);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        mLatestIdleItem = getCurrentItem();
    }

    private void setAnimationState(int state) {
        mAnimationState = state;
    }

}
