package com.fragmentmaster.app.ware;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class FragmentManagerWare_Support extends FragmentManagerWare<Fragment, FragmentManager> {

    public FragmentManagerWare_Support(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public Fragment newFragment(Context context, String className) {
        try {
            return (Fragment.instantiate(context,
                    className, new Bundle()));
        } catch (Exception e) {
            throw new FragmentNotFoundException("No fragment found : { className="
                    + className + " }");
        }
    }

    @Override
    public void addFragment(int containerId, Fragment fragment) {
        mFragmentManager.beginTransaction()
                .add(containerId, fragment)
                .commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
    }

    @Override
    public void removeFragment(Fragment fragment) {
        mFragmentManager.beginTransaction().remove(fragment)
                .commit();
        mFragmentManager.executePendingTransactions();
    }

    @Override
    public void putFragment(Bundle bundle, String key, Fragment fragment) {
        mFragmentManager.putFragment(bundle, key, fragment);
    }

    @Override
    public Fragment getFragment(Bundle bundle, String key) {
        return mFragmentManager.getFragment(bundle, key);
    }
}
