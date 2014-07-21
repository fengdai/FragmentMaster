package com.fragmentmaster.animator;

import android.view.View;

public interface PageAnimatorProvider {
	public static final int DEFAULT_PAGE_ANIMATOR_ID = 0;

	public PageAnimator getPageAnimator(int pageAnimatorId);

	public void resetPage(View page);
}
