package com.fragmentmaster.app.event;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.fragmentmaster.app.FragmentMaster;

public class EventDispatcher implements FragmentMaster.Callback {

    private Activity mActivity;
    private FragmentMaster.Callback mCallback;
    private WindowEventHandler mWindowEventHandler;
    private ActivityEventHandler mActivityEventHandler;

    public EventDispatcher(Activity activity) {
        mActivity = activity;
        mWindowEventHandler = new WindowEventHandler(activity);
        mActivityEventHandler = new ActivityEventHandler(activity);
    }

    public void setCallback(FragmentMaster.Callback callback) {
        this.mCallback = callback;
    }

    public FragmentMaster.Callback getCallback() {
        return mCallback;
    }

    public boolean hasCallback() {
        return mCallback != null;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        mActivity.onUserInteraction();

        if (mWindowEventHandler.dispatchKeyEvent(event)) {
            return true;
        }

        if (hasCallback() && mCallback.dispatchKeyEvent(event)) {
            return true;
        }
        return mActivityEventHandler.dispatchKeyEvent(event);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        mActivity.onUserInteraction();

        if (mWindowEventHandler.dispatchKeyShortcutEvent(event)) {
            return true;
        }
        if (hasCallback() && mCallback.dispatchKeyShortcutEvent(event)) {
            return true;
        }
        return mActivityEventHandler.dispatchKeyShortcutEvent(event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mActivity.onUserInteraction();
        }

        if (mWindowEventHandler.dispatchTouchEvent(ev)) {
            return true;
        }
        if (hasCallback() && mCallback.dispatchTouchEvent(ev)) {
            return true;
        }
        return mActivityEventHandler.dispatchTouchEvent(ev);
    }

    public boolean dispatchTrackballEvent(MotionEvent ev) {
        mActivity.onUserInteraction();

        if (mWindowEventHandler.dispatchTrackballEvent(ev)) {
            return true;
        }
        if (hasCallback() && mCallback.dispatchTrackballEvent(ev)) {
            return true;
        }
        return mActivityEventHandler.dispatchTrackballEvent(ev);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        mActivity.onUserInteraction();

        if (mWindowEventHandler.dispatchGenericMotionEvent(ev)) {
            return true;
        }
        if (hasCallback() && mCallback.dispatchGenericMotionEvent(ev)) {
            return true;
        }
        return mActivityEventHandler.dispatchGenericMotionEvent(ev);
    }
}
