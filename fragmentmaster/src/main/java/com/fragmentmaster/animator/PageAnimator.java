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

package com.fragmentmaster.animator;

import android.view.View;

/**
 * A PageAnimator is invoked whenever a visible/attached page is scrolled. This
 * offers an opportunity for the application to apply a custom transformation to
 * the page views.
 */
public abstract class PageAnimator {

    /**
     * Apply a transformation to the given page.
     *
     * @param page     Apply the transformation to this page
     * @param position Position of page relative to the current front-and-center
     *                 position of the pager. 0 is front and center. 1 is one full
     *                 page position to the right, and -1 is one page position to the
     *                 left.
     * @param enter    true if the pager is scrolling from item to item+1.
     */
    public void transformPage(View page, float position, boolean enter) {
        if (position <= 0) { // [-1,0]
            transformBackgroundPage(page, position, enter);
        } else if (position <= 1) { // (0,1]
            transformForegroundPage(page, position, enter);
        }
    }

    /**
     * In this stage, transform the background page.
     *
     * @param position [-1,0]
     */
    protected abstract void transformBackgroundPage(View page, float position,
            boolean enter);

    /**
     * In this stage, transform the foreground page.
     *
     * @param position (0,1]
     */
    protected abstract void transformForegroundPage(View page, float position,
            boolean enter);

}
