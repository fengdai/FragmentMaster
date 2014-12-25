package com.fragmentmaster.app.ware;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fragmentmaster.app.MasterActivity;

public abstract class FragmentManagerWare<Fragment, FragmentManager> {

    protected FragmentManager mFragmentManager;

    public FragmentManagerWare(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    abstract public Fragment newFragment(Context context, String className);

    abstract public void addFragment(int containerId, Fragment fragment);

    abstract public void removeFragment(Fragment fragment);

    abstract public void putFragment(Bundle bundle, String key, Fragment fragment);

    abstract public Fragment getFragment(Bundle bundle, String key);

    public static class FragmentNotFoundException extends RuntimeException {
        public FragmentNotFoundException() {
        }

        public FragmentNotFoundException(String name) {
            super(name);
        }
    }

    @SuppressLint("NewApi")
    public static FragmentManagerWare createFragmentManagerWare(MasterActivity activity, boolean supportVersion) {
        if (supportVersion) {
            return new FragmentManagerWare_Support(activity.getSupportFragmentManager());
        }
        return new FragmentManagerWare_HC(activity.getFragmentManager());
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    public static <T> FragmentManagerWare createChildFragmentManagerWare(T fragment) {
        if (fragment instanceof android.support.v4.app.Fragment) {
            return new FragmentManagerWare_Support(((android.support.v4.app.Fragment) fragment).getChildFragmentManager());
        }
        if (fragment instanceof android.app.Fragment) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ((android.app.Fragment) fragment).getChildFragmentManager();
            } else {
                return null;
            }
        }
        throw new RuntimeException("Can't create FragmentManagerWare from: " + fragment);
    }


}