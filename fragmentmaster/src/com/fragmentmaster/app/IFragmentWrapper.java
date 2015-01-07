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

    public Fragment getFragment();

    public Activity getActivity();

    public Fragment getParentFragment();

    public FragmentManager getChildFragmentManager();

    public void setTargetFragment(Fragment target, int requestCode);

    public Fragment getTargetFragment();

    public int getTargetRequestCode();

    public void setMenuVisibility(boolean isPrimary);

    public void setUserVisibleHint(boolean isPrimary);

    public boolean isResumed();

    public View getView();
}
