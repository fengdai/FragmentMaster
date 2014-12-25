package com.fragmentmaster.util;

import android.os.Build;

public class FragmentUtil {
    private FragmentUtil() {
    }

    public static <Fragment> boolean isChildFragmentSupported(Fragment fragment) {
        if (fragment instanceof android.support.v4.app.Fragment) {
            return true;
        }
        if (fragment instanceof android.app.Fragment) {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
        }
        return false;
    }
}
