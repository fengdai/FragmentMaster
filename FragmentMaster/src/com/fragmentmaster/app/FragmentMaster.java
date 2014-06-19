package com.fragmentmaster.app;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.KeyEventCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public abstract class FragmentMaster {

	private static final String TAG = "FragmentMaster";

	// The host activity.
	private final MasterActivity mActivity;
	private final FragmentManager mFragmentManager;
	private int mContainerResID = 0;
	private ViewGroup mContainer;
	private boolean mIsSlideEnable = true;
	private boolean mIsInstalled = false;
	private boolean mSticky = false;
	private boolean mHomeFragmentApplied = false;

	private boolean mReverseDrawingOrder = false;
	private PageTransformer mPageTransformer;

	// Fragments started by FragmentMaster.
	private ArrayList<MasterFragment> mFragments = new ArrayList<MasterFragment>();

	// Events callback
	private Callback mCallback = null;

	/**
	 * A PageTransformer is invoked whenever a visible/attached page is
	 * scrolled. This offers an opportunity for the application to apply a
	 * custom transformation to the page views.
	 */
	public interface PageTransformer {
		/**
		 * Apply a transformation to the given page.
		 * 
		 * @param page
		 *            Apply the transformation to this page
		 * @param position
		 *            Position of page relative to the current front-and-center
		 *            position of the pager. 0 is front and center. 1 is one
		 *            full page position to the right, and -1 is one page
		 *            position to the left.
		 */
		public void transformPage(View page, float position);
	}

	public interface Callback {
		public boolean dispatchKeyEvent(KeyEvent event);
		public boolean dispatchKeyShortcutEvent(KeyEvent event);
		public boolean dispatchTouchEvent(MotionEvent event);
		public boolean dispatchTrackballEvent(MotionEvent event);
		public boolean dispatchGenericMotionEvent(MotionEvent event);
	}

	protected FragmentMaster(MasterActivity activity) {
		mActivity = activity;
		mFragmentManager = activity.getSupportFragmentManager();
	}

	public FragmentActivity getActivity() {
		return mActivity;
	}

	public FragmentManager getFragmentManager() {
		return mFragmentManager;
	}

	public int getContainerResID() {
		return mContainerResID;
	}

	protected int getFragmentContainerId() {
		return getContainerResID();
	}

	public void startFragmentForResult(MasterFragment target, Request request,
			int requestCode) {
		ensureInstalled();

		MasterFragment fragment = newFragment(request.getClassName());
		fragment.setRequest(request);
		fragment.setTargetFragment(target, requestCode);
		mFragmentManager.beginTransaction()
				.add(getFragmentContainerId(), fragment)
				.commitAllowingStateLoss();
		mFragmentManager.executePendingTransactions();
		mFragments.add(fragment);

		performStartFragmentForResult(fragment);
	}

	protected abstract void performStartFragmentForResult(
			MasterFragment fragment);

	private MasterFragment newFragment(String className) {
		try {
			return (MasterFragment) MasterFragment.instantiate(getActivity(),
					className, new Bundle());
		} catch (Exception e) {
			throw new RuntimeException("No fragment found : { className="
					+ className + " }");
		}
	}

	public void finishFragment(MasterFragment fragment, int resultCode,
			Request data) {
		ensureInstalled();

		doFinishFragment(fragment);
		deliverFragmentResult(fragment, resultCode, data);
	}

	protected void doFinishFragment(MasterFragment fragment) {
		int index = mFragments.indexOf(fragment);
		if (index < 0) {
			throw new IllegalStateException("Fragment {" + fragment
					+ "} not currently in FragmentMaster.");
		}

		if (index == 0 && mSticky) {
			mActivity.finish();
			return;
		}

		mFragmentManager.beginTransaction().remove(fragment)
				.commitAllowingStateLoss();
		mFragmentManager.executePendingTransactions();
		mFragments.remove(index);

		MasterFragment f = null;
		for (int i = index; i < mFragments.size(); i++) {
			f = mFragments.get(i);
			MasterFragment target = (MasterFragment) f.getTargetFragment();
			if (target == fragment) {
				f.setTargetFragment(null, -1);
			}
		}

		performFinishFragment(fragment);
	}

	protected void deliverFragmentResult(MasterFragment fragment,
			int resultCode, Request data) {
		Fragment targetFragment = fragment.getTargetFragment();
		int requestCode = fragment.getTargetRequestCode();
		if (requestCode != -1 && targetFragment instanceof MasterFragment) {
			dispatchFragmentResult((MasterFragment) targetFragment,
					fragment.getTargetRequestCode(), resultCode, data);
		}
	}

	void dispatchFragmentResult(MasterFragment who, int requestCode,
			int resultCode, Request data) {
		if (who.mTargetChildFragment == null) {
			who.onFragmentResult(requestCode, resultCode, data);
		} else {
			dispatchFragmentResult(who.mTargetChildFragment, requestCode,
					resultCode, data);
		}
		who.mTargetChildFragment = null;
	}

	private void ensureInstalled() {
		if (!isInstalled()) {
			throw new IllegalStateException("Haven't installed.");
		}
	}

	protected abstract void performFinishFragment(MasterFragment fragment);

	public MasterFragment getActiveFragment() {
		MasterFragment activeFragment = null;
		if (mFragments.size() > 0) {
			activeFragment = mFragments.get(mFragments.size() - 1);
		}
		return activeFragment;
	}

	public List<MasterFragment> getFragments() {
		return mFragments;
	}

	public final void setPageTransformer(boolean reverseDrawingOrder,
			PageTransformer transformer) {
		mReverseDrawingOrder = reverseDrawingOrder;
		mPageTransformer = transformer;
		onSetPageTransformer(reverseDrawingOrder, transformer);
	}

	protected void onSetPageTransformer(boolean reverseDrawingOrder,
			PageTransformer transformer) {
	}

	public PageTransformer getPageTransformer() {
		return mPageTransformer;
	}

	public boolean isReverseDrawingOrder() {
		return mReverseDrawingOrder;
	}

	public final void install(int containerResID, Request homeRequest,
			boolean sticky) {
		if (isInstalled()) {
			throw new IllegalStateException("Already installed!");
		} else {
			mContainerResID = containerResID;
			checkInstallProperties();
			performInstall(mContainer);
			mIsInstalled = true;

			if (homeRequest != null) {
				applyHomeFragment(homeRequest, sticky);
			}
		}
	}

	private void applyHomeFragment(Request homeRequest, boolean sticky) {
		mSticky = sticky;
		if (!mHomeFragmentApplied) {
			startFragmentForResult(null, homeRequest, -1);
			mHomeFragmentApplied = true;
		}
	}

	private void checkInstallProperties() {
		View container = mActivity.findViewById(mContainerResID);
		if (container == null) {
			throw new RuntimeException("No view found for id 0x"
					+ Integer.toHexString(mContainerResID));
		} else {
			mContainer = (ViewGroup) container;
		}
	}

	protected abstract void performInstall(ViewGroup container);

	public boolean isInstalled() {
		return mIsInstalled;
	}

	public final void setSlideEnable(boolean enable) {
		if (enable != mIsSlideEnable) {
			mIsSlideEnable = enable;
			onSlideEnableChanged(enable);
		}
	}

	protected void onSlideEnableChanged(boolean enable) {
	}

	public boolean isSlideEnable() {
		return mIsSlideEnable;
	}

	Parcelable saveAllState() {
		FragmentMasterState state = new FragmentMasterState();
		Bundle fragments = null;
		for (int i = 0; i < mFragments.size(); i++) {
			Fragment f = mFragments.get(i);
			if (f != null) {
				if (fragments == null) {
					fragments = new Bundle();
				}
				String key = "f" + i;
				mFragmentManager.putFragment(fragments, key, f);
			}
		}
		state.mFragments = fragments;
		state.mIsSlideEnable = mIsSlideEnable;
		state.mHomeFragmemtApplied = mHomeFragmentApplied;

		logState();
		return state;
	}

	private void logState() {
		int fragmentsInManagerCount = 0;
		for (Fragment f : mFragmentManager.getFragments()) {
			if (f != null)
				fragmentsInManagerCount++;
		}
		Log.d(TAG, "STATE FragmentMaster[" + mFragments.size()
				+ "], FragmentManager[" + fragmentsInManagerCount
				+ "], mIsSlideEnable[" + mIsSlideEnable
				+ "], mHomeFragmemtApplied[" + mHomeFragmentApplied + "]");
	}

	void restoreAllState(Parcelable state) {
		if (state != null) {
			FragmentMasterState fms = (FragmentMasterState) state;

			mFragments.clear();
			Bundle fragments = fms.mFragments;
			if (fragments != null) {
				Iterable<String> keys = fragments.keySet();
				for (String key : keys) {
					if (key.startsWith("f")) {
						int index = Integer.parseInt(key.substring(1));
						MasterFragment f = (MasterFragment) mFragmentManager
								.getFragment(fragments, key);
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

			setSlideEnable(fms.mIsSlideEnable);
			mHomeFragmentApplied = fms.mHomeFragmemtApplied;
		}
	}

	public void setCallback(Callback callback) {
		mCallback = callback;
	}

	public Callback getCallback() {
		return mCallback;
	}

	// ------------------------------------------------------------------------
	// Dispatch events
	// ------------------------------------------------------------------------

	protected boolean dispatchKeyEvent(KeyEvent event) {
		if (mCallback != null) {
			return mCallback.dispatchKeyEvent(event);
		}
		return mActivity.superDispatchKeyEvent(event);
	}

	final boolean dispatchKeyEventToWindow(KeyEvent event) {
		mActivity.onUserInteraction();
		Window win = mActivity.getWindow();
		if (win.superDispatchKeyEvent(event)) {
			return true;
		}
		return false;
	}

	final boolean dispatchKeyEventToActivity(KeyEvent event) {
		final View decor = mActivity.getWindow().getDecorView();
		return KeyEventCompat.dispatch(event, mActivity, decor != null
				? KeyEventCompat.getKeyDispatcherState(decor)
				: null, mActivity);
	}

	protected boolean dispatchKeyShortcutEvent(KeyEvent event) {
		if (mCallback != null) {
			return mCallback.dispatchKeyShortcutEvent(event);
		}
		return mActivity.superDispatchKeyShortcutEvent(event);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	final boolean dispatchKeyShortcutEventToWindow(KeyEvent event) {
		mActivity.onUserInteraction();
		if (mActivity.getWindow().superDispatchKeyShortcutEvent(event)) {
			return true;
		}
		return false;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	final boolean dispatchKeyShortcutEventToActivity(KeyEvent event) {
		return mActivity.onKeyShortcut(event.getKeyCode(), event);
	}

	protected boolean dispatchTouchEvent(MotionEvent event) {
		if (mCallback != null) {
			return mCallback.dispatchTouchEvent(event);
		}
		return mActivity.superDispatchTouchEvent(event);
	}

	final boolean dispatchTouchEventToWindow(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			mActivity.onUserInteraction();
		}
		if (mActivity.getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return false;
	}

	final boolean dispatchTouchEventToActivity(MotionEvent ev) {
		return mActivity.onTouchEvent(ev);
	}

	protected boolean dispatchGenericMotionEvent(MotionEvent ev) {
		if (mCallback != null) {
			return mCallback.dispatchGenericMotionEvent(ev);
		}
		return mActivity.superDispatchGenericMotionEvent(ev);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	final boolean dispatchGenericMotionEventToWindow(MotionEvent ev) {
		mActivity.onUserInteraction();
		if (mActivity.getWindow().superDispatchGenericMotionEvent(ev)) {
			return true;
		}
		return false;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	final boolean dispatchGenericMotionEventToActivity(MotionEvent ev) {
		return mActivity.onGenericMotionEvent(ev);
	}

	protected boolean dispatchTrackballEvent(MotionEvent ev) {
		if (mCallback != null) {
			return mCallback.dispatchTrackballEvent(ev);
		}
		return mActivity.superDispatchTrackballEvent(ev);
	}

	final boolean dispatchTrackballEventToWindow(MotionEvent ev) {
		mActivity.onUserInteraction();
		if (mActivity.getWindow().superDispatchTrackballEvent(ev)) {
			return true;
		}
		return false;
	}

	final boolean dispatchTrackballEventToActivity(MotionEvent ev) {
		return mActivity.onTrackballEvent(ev);
	}
}

final class FragmentMasterState implements Parcelable {

	Bundle mFragments;
	boolean mIsSlideEnable;
	boolean mHomeFragmemtApplied;

	public FragmentMasterState() {
	}

	public FragmentMasterState(Parcel in) {
		mFragments = in.readBundle();
		mIsSlideEnable = in.readInt() == 0;
		mHomeFragmemtApplied = in.readInt() == 0;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeBundle(mFragments);
		dest.writeInt(mIsSlideEnable ? 0 : 1);
		dest.writeInt(mHomeFragmemtApplied ? 0 : 1);
	}

	public static final Parcelable.Creator<FragmentMasterState> CREATOR = new Parcelable.Creator<FragmentMasterState>() {
		public FragmentMasterState createFromParcel(Parcel in) {
			return new FragmentMasterState(in);
		}

		public FragmentMasterState[] newArray(int size) {
			return new FragmentMasterState[size];
		}
	};
}
