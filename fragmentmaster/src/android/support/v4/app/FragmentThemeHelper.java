package android.support.v4.app;

import android.app.Activity;
import android.content.res.TypedArray;
import android.view.ContextThemeWrapper;

import com.fragmentmaster.R;
import com.fragmentmaster.annotation.Configuration;

public class FragmentThemeHelper {

    public static ContextThemeWrapper createContextThemeWrapper(Activity activity, Fragment fragment) {
        int masterFragmentTheme = getMasterFragmentTheme(activity, fragment);
        if (masterFragmentTheme == -1) {
            return activity;
        }
        return new ContextThemeWrapper(activity, masterFragmentTheme);
    }

    public static int getMasterFragmentTheme(Activity activity, Fragment fragment) {
        int masterFragmentTheme = -1;
        Class clazz = ((Object) fragment).getClass();
        // Get theme from Configuration annotation.
        if (clazz.isAnnotationPresent(Configuration.class)) {
            Configuration configuration = (Configuration) clazz.getAnnotation(Configuration.class);
            masterFragmentTheme = configuration.theme();
        }
        // Get theme from Theme attrs.
        if (masterFragmentTheme == -1) {
            TypedArray a = activity.obtainStyledAttributes(null,
                    R.styleable.FragmentMaster, android.R.attr.theme, 0);
            masterFragmentTheme = a.getResourceId(R.styleable.FragmentMaster_masterFragmentTheme,
                    -1);
            a.recycle();
        }
        return masterFragmentTheme;
    }
}