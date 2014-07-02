package com.fragmentmaster.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * The base fragment
 */
public class MasterFragment extends Fragment implements IMasterFragment {

	private MasterFragmentDelegate mImpl = new MasterFragmentDelegate(this);

	public MasterFragment() {
	}

	// ------------------------------------------------------------------------
	// Lifecycle
	// ------------------------------------------------------------------------

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mImpl.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImpl.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mImpl.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mImpl.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		mImpl.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		mImpl.onResume();
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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mImpl.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
		mImpl.onPause();
	}

	public void onDetach() {
		super.onDetach();
		mImpl.onDetach();
	}

	// ------------------------------------------------------------------------
	// MasterFragment features
	// ------------------------------------------------------------------------

	@Override
	public Fragment getFragment() {
		return this;
	}

	public MasterActivity getMasterActivity() {
		return mImpl.getMasterActivity();
	}

	public FragmentMaster getFragmentMaster() {
		return mImpl.getFragmentMaster();
	}

	/**
	 * Starts a specific fragment.
	 */
	public void startFragment(Class<? extends IMasterFragment> clazz) {
		mImpl.startFragment(clazz);
	}

	/**
	 * Starts a fragment.
	 * 
	 * @param request
	 *            The request.
	 */
	public void startFragment(Request request) {
		mImpl.startFragment(request);
	}

	public void startFragmentForResult(Class<? extends IMasterFragment> clazz,
			int requestCode) {
		mImpl.startFragmentForResult(clazz, requestCode);
	}

	public void startFragmentForResult(Request request, int requestCode) {
		mImpl.startFragmentForResult(request, requestCode);
	}

	public void startFragmentFromChild(IMasterFragment childFragment,
			Request request, int requestCode) {
		mImpl.startFragmentFromChild(childFragment, request, requestCode);
	}

	/**
	 * Call this to set the result that your fragment will return to its caller.
	 */
	public final void setResult(int resultCode) {
		mImpl.setResult(resultCode);
	}

	/**
	 * Call this to set the result that your fragment will return to its caller.
	 */
	public final void setResult(int resultCode, Request data) {
		mImpl.setResult(resultCode, data);
	}

	public void finish() {
		mImpl.finish();
	}

	public boolean isFinishing() {
		return mImpl.isFinishing();
	}

	public Request getRequest() {
		return mImpl.getRequest();
	}

	public void setRequest(Request newRequest) {
		mImpl.setRequest(newRequest);
	}

	/**
	 * Whether the state have been saved by system.
	 */
	public boolean hasStateSaved() {
		return mImpl.hasStateSaved();
	}

	public void setSoftInputMode(int mode) {
		mImpl.setSoftInputMode(mode);
	}

	public int getSoftInputMode() {
		return mImpl.getSoftInputMode();
	}

	public void setPrimary(boolean isPrimary) {
		mImpl.setPrimary(isPrimary);
	}

	public void invalidateWindowConfiguration() {
		mImpl.invalidateWindowConfiguration();
	}

	public boolean isUserActive() {
		return mImpl.isUserActive();
	}

	public boolean isPrimary() {
		return mImpl.isPrimary();
	}

	public void setSlideable(boolean slideable) {
		mImpl.setSlideable(slideable);
	}

	@Override
	public boolean isSlideable() {
		return mImpl.isSlideable();
	}

	public void onFragmentResult(int requestCode, int resultCode, Request data) {
	}

	public IMasterFragment getTargetChildFragment() {
		return mImpl.getTargetChildFragment();
	}

	public void setTargetChildFragment(IMasterFragment targetChildFragment) {
		mImpl.setTargetChildFragment(targetChildFragment);
	}

	/**
	 * Called when the fragment has detected the user's press of the back key.
	 * The default implementation simply finishes the current fragment, but you
	 * can override this to do whatever you want.
	 */
	public void onBackPressed() {
		mImpl.onBackPressed();
	}

	// ------------------------------------------------------------------------
	// Dispatch events
	// ------------------------------------------------------------------------

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return mImpl.dispatchKeyEvent(event);
	}

	@Override
	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
		return mImpl.dispatchKeyShortcutEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return mImpl.dispatchTouchEvent(ev);
	}

	@Override
	public boolean dispatchTrackballEvent(MotionEvent ev) {
		return mImpl.dispatchTrackballEvent(ev);
	}

	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent ev) {
		return mImpl.dispatchGenericMotionEvent(ev);
	}

	// ------------------------------------------------------------------------
	// Handle events
	// ------------------------------------------------------------------------

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ECLAIR) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				onBackPressed();
				return true;
			}
		} else {
			event.startTracking();
		}
		return false;
	}

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
					&& !event.isCanceled()) {
				onBackPressed();
				return true;
			}
		}
		return false;
	}

	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		return false;
	}

	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		return false;
	}

	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		return false;
	}

	public boolean onTrackballEvent(MotionEvent event) {
		return false;
	}

	public boolean onGenericMotionEvent(MotionEvent event) {
		return false;
	}

}
