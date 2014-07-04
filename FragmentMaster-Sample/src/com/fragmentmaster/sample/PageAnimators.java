package com.fragmentmaster.sample;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fragmentmaster.animator.PageAnimator;
import com.fragmentmaster.app.MasterFragment;
import com.fragmentmaster.app.MasterListFragment;
import com.fragmentmaster.app.Request;
import com.fragmentmaster.sample.MainActivity.MyAnimatorProvider;
import com.fragmentmaster.sample.pageanimator.OvershootSlideAnimator;
import com.fragmentmaster.sample.pageanimator.VerticalSlideAnimator;

public class PageAnimators extends MasterListFragment {

	static class Page {
		CharSequence name;
		Request request;
		PageAnimator pageAnimator;

		Page(CharSequence name, Request request, PageAnimator pageAnimator) {
			this.name = name;
			this.request = request;
			this.pageAnimator = pageAnimator;
		}

		@Override
		public String toString() {
			return name.toString();
		}
	}

	private static final List<Page> PAGES = new ArrayList<Page>();

	static {
		PAGES.add(new Page("VerticalSlideAnimator", new Request(
				VerticalSlidePage.class), new VerticalSlideAnimator()));
		PAGES.add(new Page("OvershootSlideAnimator", new Request(
				OvershootSlidePage.class), new OvershootSlideAnimator()));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		setListAdapter(new ArrayAdapter<Page>(getActivity(),
				android.R.layout.simple_list_item_1, PAGES));
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Page page = PAGES.get(position);
		startFragment(page.request);
	}

	public static class VerticalSlidePage extends MasterFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.vertical_slide_animator_fragment,
					container, false);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			// As this fragment performs vertical slide animation. Don't let
			// this kind of fragment to be horizontally slideable;
			setSlideable(false);
		}

		@Override
		public int onCreatePageAnimator() {
			return MyAnimatorProvider.VERTICAL_SLIDE_ANIMATOR_ID;
		}
	}

	public static class OvershootSlidePage extends MasterFragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.overshoot_slide_animator_fragment,
					container, false);
		}

		@Override
		public int onCreatePageAnimator() {
			return MyAnimatorProvider.OVER_SHOOT_SLIDE_ANIMATOR_ID;
		}
	}
}
