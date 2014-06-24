package com.fragmentmaster.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.KeyEventCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

class MasterFragmentDelegate {

	private static final String BUNDLE_KEY_REQUEST = "FragmentMaster:REQUEST";
	private static final String BUNDLE_KEY_TARGET_CHILD_FRAGMENT = "FragmentMaster:TARGET_CHILD_FRAGMENT";
	private static final String BUNDLE_KEY_SOFT_INPUT_MODE = "FragmentMaster:SOFT_INPUT_MODE";

	MasterFragment mMasterFragment;

	private MasterFragment mTargetChildFragment;

	private static final int MSG_ON_USER_ACTIVE = 1;
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_ON_USER_ACTIVE :
					performUserActive();
					break;
				default :
					super.handleMessage(msg);
			}
		}

	};

	private MasterActivity mActivity;
	private boolean mStateSaved = false;

	private int mResultCode = MasterFragment.RESULT_CANCELED;
	private Request mResultData = null;

	// SoftInputMode, SOFT_INPUT_ADJUST_UNSPECIFIED is default.
	private int mSoftInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED;

	private boolean mIsUserActive = false;
	private boolean mIsPrimary = false;

	private boolean mFinished = false;

	public MasterFragmentDelegate(MasterFragment masterFragment) {
		mMasterFragment = masterFragment;
	}

	public void onAttach(Activity activity) {
		if (activity instanceof MasterActivity) {
			mActivity = (MasterActivity) activity;
		}
	}

	public void onDetach() {
		mActivity = null;
	}

	public MasterActivity getMasterActivity() {
		return mActivity;
	}

	public FragmentMaster getFragmentMaster() {
		return mActivity == null ? null : mActivity.getFragmentMaster();
	}

	/**
	 * Starts a specific fragment.
	 */
	public void startFragment(Class<? extends MasterFragment> clazz) {
		startFragmentForResult(new Request(clazz), -1);
	}

	/**
	 * Starts a fragment.
	 * 
	 * @param request
	 *            The request.
	 */
	public void startFragment(Request request) {
		startFragmentForResult(request, -1);
	}

	public void startFragmentForResult(Class<? extends MasterFragment> clazz,
			int requestCode) {
		startFragmentForResult(new Request(clazz), requestCode);
	}

	public void startFragmentForResult(Request request, int requestCode) {
		checkState();
		if (mMasterFragment.getParentFragment() instanceof MasterFragment) {
			((MasterFragment) mMasterFragment.getParentFragment())
					.startFragmentFromChild(mMasterFragment, request,
							requestCode);
		} else {
			getFragmentMaster().startFragmentForResult(mMasterFragment,
					request, requestCode);
		}
	}

	private void checkState() {
		if (mActivity == null) {
			throw new IllegalStateException("Fragment " + this
					+ " not attached to MasterActivity!");
		}
	}

	public void startFragmentFromChild(MasterFragment childFragment,
			Request request, int requestCode) {
		if (requestCode != -1) {
			mTargetChildFragment = childFragment;
		}
		startFragmentForResult(request, requestCode);
	}

	/**
	 * Call this to set the result that your fragment will return to its caller.
	 */
	public final void setResult(int resultCode) {
		synchronized (this) {
			mResultCode = resultCode;
			mResultData = null;
		}
	}

	/**
	 * Call this to set the result that your fragment will return to its caller.
	 */
	public final void setResult(int resultCode, Request data) {
		synchronized (this) {
			mResultCode = resultCode;
			mResultData = data;
		}
	}

	public void finish() {
		checkState();

		int resultCode;
		Request resultData;
		synchronized (this) {
			resultCode = mResultCode;
			resultData = mResultData;
		}
		getFragmentMaster().finishFragment(mMasterFragment, resultCode,
				resultData);
		mFinished = true;
	}

	public boolean isFinishing() {
		return mFinished;
	}

	/**
	 * Called when the fragment has detected the user's press of the back key.
	 * The default implementation simply finishes the current fragment, but you
	 * can override this to do whatever you want.
	 */
	public void onBackPressed() {
		mMasterFragment.finish();
	}

	public Request getRequest() {
		return (Request) mMasterFragment.getArguments().get(BUNDLE_KEY_REQUEST);
	}

	public void setRequest(Request newRequest) {
		Bundle bundle = mMasterFragment.getArguments();
		bundle.putParcelable(BUNDLE_KEY_REQUEST, newRequest);
	}

	public void onSaveInstanceState(Bundle outState) {
		if (mTargetChildFragment != null) {
			mMasterFragment.getChildFragmentManager().putFragment(outState,
					BUNDLE_KEY_TARGET_CHILD_FRAGMENT, mTargetChildFragment);
		}
		outState.putInt(BUNDLE_KEY_SOFT_INPUT_MODE, mSoftInputMode);
		mStateSaved = true;
	}

	public void onCreate(Bundle savedInstanceState) {
		mStateSaved = false;
		if (savedInstanceState != null) {
			mSoftInputMode = savedInstanceState
					.getInt(BUNDLE_KEY_SOFT_INPUT_MODE);
		}
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Set the fragmentRootView's "clickable" to true to avoid
		// touch events to be passed to the views behind the fragment.
		view.setClickable(true);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		mStateSaved = false;
		if (savedInstanceState != null) {
			mTargetChildFragment = (MasterFragment) mMasterFragment
					.getChildFragmentManager().getFragment(savedInstanceState,
							BUNDLE_KEY_TARGET_CHILD_FRAGMENT);
		}
	}
	public void onStart() {
		mStateSaved = false;
	}

	public void onResume() {
		mStateSaved = false;
		if (isPrimary()) {
			mHandler.sendEmptyMessage(MSG_ON_USER_ACTIVE);
		}
	}

	public void onPause() {

		if (mHandler.hasMessages(MSG_ON_USER_ACTIVE)) {
			mHandler.removeMessages(MSG_ON_USER_ACTIVE);
			this.performUserActive();
		}

		if (isPrimary()) {
			performUserLeave();
		}
	}

	/**
	 * Whether the state have been saved by system.
	 */
	public boolean hasStateSaved() {
		return mStateSaved;
	}

	public void setSoftInputMode(int mode) {
		if (mSoftInputMode != mode) {
			mSoftInputMode = mode;
			invalidateWindowConfiguration();
		}
	}

	public int getSoftInputMode() {
		return mSoftInputMode;
	}

	public void setPrimary(boolean isPrimary) {
		mMasterFragment.setMenuVisibility(isPrimary);
		mMasterFragment.setUserVisibleHint(isPrimary);
		onSetPrimary(isPrimary);
	}

	private void onSetPrimary(boolean isPrimary) {
		boolean oldPrimaryState = mIsPrimary;
		mIsPrimary = isPrimary;
		if (!oldPrimaryState && isPrimary) {
			invalidateWindowConfiguration();
			if (mMasterFragment.isResumed()) {
				performUserActive();
			}
		} else if (oldPrimaryState && !isPrimary) {
			if (mMasterFragment.isResumed()) {
				performUserLeave();
			}
		}
	}

	private void performUserActive() {
		mIsUserActive = true;
		onUserActive();
	}

	private void performUserLeave() {
		mIsUserActive = false;
		onUserLeave();
	}

	public void invalidateWindowConfiguration() {
		if (mMasterFragment.getActivity() != null) {
			mMasterFragment.getActivity().getWindow()
					.setSoftInputMode(mSoftInputMode);
		}
	}

	public boolean isUserActive() {
		return mIsUserActive;
	}

	public boolean isPrimary() {
		return mIsPrimary;
	}

	/**
	 * Called when user has come to this fragment.
	 */
	public void onUserActive() {
	}

	/**
	 * Called when user has left this fragment.
	 */
	public void onUserLeave() {
	}

	public void setSlideEnable(boolean enable) {
		checkState();
		getFragmentMaster().setSlideEnable(enable);
	}

	public MasterFragment getTargetChildFragment() {
		return mTargetChildFragment;
	}

	public void setTargetChildFragment(MasterFragment targetChildFragment) {
		mTargetChildFragment = targetChildFragment;
	}

	// ------------------------------------------------------------------------
	// Dispatch events
	// ------------------------------------------------------------------------

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (getFragmentMaster().dispatchKeyEventToWindow(event)) {
			return true;
		}

		View view = mMasterFragment.getView();
		boolean handled = KeyEventCompat.dispatch(event, mMasterFragment,
				view != null
						? KeyEventCompat.getKeyDispatcherState(view)
						: null, this);
		if (handled) {
			return true;
		}

		return getFragmentMaster().dispatchKeyEventToActivity(event);
	}

	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
		if (getFragmentMaster().dispatchKeyEventToWindow(event)) {
			return true;
		}
		if (mMasterFragment.onKeyShortcut(event.getKeyCode(), event)) {
			return true;
		}
		return getFragmentMaster().dispatchKeyShortcutEventToActivity(event);
	}

	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (getFragmentMaster().dispatchTouchEventToWindow(ev)) {
			return true;
		}
		if (mMasterFragment.onTouchEvent(ev)) {
			return true;
		}
		return getFragmentMaster().dispatchTouchEventToActivity(ev);
	}

	public boolean dispatchTrackballEvent(MotionEvent ev) {
		if (getFragmentMaster().dispatchTrackballEventToWindow(ev)) {
			return true;
		}
		if (mMasterFragment.onTrackballEvent(ev)) {
			return true;
		}
		return getFragmentMaster().dispatchTrackballEventToActivity(ev);
	}

	public boolean dispatchGenericMotionEvent(MotionEvent ev) {
		if (getFragmentMaster().dispatchGenericMotionEventToWindow(ev)) {
			return true;
		}
		if (mMasterFragment.onGenericMotionEvent(ev)) {
			return true;
		}
		return getFragmentMaster().dispatchGenericMotionEventToActivity(ev);
	}

}
