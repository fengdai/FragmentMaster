package com.fragmentmaster.app;

import android.app.Activity;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;

import com.fragmentmaster.R;
import com.fragmentmaster.annotation.Configuration;

public class FragmentThemeHelper {

    public static ContextThemeWrapper createContextThemeWrapper(Activity activity, IMasterFragment fragment) {
        int masterFragmentTheme = getMasterFragmentTheme(activity, fragment);
        if (masterFragmentTheme == -1) {
            return activity;
        }
        return new ContextThemeWrapper(activity, masterFragmentTheme);
    }

    public static int getMasterFragmentTheme(Activity activity, IMasterFragment fragment) {
        int masterFragmentTheme = -1;
        Class clazz = fragment.getClass();
        // Get theme from Configuration annotation.
        if (clazz.isAnnotationPresent(Configuration.class)) {
            Configuration configuration = (Configuration) clazz.getAnnotation(Configuration.class);
            masterFragmentTheme = configuration.theme();
        }
        // Get theme from Theme attrs.
        if (masterFragmentTheme == -1) {
            TypedValue outValue = new TypedValue();
            activity.getTheme().resolveAttribute(R.attr.masterFragmentTheme, outValue, true);
            masterFragmentTheme = outValue.resourceId;
        }
        return masterFragmentTheme;
    }
}