package com.fragmentmaster.app.event;

import android.app.Activity;
import android.support.v4.view.KeyEventCompat2;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class ActivityEventDispatcher implements EventDispatcher {

    public Activity mActivity;

    public ActivityEventDispatcher(Activity activity) {
        mActivity = activity;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        final View decor = mActivity.getWindow().getDecorView();
        return KeyEventCompat2.dispatch(event, mActivity, decor != null
                ? KeyEventCompat2.getKeyDispatcherState(decor)
                : null, mActivity);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return mActivity.onKeyShortcut(event.getKeyCode(), event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return mActivity.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
        return mActivity.onTrackballEvent(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return mActivity.onGenericMotionEvent(event);
    }
}
