/*
 * Copyright 2014 Feng Dai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
