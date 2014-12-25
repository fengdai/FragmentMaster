package com.fragmentmaster.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.fragmentmaster.app.ware.FragmentManagerWare;
import com.fragmentmaster.internal.FragmentMasterImpl;

/**
 * Host activity of MasterFragment.
 */
public abstract class MasterActivity extends ActionBarActivity {

    /**
     * Persistence key for FragmentMaster
     */
    private static final String FRAGMENTS_TAG = "FragmentMaster:fragments";

    private FragmentMaster mFragmentMaster = new FragmentMasterImpl(this, FragmentManagerWare.createFragmentManagerWare(this, true));

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

    public FragmentMaster getSupportFragmentMaster() {
        return mFragmentMaster;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mFragmentMaster.dispatchKeyEvent(event);
    }

    public boolean superDispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return mFragmentMaster.dispatchKeyShortcutEvent(event);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean superDispatchKeyShortcutEvent(KeyEvent event) {
        return super.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mFragmentMaster.dispatchTouchEvent(ev);
    }

    public boolean superDispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return mFragmentMaster.dispatchGenericMotionEvent(ev);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public boolean superDispatchGenericMotionEvent(MotionEvent ev) {
        return super.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        return mFragmentMaster.dispatchTrackballEvent(ev);
    }

    public boolean superDispatchTrackballEvent(MotionEvent ev) {
        return super.dispatchTrackballEvent(ev);
    }
}
