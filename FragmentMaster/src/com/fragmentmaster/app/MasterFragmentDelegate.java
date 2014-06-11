package com.fragmentmaster.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public interface MasterFragmentDelegate {
	/** Standard fragment result: operation canceled. */
	public static final int RESULT_CANCELED = 0;
	/** Standard fragment result: operation succeeded. */
	public static final int RESULT_OK = -1;

	public void onAttach(Activity activity);

	public void onDetach();

	public MasterActivity getMasterActivity();
	public FragmentMaster getMaster();
	/**
	 * Starts a specific fragment.
	 */
	public void startFragment(Class<? extends MasterFragment> clazz);
	/**
	 * Starts a fragment.
	 * 
	 * @param request
	 *            The request.
	 */
	public void startFragmentForResult(Request request, int requestCode);

	/**
	 * Call this to set the result that your fragment will return to its caller.
	 */
	public void setResult(int resultCode);
	/**
	 * Call this to set the result that your fragment will return to its caller.
	 */
	public void setResult(int resultCode, Request data);
	public void finish();
	public boolean isFinishing();
	/**
	 * Called when the fragment has detected the user's press of the back key.
	 * The default implementation simply finishes the current fragment, but you
	 * can override this to do whatever you want.
	 */
	public void onBackPressed();
	public Request getRequest();
	public void setRequest(Request newRequest);
	public void onSaveInstanceState(Bundle outState);
	public void onCreate(Bundle savedInstanceState);
	public void onViewCreated(View view, Bundle savedInstanceState);
	public void onActivityCreated(Bundle savedInstanceState);
	public void onStart();
	public void onResume();
	/**
	 * Whether the state have been saved by system.
	 */
	public boolean hasStateSaved();
	public void setSoftInputMode(int mode);
	public int getSoftInputMode();
	public void setUserVisibleHint(boolean isVisibleToUser);
	public void invalidateWindowConfiguration();
	public boolean isUserActive();
	/**
	 * Called when user has come to this fragment.
	 */
	public void onUserActive();
	/**
	 * Called when user has left this fragment.
	 */
	public void onUserLeave();
	public void setSlideEnable(boolean enable);
	// ------------------------------------------------------------------------
	// Dispatch events
	// ------------------------------------------------------------------------

	public boolean dispatchKeyEvent(KeyEvent event);
	public boolean dispatchKeyShortcutEvent(KeyEvent event);
	public boolean dispatchTouchEvent(MotionEvent ev);
	public boolean dispatchTrackballEvent(MotionEvent ev);
	public boolean dispatchGenericMotionEvent(MotionEvent ev);

	// ------------------------------------------------------------------------
	// Handle events
	// ------------------------------------------------------------------------

	public boolean onTouchEvent(MotionEvent ev);
	public boolean onKeyDown(int keyCode, KeyEvent event);
	public boolean onKeyUp(int keyCode, KeyEvent event);
	public boolean onKeyLongPress(int keyCode, KeyEvent event);
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event);
	public boolean onKeyShortcut(int keyCode, KeyEvent event);
	public boolean onTrackballEvent(MotionEvent event);
	public boolean onGenericMotionEvent(MotionEvent event);
}
