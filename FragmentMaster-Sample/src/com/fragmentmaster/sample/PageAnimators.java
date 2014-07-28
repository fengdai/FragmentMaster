package com.fragmentmaster.sample;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fragmentmaster.animator.PageAnimator;
import com.fragmentmaster.app.MasterFragment;
import com.fragmentmaster.app.MasterListFragment;
import com.fragmentmaster.app.Request;
import com.fragmentmaster.sample.pageanimator.Animators;

public class PageAnimators extends MasterListFragment {

	static class Page {
		CharSequence name;
		Request request;

		Page(CharSequence name, Request request) {
			this.name = name;
			this.request = request;
		}

		@Override
		public String toString() {
			return name.toString();
		}
	}

	private static final List<Page> PAGES = new ArrayList<Page>();

	static {
		PAGES.add(new Page("StackAnimator", new Request(StackPage.class)));
		PAGES.add(new Page("EnterOvershootAnimator", new Request(
				EnterOvershootPage.class)));
		PAGES.add(new Page("VerticalSlideAnimator", new Request(
				VerticalSlidePage.class)));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
		public PageAnimator onCreatePageAnimator() {
			return Animators.VERTICAL_SLIDE_ANIMATOR;
		}
	}

	public static class EnterOvershootPage extends MasterFragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.overshoot_slide_animator_fragment,
					container, false);
		}

		@Override
		public PageAnimator onCreatePageAnimator() {
			return Animators.ENTER_OVER_SHOOT_ANIMATOR;
		}
	}

	public static class StackPage extends MasterFragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.overshoot_slide_animator_fragment,
					container, false);
		}

		@Override
		public PageAnimator onCreatePageAnimator() {
			return Animators.STACK_ANIMATOR;
		}
	}
}
