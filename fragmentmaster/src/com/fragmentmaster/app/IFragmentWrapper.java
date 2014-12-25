package com.fragmentmaster.app;

import android.app.Activity;
import android.view.View;

/**
 * Define fragment's methods that used by FragmentMaster.
 *
 * @param <Fragment> {@link android.app.Fragment} Or {@link android.support.v4.app.Fragment}
 */
interface IFragmentWrapper<Fragment> {

    public Fragment getFragment();

    public Activity getActivity();

    public Fragment getParentFragment();

    public void setTargetFragment(Fragment fragment, int requestCode);

    public Fragment getTargetFragment();

    public int getTargetRequestCode();

    public void setMenuVisibility(boolean isPrimary);

    public void setUserVisibleHint(boolean isPrimary);

    public boolean isResumed();

    public View getView();
}
