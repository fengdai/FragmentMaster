package com.fragmentmaster.app;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Common MasterFragment interface.
 */
public interface IMasterFragment
		extends
			IFragmentWrapper,
			FragmentMaster.Callback,
			KeyEvent.Callback {

	/** Standard fragment result: operation canceled. */
	public static final int RESULT_CANCELED = 0;
	/** Standard fragment result: operation succeeded. */
	public static final int RESULT_OK = -1;

	/**
	 * Starts a specific fragment.
	 */
	public void startFragment(Class<? extends IMasterFragment> clazz);
	/**
	 * Starts a fragment.
	 * 
	 * @param request
	 *            The request.
	 */
	public void startFragment(Request request);

	public void startFragmentForResult(Class<? extends IMasterFragment> clazz,
			int requestCode);

	public void startFragmentForResult(Request request, int requestCode);

	public void startFragmentFromChild(IMasterFragment masterFragment,
			Request request, int requestCode);

	public void setRequest(Request request);

	public Request getRequest();

	public void setTargetChildFragment(IMasterFragment targetChildFragment);

	public IMasterFragment getTargetChildFragment();

	/**
	 * Call this to set the result that your fragment will return to its caller.
	 */
	public void setResult(int resultCode);

	/**
	 * Call this to set the result that your fragment will return to its caller.
	 */
	public void setResult(int resultCode, Request data);

	/**
	 * Finish this fragment.
	 */
	public void finish();

	public boolean isFinishing();

	public Fragment getFragment();

	public MasterActivity getMasterActivity();

	public FragmentMaster getFragmentMaster();

	/**
	 * Whether the state have been saved by system.
	 */
	public boolean hasStateSaved();

	public void setSoftInputMode(int mode);

	public int getSoftInputMode();

	public void setPrimary(boolean isPrimary);

	public boolean isPrimary();

	public boolean isUserActive();

	public void invalidateWindowConfiguration();

	public void setSlideable(boolean slideable);

	public boolean isSlideable();

	/**
	 * Called when user has come to this fragment.
	 */
	public void onUserActive();

	/**
	 * Called when user has left this fragment.
	 */
	public void onUserLeave();

	public void onFragmentResult(int requestCode, int resultCode, Request data);

	public void onBackPressed();

	public boolean onTouchEvent(MotionEvent ev);

	public boolean onKeyShortcut(int keyCode, KeyEvent event);

	public boolean onTrackballEvent(MotionEvent event);

	public boolean onGenericMotionEvent(MotionEvent event);
}
