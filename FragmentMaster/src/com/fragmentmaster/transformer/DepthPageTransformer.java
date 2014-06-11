package com.fragmentmaster.transformer;

import android.view.View;

import com.fragmentmaster.app.FragmentMaster.PageTransformer;
import com.nineoldandroids.view.ViewHelper;

public class DepthPageTransformer implements PageTransformer {
	private static final float MIN_SCALE = 0.85f;
	private static final float MIN_ALPHA = 0.4f;

	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();

		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
			ViewHelper.setAlpha(view, 0);

		} else if (position <= 0) { // [-1,0]
			// Fade the page out.
			ViewHelper.setAlpha(view, MIN_ALPHA + (1 - MIN_ALPHA)
					* (1 + position));

			// Counteract the default slide transition
			ViewHelper.setTranslationX(view, pageWidth * -position);

			// Scale the page down (between MIN_SCALE and 1)
			float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 + position);
			ViewHelper.setScaleX(view, scaleFactor);
			ViewHelper.setScaleY(view, scaleFactor);
		} else if (position <= 1) { // (0,1]
			// Use the default slide transition when moving to the right
			// page

			ViewHelper.setAlpha(view, 1);
			ViewHelper.setTranslationX(view, 0);
			ViewHelper.setScaleX(view, 1);
			ViewHelper.setScaleY(view, 1);
		} else { // (1,+Infinity]
			// This page is way off-screen to the right.
			ViewHelper.setAlpha(view, 0);
		}
	}
}
