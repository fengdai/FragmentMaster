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
