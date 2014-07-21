package com.fragmentmaster.sample;

import android.os.Bundle;
import android.view.View;

import com.fragmentmaster.animator.PageAnimator;
import com.fragmentmaster.animator.PageAnimatorProvider;
import com.fragmentmaster.app.FragmentMaster;
import com.fragmentmaster.app.MasterActivity;
import com.fragmentmaster.app.Request;
import com.fragmentmaster.sample.pageanimator.Animators;
import com.nineoldandroids.view.ViewHelper;

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

		public static final int VERTICAL_SLIDE_ANIMATOR_ID = 1;
		public static final int OVER_SHOOT_SLIDE_ANIMATOR_ID = 2;

		@Override
		public PageAnimator getPageAnimator(int pageAnimatorId) {
			switch (pageAnimatorId) {
			case VERTICAL_SLIDE_ANIMATOR_ID:
				return Animators.VERTICAL_SLIDE_ANIMATOR;
			case OVER_SHOOT_SLIDE_ANIMATOR_ID:
				return Animators.OVER_SHOOT_SLIDE_ANIMATOR;
			default:
				return Animators.DEFAULT_PAGE_ANIMATOR;
			}
		}

		@Override
		public void resetPage(View page) {
			ViewHelper.setAlpha(page, 1);
			ViewHelper.setTranslationX(page, 0);
			ViewHelper.setTranslationY(page, 0);
			ViewHelper.setScaleX(page, 1);
			ViewHelper.setScaleY(page, 1);
			ViewHelper.setRotation(page, 0);
			ViewHelper.setRotationX(page, 0);
			ViewHelper.setRotationY(page, 0);
			ViewHelper.setPivotX(page, page.getWidth() / 2);
			ViewHelper.setPivotY(page, page.getHeight() / 2);
		}
	}
}
