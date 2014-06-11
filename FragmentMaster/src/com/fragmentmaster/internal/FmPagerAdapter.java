package com.fragmentmaster.internal;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

abstract class FmPagerAdapter extends PagerAdapter {
	private static final String TAG = "FragmentPagerAdapter";
	private static final boolean DEBUG = false;

	private final FragmentManager mFragmentManager;
	private FragmentTransaction mCurTransaction = null;

	private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	private Fragment mCurrentPrimaryItem = null;

	public FmPagerAdapter(FragmentManager fm) {
		mFragmentManager = fm;
	}

	/**
	 * Return the Fragment associated with a specified position.
	 */
	public abstract Fragment getItem(int position);

	@Override
	public void startUpdate(ViewGroup container) {
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// If we already have this item instantiated, there is nothing
		// to do. This can happen when we are restoring the entire pager
		// from its saved state, where the fragment manager has already
		// taken care of restoring the fragments we previously had instantiated.
		if (mFragments.size() > position) {
			Fragment f = mFragments.get(position);
			if (f != null) {
				return f;
			}
		}

		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}

		Fragment fragment = getItem(position);
		if (DEBUG)
			Log.v(TAG, "Adding item #" + position + ": f=" + fragment);
		while (mFragments.size() <= position) {
			mFragments.add(null);
		}
		fragment.setMenuVisibility(false);
		fragment.setUserVisibleHint(false);
		mFragments.set(position, fragment);

		mCurTransaction.add(container.getId(), fragment);

		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		Fragment fragment = (Fragment) object;

		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}
		if (DEBUG)
			Log.v(TAG, "Removing item #" + position + ": f=" + object + " v="
					+ ((Fragment) object).getView());
		mFragments.remove(fragment);

		mCurTransaction.remove(fragment);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		Fragment fragment = (Fragment) object;
		if (fragment != mCurrentPrimaryItem) {
			if (mCurrentPrimaryItem != null) {
				mCurrentPrimaryItem.setMenuVisibility(false);
				mCurrentPrimaryItem.setUserVisibleHint(false);
			}
			if (fragment != null) {
				fragment.setMenuVisibility(true);
				fragment.setUserVisibleHint(true);
			}
			mCurrentPrimaryItem = fragment;

			onPrimaryItemChanged(container, position, object);
		}
	}

	protected void onPrimaryItemChanged(ViewGroup container, int position,
			Object object) {
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		if (mCurTransaction != null) {
			mCurTransaction.commitAllowingStateLoss();
			mCurTransaction = null;
			mFragmentManager.executePendingTransactions();
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return ((Fragment) object).getView() == view;
	}

	@Override
	public Parcelable saveState() {
		Bundle state = null;
		for (int i = 0; i < mFragments.size(); i++) {
			Fragment f = mFragments.get(i);
			if (f != null) {
				if (state == null) {
					state = new Bundle();
				}
				String key = "f" + i;
				mFragmentManager.putFragment(state, key, f);
			}
		}
		return state;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		if (state != null) {
			Bundle bundle = (Bundle) state;
			bundle.setClassLoader(loader);
			mFragments.clear();
			Iterable<String> keys = bundle.keySet();
			for (String key : keys) {
				if (key.startsWith("f")) {
					int index = Integer.parseInt(key.substring(1));
					Fragment f = mFragmentManager.getFragment(bundle, key);
					if (f != null) {
						while (mFragments.size() <= index) {
							mFragments.add(null);
						}
						f.setMenuVisibility(false);
						mFragments.set(index, f);
					} else {
						Log.w(TAG, "Bad fragment at key " + key);
					}
				}
			}
		}
	}
}
