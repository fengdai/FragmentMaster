/*
 * Copyright 2015 Feng Dai
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
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Host activity of MasterFragment.
 */
public abstract class MasterActionBarActivity extends ActionBarActivity implements IMasterActivity {

    private final MasterActivityDelegate mImpl = new MasterActivityDelegate(this);

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mImpl.onCreate(bundle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mImpl.onSaveInstanceState(outState);
    }

    public FragmentMaster getFragmentMaster() {
        return mImpl.getFragmentMaster();
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        return mImpl.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(@NonNull KeyEvent event) {
        return mImpl.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        return mImpl.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return mImpl.dispatchGenericMotionEvent(ev);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        return mImpl.dispatchTrackballEvent(ev);
    }
}
