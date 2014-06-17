package com.fragmentmaster.transformer;

import android.view.View;

import com.fragmentmaster.app.FragmentMaster.PageTransformer;
import com.nineoldandroids.view.ViewHelper;

public class StaticPageTransformer implements PageTransformer {

	@Override
	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();

		if (position < -1) { // [-Infinity,-1)
			ViewHelper.setAlpha(view, 0);
		} else if (position <= 0) { // [-1,0]
			ViewHelper.setAlpha(view, 1);
			ViewHelper.setTranslationX(view, pageWidth * -position);
			ViewHelper.setScaleX(view, 1);
			ViewHelper.setScaleY(view, 1);
		} else if (position <= 1) { // (0,1]
			ViewHelper.setAlpha(view, 1);
			ViewHelper.setTranslationX(view, pageWidth * -position);
			ViewHelper.setScaleX(view, 1);
			ViewHelper.setScaleY(view, 1);
		} else { // (1,+Infinity]
			ViewHelper.setAlpha(view, 0);
		}
	}
}
