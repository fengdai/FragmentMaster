package com.fragmentmaster.app.event;

import android.view.KeyEvent;
import android.view.MotionEvent;

public interface EventDispatcher {

    public boolean dispatchKeyEvent(KeyEvent event);

    public boolean dispatchKeyShortcutEvent(KeyEvent event);

    public boolean dispatchTouchEvent(MotionEvent event);

    public boolean dispatchTrackballEvent(MotionEvent event);

    public boolean dispatchGenericMotionEvent(MotionEvent event);
}
