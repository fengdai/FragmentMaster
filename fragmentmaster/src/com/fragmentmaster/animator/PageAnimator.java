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
	 * @param page
	 *            Apply the transformation to this page
	 * @param position
	 *            Position of page relative to the current front-and-center
	 *            position of the pager. 0 is front and center. 1 is one full
	 *            page position to the right, and -1 is one page position to the
	 *            left.
	 * @param enter
	 *            true if the pager is scrolling from item to item+1.
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
	 * @param position
	 *            [-1,0]
	 */
	protected abstract void transformBackgroundPage(View page, float position,
			boolean enter);

	/**
	 * In this stage, transform the foreground page.
	 * 
	 * @param position
	 *            (0,1]
	 */
	protected abstract void transformForegroundPage(View page, float position,
			boolean enter);

}
