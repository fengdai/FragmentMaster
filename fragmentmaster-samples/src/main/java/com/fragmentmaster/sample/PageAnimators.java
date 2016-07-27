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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fragmentmaster.animator.PageAnimator;
import com.fragmentmaster.annotation.Configuration;
import com.fragmentmaster.app.MasterFragment;
import com.fragmentmaster.app.MasterListFragment;
import com.fragmentmaster.app.Request;
import com.fragmentmaster.sample.entry.Entry;
import com.fragmentmaster.sample.pageanimator.Animators;

import java.util.ArrayList;
import java.util.List;

public class PageAnimators extends MasterListFragment {

    private static final List<Entry> ENTRIES = new ArrayList<Entry>();

    static {
        ENTRIES.add(new Entry("StackAnimator", new Request(StackPage.class)));
        ENTRIES.add(new Entry("EnterOvershootAnimator", new Request(
                EnterOvershootPage.class)));
        ENTRIES.add(new Entry("VerticalSlideAnimator", new Request(
                VerticalSlidePage.class)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<Entry>(getActivity(),
                android.R.layout.simple_list_item_1, ENTRIES));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Entry page = ENTRIES.get(position);
        startFragment(page.mRequest);
    }

    @Configuration(theme = R.style.AppTheme_MasterFragment_Transparent)
    public static class VerticalSlidePage extends MasterFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.vertical_slide_animator_fragment,
                    container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            // As this fragment performs vertical slide animation. Don't let
            // this kind of fragment to be horizontally slideable;
            allowSwipeBack(false);
        }

        @Override
        public PageAnimator onCreatePageAnimator() {
            return Animators.VERTICAL_SLIDE_ANIMATOR;
        }
    }

    public static class EnterOvershootPage extends MasterFragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.overshoot_slide_animator_fragment,
                    container, false);
        }

        @Override
        public PageAnimator onCreatePageAnimator() {
            return Animators.ENTER_OVER_SHOOT_ANIMATOR;
        }
    }

    public static class StackPage extends MasterFragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.overshoot_slide_animator_fragment,
                    container, false);
        }

        @Override
        public PageAnimator onCreatePageAnimator() {
            return Animators.STACK_ANIMATOR;
        }
    }
}
