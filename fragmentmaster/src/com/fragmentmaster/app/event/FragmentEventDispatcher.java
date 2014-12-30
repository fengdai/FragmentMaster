package com.fragmentmaster.app.event;

import android.support.v4.view.KeyEventCompat2;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.fragmentmaster.app.IMasterFragment;

public class FragmentEventDispatcher implements EventDispatcher {

    private IMasterFragment mMasterFragment;

    public FragmentEventDispatcher(IMasterFragment masterFragment) {
        mMasterFragment = masterFragment;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        View view = mMasterFragment.getView();
        return KeyEventCompat2.dispatch(event, mMasterFragment,
                view != null
                        ? KeyEventCompat2.getKeyDispatcherState(view)
                        : null, this);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return mMasterFragment.onKeyShortcut(event.getKeyCode(), event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return mMasterFragment.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
        return mMasterFragment.onTrackballEvent(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return mMasterFragment.onGenericMotionEvent(event);
    }
}
