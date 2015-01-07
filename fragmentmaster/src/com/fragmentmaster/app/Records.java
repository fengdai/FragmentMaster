/*
 * Copyright 2014 Feng Dai
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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Records {

    private static final String TAG = "Records";

    private ArrayList<IMasterFragment> mFragments = new ArrayList<IMasterFragment>();

    public void add(IMasterFragment fragment, IMasterFragment target, int requestCode) {
        fragment.setTargetFragment(
                target == null ? null : target.getFragment(), requestCode);
        mFragments.add(fragment);
    }

    public void remove(IMasterFragment fragment) {
        int index = mFragments.indexOf(fragment);
        mFragments.remove(index);

        IMasterFragment f;
        for (int i = index; i < mFragments.size(); i++) {
            f = mFragments.get(i);
            IMasterFragment target = (IMasterFragment) f.getTargetFragment();
            if (target == fragment) {
                f.setTargetFragment(null, -1);
            }
        }
    }

    public int size() {
        return mFragments.size();
    }

    public int indexOf(IMasterFragment fragment) {
        return mFragments.indexOf(fragment);
    }

    public boolean has(IMasterFragment fragment) {
        return mFragments.contains(fragment);
    }

    public List<IMasterFragment> getFragments() {
        return Collections.unmodifiableList(mFragments);
    }

    public Bundle save(FragmentManager fragmentManager) {
        Bundle fragments = null;
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment f = mFragments.get(i).getFragment();
            if (f != null) {
                if (fragments == null) {
                    fragments = new Bundle();
                }
                String key = "f" + i;
                fragmentManager.putFragment(fragments, key, f);
            }
        }
        return fragments;
    }

    public void restore(FragmentManager fragmentManager, Bundle fragments) {
        mFragments.clear();
        if (fragments != null) {
            Iterable<String> keys = fragments.keySet();
            for (String key : keys) {
                if (key.startsWith("f")) {
                    int index = Integer.parseInt(key.substring(1));
                    IMasterFragment f = (IMasterFragment) fragmentManager
                            .getFragment(fragments, key);
                    if (f != null) {
                        while (mFragments.size() <= index) {
                            mFragments.add(null);
                        }
                        f.setMenuVisibility(false);
                        mFragments.set(index, f);
                    } else {
                        Log.w(TAG, "Bad fragment at key " + key);
                    }
                }
            }
        }
    }
}
