package com.fragmentmaster.sample;

import android.os.Bundle;

import com.fragmentmaster.animator.DefaultPageAnimator;
import com.fragmentmaster.animator.PageAnimator;
import com.fragmentmaster.animator.PageAnimatorProvider;
import com.fragmentmaster.app.FragmentMaster;
import com.fragmentmaster.app.MasterActivity;
import com.fragmentmaster.app.Request;
import com.fragmentmaster.sample.pageanimator.OvershootSlideAnimator;
import com.fragmentmaster.sample.pageanimator.VerticalSlideAnimator;

public class MainActivity extends MasterActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentMaster fragmentMaster = getFragmentMaster();
		fragmentMaster.setAnimatorProvider(new MyAnimatorProvider());
		fragmentMaster.install(R.id.container, new Request(Home.class), true);
	}

	public static class MyAnimatorProvider implements PageAnimatorProvider {

		private static final PageAnimator DEFAULT_PAGE_ANIMATOR = new DefaultPageAnimator();

		public static final int VERTICAL_SLIDE_ANIMATOR_ID = 1;
		public static final int OVER_SHOOT_SLIDE_ANIMATOR_ID = 2;

		private static final PageAnimator VERTICAL_SLIDE_ANIMATOR = new VerticalSlideAnimator();
		private static final PageAnimator OVER_SHOOT_SLIDE_ANIMATOR = new OvershootSlideAnimator();

		@Override
		public PageAnimator getPageAnimator(int pageAnimatorId) {
			switch (pageAnimatorId) {
			case VERTICAL_SLIDE_ANIMATOR_ID:
				return VERTICAL_SLIDE_ANIMATOR;
			case OVER_SHOOT_SLIDE_ANIMATOR_ID:
				return OVER_SHOOT_SLIDE_ANIMATOR;
			default:
				return DEFAULT_PAGE_ANIMATOR;
			}
		}
	}
}
