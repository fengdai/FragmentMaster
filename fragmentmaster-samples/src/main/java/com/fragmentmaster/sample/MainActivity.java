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

package com.fragmentmaster.sample;

import android.os.Bundle;
import android.util.Log;

import com.fragmentmaster.app.FragmentMaster;
import com.fragmentmaster.app.IMasterFragment;
import com.fragmentmaster.app.MasterActivity;
import com.fragmentmaster.app.Request;

public class MainActivity extends MasterActivity {
    public static final String TAG = "MainActivity";

    private FragmentMaster.FragmentLifecycleCallbacks mLifecycleCallbacks =
            new FragmentMaster.SimpleFragmentLifecycleCallbacks() {
                @Override
                public void onFragmentResumed(IMasterFragment fragment) {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "[onResume]    " + fragment.toString());
                }

                @Override
                public void onFragmentUserActed(IMasterFragment fragment) {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "[onUserActive]" + fragment.toString());
                }

                @Override
                public void onFragmentUserLeft(IMasterFragment fragment) {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "[onUserLeft]  " + fragment.toString());
                }

                @Override
                public void onFragmentPaused(IMasterFragment fragment) {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "[onPaused]    " + fragment.toString());
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentMaster fragmentMaster = getFragmentMaster();
        fragmentMaster.registerFragmentLifecycleCallbacks(mLifecycleCallbacks);
        fragmentMaster.install(R.id.container, new Request(Home.class), true);
    }
}
