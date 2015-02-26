/*
 * Copyright 2015 Feng Dai
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
import android.util.TypedValue;
import android.view.ContextThemeWrapper;

import com.fragmentmaster.R;
import com.fragmentmaster.annotation.Configuration;

public class FragmentContext extends ContextThemeWrapper {

    FragmentContext(IMasterFragment fragment) {
        super(fragment.getActivity(), getMasterFragmentThemeRes(fragment.getActivity(), fragment));
    }

    private static int getMasterFragmentThemeRes(Activity context, IMasterFragment fragment) {
        int themeRes = -1;
        Class clazz = fragment.getClass();
        // Get theme from Configuration annotation.
        if (clazz.isAnnotationPresent(Configuration.class)) {
            Configuration configuration = (Configuration) clazz.getAnnotation(Configuration.class);
            themeRes = configuration.theme();
        }
        // Get theme from Theme attrs.
        if (themeRes == -1) {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.masterFragmentTheme, outValue, true);
            themeRes = outValue.resourceId;
        }
        return themeRes;
    }
}
