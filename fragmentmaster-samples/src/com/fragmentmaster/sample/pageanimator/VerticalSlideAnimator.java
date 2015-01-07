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
import com.nineoldandroids.view.ViewHelper;

public class VerticalSlideAnimator extends PageAnimator {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    protected void transformBackgroundPage(View page, float position,
                                           boolean enter) {
        int pageWidth = page.getWidth();

        // Counteract the default slide transition
        ViewHelper.setTranslationX(page, pageWidth * -position);

        // Fade the page out (between MIN_ALPHA and 1)
        ViewHelper.setAlpha(page, MIN_ALPHA + (1 - MIN_ALPHA) * (1 + position));

        // Scale the page down (between MIN_SCALE and 1)
        float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 + position);
        ViewHelper.setScaleX(page, scaleFactor);
        ViewHelper.setScaleY(page, scaleFactor);
    }

    @Override
    protected void transformForegroundPage(View page, float position,
                                           boolean enter) {
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();
        ViewHelper.setTranslationX(page, pageWidth * -position);
        ViewHelper.setTranslationY(page, pageHeight * position);
        ViewHelper.setAlpha(page, 1);
        ViewHelper.setScaleX(page, 1);
        ViewHelper.setScaleY(page, 1);
    }
}
