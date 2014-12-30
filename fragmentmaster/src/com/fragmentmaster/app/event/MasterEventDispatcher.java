package com.fragmentmaster.app.event;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Event dispatch order:
 * <p/>
 * 1. Activity's Window <br/>
 * 2. Interceptor (If there is a Interceptor) <br/>
 * 3. Activity <br/>
 */
public class MasterEventDispatcher implements EventDispatcher {

    private Activity mActivity;
    private EventDispatcher mEventInterceptor;
    private WindowEventDispatcher mWindowEventDispatcher;
    private ActivityEventDispatcher mActivityEventDispatcher;

    public MasterEventDispatcher(Activity activity) {
        mActivity = activity;
        mWindowEventDispatcher = new WindowEventDispatcher(activity);
        mActivityEventDispatcher = new ActivityEventDispatcher(activity);
    }

    public void setInterceptor(EventDispatcher eventHandler) {
        this.mEventInterceptor = eventHandler;
    }

    public EventDispatcher getInterceptor() {
        return mEventInterceptor;
    }

    public boolean hasInterceptor() {
        return mEventInterceptor != null;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        mActivity.onUserInteraction();
        return mWindowEventDispatcher.dispatchKeyEvent(event)
                || (hasInterceptor() && mEventInterceptor.dispatchKeyEvent(event))
                || mActivityEventDispatcher.dispatchKeyEvent(event);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        mActivity.onUserInteraction();
        return mWindowEventDispatcher.dispatchKeyShortcutEvent(event)
                || (hasInterceptor() && mEventInterceptor.dispatchKeyShortcutEvent(event))
                || mActivityEventDispatcher.dispatchKeyShortcutEvent(event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mActivity.onUserInteraction();
        }
        return mWindowEventDispatcher.dispatchTouchEvent(ev)
                || (hasInterceptor() && mEventInterceptor.dispatchTouchEvent(ev))
                || mActivityEventDispatcher.dispatchTouchEvent(ev);
    }

    public boolean dispatchTrackballEvent(MotionEvent ev) {
        mActivity.onUserInteraction();
        return mWindowEventDispatcher.dispatchTrackballEvent(ev)
                || (hasInterceptor() && mEventInterceptor.dispatchTrackballEvent(ev))
                || mActivityEventDispatcher.dispatchTrackballEvent(ev);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        mActivity.onUserInteraction();
        return mWindowEventDispatcher.dispatchGenericMotionEvent(ev)
                || (hasInterceptor() && mEventInterceptor.dispatchGenericMotionEvent(ev))
                || mActivityEventDispatcher.dispatchGenericMotionEvent(ev);
    }

}
