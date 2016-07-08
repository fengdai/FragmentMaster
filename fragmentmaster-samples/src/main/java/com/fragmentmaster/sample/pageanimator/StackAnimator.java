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

package com.fragmentmaster.sample.pageanimator;

import android.view.View;

import com.fragmentmaster.animator.PageAnimator;

public class StackAnimator extends PageAnimator {
    private static final float MIN_ALPHA = 0.5f;
    private static final float TRANSLATION_FACTOR = 0.65f;

    @Override
    protected void transformBackgroundPage(View page, float position,
                                           boolean enter) {
        page.setVisibility(position == -1 ? View.INVISIBLE : View.VISIBLE);
        int pageWidth = page.getWidth();
        page.setTranslationX(pageWidth * (TRANSLATION_FACTOR * -position));
        // Fade the page out (between MIN_ALPHA and 1)
        page.setAlpha(MIN_ALPHA + (1 - MIN_ALPHA) * (1 + position));
    }

    @Override
    protected void transformForegroundPage(View page, float position,
                                           boolean enter) {
        page.setTranslationX(0);
        page.setAlpha(1);
    }

}
