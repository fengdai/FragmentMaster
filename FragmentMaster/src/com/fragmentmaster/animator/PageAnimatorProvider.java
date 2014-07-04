package com.fragmentmaster.animator;

public interface PageAnimatorProvider {
	public static final int DEFAULT_PAGE_ANIMATOR_ID = 0;

	public PageAnimator getPageAnimator(int pageAnimatorId);
}
