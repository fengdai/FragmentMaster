package com.fragmentmaster.app.ware;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentManagerWare_HC extends FragmentManagerWare<Fragment, FragmentManager> {
    public FragmentManagerWare_HC(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
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
