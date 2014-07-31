package com.fragmentmaster.sample.pageanimator;

import android.view.View;

import com.fragmentmaster.animator.PageAnimator;
import com.nineoldandroids.view.ViewHelper;

public class StackAnimator extends PageAnimator {
	private static final float MIN_ALPHA = 0.5f;
	private static final float TRANSLATION_FACTOR = 0.65f;

	@Override
	protected void transformBackgroundPage(View page, float position,
			boolean enter) {
		int pageWidth = page.getWidth();
		ViewHelper.setTranslationX(page, pageWidth
				* (TRANSLATION_FACTOR * -position));
		// Fade the page out (between MIN_ALPHA and 1)
		ViewHelper.setAlpha(page, MIN_ALPHA + (1 - MIN_ALPHA) * (1 + position));
	}

	@Override
	protected void transformForegroundPage(View page, float position,
			boolean enter) {
		ViewHelper.setTranslationX(page, 0);
		ViewHelper.setAlpha(page, 1);
	}

}
