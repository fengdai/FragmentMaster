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
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPagerCompat;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Real container of fragments.
 */
class FragmentMasterPager extends ViewPagerCompat {

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
            ViewHelper.setAlpha(page, 1);
            ViewHelper.setTranslationX(page, 0);
            ViewHelper.setTranslationY(page, 0);
            ViewHelper.setScaleX(page, 1);
            ViewHelper.setScaleY(page, 1);
            ViewHelper.setRotation(page, 0);
            ViewHelper.setRotationX(page, 0);
            ViewHelper.setRotationY(page, 0);
            ViewHelper.setPivotX(page, page.getWidth() / 2f);
            ViewHelper.setPivotY(page, page.getHeight() / 2f);
        }

    };

    // Internal listener
    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {
            if (mWrappedOnPageChangeListener != null) {
                mWrappedOnPageChangeListener.onPageScrollStateChanged(state);
            }

            if (state == ViewPager.SCROLL_STATE_IDLE) {
                post(mIdleRunnable);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            if (mWrappedOnPageChangeListener != null) {
                mWrappedOnPageChangeListener.onPageScrolled(position,
                        positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mWrappedOnPageChangeListener != null) {
                mWrappedOnPageChangeListener.onPageSelected(position);
            }
        }
    };

    private OnPageChangeListener mWrappedOnPageChangeListener;

    public FragmentMasterPager(FragmentMasterImpl fragmentMaster) {
        super(fragmentMaster.getActivity());
        mFragmentMasterImpl = fragmentMaster;
        super.setOnPageChangeListener(mOnPageChangeListener);
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
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mWrappedOnPageChangeListener = listener;
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
