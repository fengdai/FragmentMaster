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
			transformPageLeft(page, position, enter);
		} else if (position <= 1) { // (0,1]
			transformPageRight(page, position, enter);
		}
	}

	/**
	 * In this stage, the left part of the page is beyond the left side of the
	 * screen.
	 */
	protected abstract void transformPageLeft(View page, float position,
			boolean enter);

	/**
	 * In this stage, the right part of the page is beyond the right side of the
	 * screen.
	 */
	protected abstract void transformPageRight(View page, float position,
			boolean enter);

}
