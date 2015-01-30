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

package com.fragmentmaster.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fragmentmaster.app.MasterListFragment;
import com.fragmentmaster.app.Request;
import com.fragmentmaster.sample.entry.Entry;

import java.util.ArrayList;
import java.util.List;

public class Home extends MasterListFragment {
    private static final List<Entry> ENTRIES = new ArrayList<Entry>();

    static {
        ENTRIES.add(new Entry("Pass Data", new Request(PassData.class)));
        ENTRIES.add(new Entry("Receive Result",
                new Request(ReceiveResult.class)));
        ENTRIES.add(new Entry("PageAnimators", new Request(PageAnimators.class)));
        ENTRIES.add(new Entry("Themes", new Request(Themes.class)));
        ENTRIES.add(new Entry("Slideable", new Request(Slideable.class)));
        ENTRIES.add(new Entry("Soft Input", new Request(SoftInput.class)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setListAdapter(new ArrayAdapter<Entry>(getActivity(),
                android.R.layout.simple_list_item_1, ENTRIES));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        startFragment(ENTRIES.get(position).mRequest);
    }

    @Override
    public String toString() {
        return "Home";
    }
}
