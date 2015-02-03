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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.fragmentmaster.animator.PageAnimator;

public class MasterListFragment extends ListFragment implements IMasterFragment {

    private MasterFragmentDelegate mImpl = new MasterFragmentDelegate(this);

    public MasterListFragment() {
    }

    // ------------------------------------------------------------------------
    // Lifecycle
    // ------------------------------------------------------------------------

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mImpl.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImpl.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImpl.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImpl.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mImpl.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mImpl.onResume();
    }

    @Override
    public void onActivate() {
    }

    @Override
    public void onDeactivate() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mImpl.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mImpl.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mImpl.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImpl.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mImpl.onDetach();
    }

    // ------------------------------------------------------------------------
    // MasterFragment features
    // ------------------------------------------------------------------------

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public FragmentMaster getFragmentMaster() {
        return mImpl.getFragmentMaster();
    }

    @Override
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return mImpl.getLayoutInflater();
    }

    @Override
    public void startFragment(Class<? extends IMasterFragment> clazz) {
        mImpl.startFragment(clazz);
    }

    @Override
    public void startFragment(Request request) {
        mImpl.startFragment(request);
    }

    @Override
    public void startFragmentForResult(Class<? extends IMasterFragment> clazz,
                                       int requestCode) {
        mImpl.startFragmentForResult(clazz, requestCode);
    }

    @Override
    public void startFragmentForResult(Request request, int requestCode) {
        mImpl.startFragmentForResult(request, requestCode);
    }

    @Override
    public void startFragmentFromChild(IMasterFragment childFragment,
                                       Request request, int requestCode) {
        mImpl.startFragmentFromChild(childFragment, request, requestCode);
    }

    @Override
    public final void setResult(int resultCode) {
        mImpl.setResult(resultCode);
    }

    @Override
    public final void setResult(int resultCode, Request data) {
        mImpl.setResult(resultCode, data);
    }

    @Override
    public void finish() {
        mImpl.finish();
    }

    @Override
    public boolean isFinishing() {
        return mImpl.isFinishing();
    }

    @Override
    public Request getRequest() {
        return mImpl.getRequest();
    }

    @Override
    public void setRequest(Request newRequest) {
        mImpl.setRequest(newRequest);
    }

    @Override
    public boolean hasStateSaved() {
        return mImpl.hasStateSaved();
    }

    @Override
    public void setSoftInputMode(int mode) {
        mImpl.setSoftInputMode(mode);
    }

    @Override
    public int getSoftInputMode() {
        return mImpl.getSoftInputMode();
    }

    @Override
    public void setPrimary(boolean isPrimary) {
        mImpl.setPrimary(isPrimary);
    }

    @Override
    public boolean isActive() {
        return mImpl.isUserActive();
    }

    @Override
    public boolean isPrimary() {
        return mImpl.isPrimary();
    }

    @Override
    public void setSlideable(boolean slideable) {
        mImpl.setSlideable(slideable);
    }

    @Override
    public boolean isSlideable() {
        return mImpl.isSlideable();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Request data) {
    }

    @Override
    public IMasterFragment getTargetChildFragment() {
        return mImpl.getTargetChildFragment();
    }

    @Override
    public void setTargetChildFragment(IMasterFragment targetChildFragment) {
        mImpl.setTargetChildFragment(targetChildFragment);
    }

    @Override
    public void onBackPressed() {
        mImpl.onBackPressed();
    }

    // ------------------------------------------------------------------------
    // Dispatch events
    // ------------------------------------------------------------------------

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mImpl.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return mImpl.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mImpl.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        return mImpl.dispatchTrackballEvent(ev);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return mImpl.dispatchGenericMotionEvent(ev);
    }

    // ------------------------------------------------------------------------
    // Handle events
    // ------------------------------------------------------------------------

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mImpl.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mImpl.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return mImpl.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return mImpl.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mImpl.onTouchEvent(ev);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return mImpl.onKeyShortcut(keyCode, event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return mImpl.onTrackballEvent(event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mImpl.onGenericMotionEvent(event);
    }

    @Override
    public PageAnimator onCreatePageAnimator() {
        return mImpl.onCreatePageAnimator();
    }
}
