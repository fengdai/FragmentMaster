package com.fragmentmaster.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Host activity of MasterFragment.
 */
public abstract class MasterActivity extends ActionBarActivity {

    /**
     * Persistence key for FragmentMaster
     */
    private static final String FRAGMENTS_TAG = "FragmentMaster:fragments";

    private FragmentMaster mFragmentMaster = new FragmentMasterImpl(this);

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            Parcelable p = bundle.getParcelable(FRAGMENTS_TAG);
            mFragmentMaster.restoreAllState(p);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable p = mFragmentMaster.saveAllState();
        if (p != null) {
            outState.putParcelable(FRAGMENTS_TAG, p);
        }
    }

    public FragmentMaster getFragmentMaster() {
        return mFragmentMaster;
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        return mFragmentMaster.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(@NonNull KeyEvent event) {
        return mFragmentMaster.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        return mFragmentMaster.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return mFragmentMaster.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        return mFragmentMaster.dispatchTrackballEvent(ev);
    }
}
