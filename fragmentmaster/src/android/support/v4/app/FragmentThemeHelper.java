package android.support.v4.app;

import com.fragmentmaster.R;

import android.app.Activity;
import android.content.res.TypedArray;
import android.view.ContextThemeWrapper;

public class FragmentThemeHelper {

    public static ContextThemeWrapper createContextThemeWrapper(Activity activity) {
        TypedArray a = activity.obtainStyledAttributes(null,
                R.styleable.FragmentMaster, android.R.attr.theme, 0);
        int masterFragmentTheme = a.getResourceId(R.styleable.FragmentMaster_masterFragmentTheme,
                -1);
        a.recycle();
        if (masterFragmentTheme == -1) {
            return activity;
        }
        return new ContextThemeWrapper(activity, masterFragmentTheme);
    }
}