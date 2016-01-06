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

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * Define native fragment's methods that used by FragmentMaster.
 */
interface IFragmentWrapper {

    Fragment getFragment();

    Activity getActivity();

    Fragment getParentFragment();

    FragmentManager getChildFragmentManager();

    void setTargetFragment(Fragment target, int requestCode);

    Fragment getTargetFragment();

    int getTargetRequestCode();

    void setMenuVisibility(boolean isPrimary);

    void setUserVisibleHint(boolean isPrimary);

    boolean isResumed();

    View getView();
}
