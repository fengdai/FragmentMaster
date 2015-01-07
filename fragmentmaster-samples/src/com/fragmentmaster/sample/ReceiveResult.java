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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fragmentmaster.app.MasterFragment;
import com.fragmentmaster.app.MasterListFragment;
import com.fragmentmaster.app.Request;

public class ReceiveResult extends MasterFragment {
    private static final int REQUEST_CODE = 0;
    private TextView mResultView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.receive_result_fragment,
                container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mResultView = (TextView) view.findViewById(R.id.resultView);
        view.findViewById(R.id.button).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startFragmentForResult(NumbersList.class, REQUEST_CODE);
                    }
                });
        FragmentManager fragmentManager = getChildFragmentManager();
        if (fragmentManager.findFragmentByTag("TAG_CHILD") == null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.childContainer, new Child(), "TAG_CHILD");
            ft.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Request data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mResultView.setText(data
                    .getStringExtra(NumbersList.EXTRA_KEY_RESULT));
        } else {
            mResultView.setText("[Canceled]");
        }
    }

    /**
     * Child fragment
     */
    public static class Child extends MasterFragment {
        private static final int REQUEST_CODE = 0;

        private TextView mResultView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(
                    R.layout.receive_result_child_fragment, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mResultView = (TextView) view.findViewById(R.id.resultView);
            view.findViewById(R.id.button).setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startFragmentForResult(NumbersList.class,
                                    REQUEST_CODE);
                        }
                    });
        }

        @Override
        public void onFragmentResult(int requestCode, int resultCode,
                                     Request data) {
            super.onFragmentResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                mResultView.setText(data
                        .getStringExtra(NumbersList.EXTRA_KEY_RESULT));
            } else {
                mResultView.setText("[Canceled]");
            }
        }
    }

    /**
     * Numbers list
     */
    public static class NumbersList extends MasterListFragment {

        public static final String EXTRA_KEY_RESULT = "result";

        private String[] mNumbers;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.list_fragment, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mNumbers = getResources().getStringArray(R.array.numbers);
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, mNumbers));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            // deliver result
            Request result = new Request();
            result.putExtra(EXTRA_KEY_RESULT, mNumbers[position]);
            setResult(RESULT_OK, result);
            finish();
        }
    }
}
