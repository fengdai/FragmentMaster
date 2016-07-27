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

import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

import com.fragmentmaster.animator.NoAnimator;
import com.fragmentmaster.animator.PageAnimator;

/**
 * Real container of fragments.
 */
class FragmentMasterPager extends ViewPager {

    private static final int FROWARD = 0;

    private static final int BACKWARD = 1;

    private PagerController mPagerController;

    private int mScrollDirection = FROWARD;

    private int mLastPosition = 0;

    private float mLastScrollOffset = 0f;

    private int mCurScrollState = SCROLL_STATE_IDLE;

    private ViewPager.PageTransformer mPageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View page, float position) {
            PageAnimator animator = mPagerController.getPageAnimator();
            animator = animator != null ? animator : NoAnimator.INSTANCE;
            boolean enter = (mScrollDirection == FROWARD) && (mCurScrollState != SCROLL_STATE_DRAGGING);
            animator.transformPage(page, position, enter);
        }
    };

    // Internal listener
    private OnPageChangeListener mOnPageChangeListener = new SimpleOnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {
            mCurScrollState = state;
        }
    };

    public FragmentMasterPager(FragmentActivity activity, PagerController pagerController) {
        super(activity);
        mPagerController = pagerController;
        addOnPageChangeListener(mOnPageChangeListener);
        setPageTransformer(false, mPageTransformer);
    }

    public boolean isScrolling() {
        return mCurScrollState != SCROLL_STATE_IDLE;
    }

    private boolean interceptTouch() {
        return mCurScrollState == SCROLL_STATE_SETTLING;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return canScroll() && !interceptTouch() && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return canScroll() && (interceptTouch() || super.onInterceptTouchEvent(ev));
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        float prePosition = mLastPosition + mLastScrollOffset;
        float curPosition = position + offset;
        if (prePosition > curPosition) {
            // The ViewPager is performing exiting.
            mScrollDirection = BACKWARD;
        } else if (prePosition < curPosition) {
            // The ViewPager is performing entering.
            mScrollDirection = FROWARD;
        }
        mLastScrollOffset = offset;
        mLastPosition = position;
        super.onPageScrolled(position, offset, offsetPixels);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        mLastPosition = getCurrentItem();
        mLastScrollOffset = 0f;
    }

    private boolean canScroll() {
        return mPagerController.allowSwipeBack() && mPagerController.hasPageAnimator();
    }
}
