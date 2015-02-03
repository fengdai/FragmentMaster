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
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentmaster.BuildConfig;
import com.fragmentmaster.animator.PageAnimator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class FragmentMaster {

    private static final String TAG = "FragmentMaster";

    // The host activity.
    private final FragmentActivity mActivity;

    private final FragmentManager mFragmentManager;

    private int mContainerResID = 0;

    private ViewGroup mContainer;

    private boolean mIsSlideable = false;

    private boolean mIsInstalled = false;

    private boolean mSticky = false;

    private boolean mHomeFragmentApplied = false;

    private PageAnimator mPageAnimator = null;

    private IMasterFragment mPrimaryFragment = null;

    // Use to record Fragments started by FragmentMaster.
    private final Records mRecords = new Records();

    private final HashSet<IMasterFragment> mFinishPendingFragments = new HashSet<IMasterFragment>();

    // Event dispatcher
    private final MasterEventDispatcher mEventDispatcher;

    private final ArrayList<FragmentLifecycleCallbacks> mFragmentLifecycleCallbackses =
            new ArrayList<FragmentLifecycleCallbacks>();

    FragmentMaster(FragmentActivity activity) {
        mActivity = activity;
        mFragmentManager = activity.getSupportFragmentManager();
        mEventDispatcher = new MasterEventDispatcher(activity);
    }

    public FragmentActivity getActivity() {
        return mActivity;
    }

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public int getContainerResId() {
        return mContainerResID;
    }

    protected int getFragmentContainerId() {
        return getContainerResId();
    }

    public final void startFragmentForResult(IMasterFragment target,
                                             Request request, int requestCode) {
        ensureInstalled();

        IMasterFragment fragment = newFragment(request.getClassName());
        fragment.setRequest((Request) request.clone());
        mFragmentManager.beginTransaction()
                .add(getFragmentContainerId(), fragment.getFragment())
                .commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
        mRecords.add(fragment, target, requestCode);
        fragment.setPrimary(false);
        setUpAnimator(fragment);
        onFragmentStarted(fragment);
    }

    protected void setUpAnimator(IMasterFragment fragment) {
        this.setPageAnimator(fragment != null ? fragment.onCreatePageAnimator() : null);
    }

    protected abstract void onFragmentStarted(IMasterFragment fragment);

    private IMasterFragment newFragment(String className) {
        try {
            return (IMasterFragment) Fragment.instantiate(getActivity(),
                    className, new Bundle());
        } catch (Exception e) {
            throw new RuntimeException("No fragment found : { className="
                    + className + " }");
        }
    }

    public final void finishFragment(IMasterFragment fragment, int resultCode,
                                     Request data) {
        ensureInstalled();
        throwIfNotInFragmentMaster(fragment);
        if (!isFinishPending(fragment)) {
            mFinishPendingFragments.add(fragment);
        }
        onFinishFragment(fragment, resultCode, data);
    }

    /**
     * Check whether the specific fragment is in FragmentMaster.
     * <p/>
     * If a fragment is not in FragmentMaster, it may not be started by
     * FragmentMaster or has been finished already.
     *
     * @param fragment The fragment to check.
     * @return If the fragment is in FragmentMaster, returns true; else returns
     * false.
     */
    public boolean isInFragmentMaster(IMasterFragment fragment) {
        return mRecords.has(fragment);
    }

    /**
     * Check whether the specific fragment is pending to be finished.
     *
     * @param fragment The fragment to check.
     * @return If the fragment is pending to be finished, returns true; else
     * returns false.
     */
    public boolean isFinishPending(IMasterFragment fragment) {
        return mFinishPendingFragments.contains(fragment);
    }

    private void throwIfNotInFragmentMaster(IMasterFragment fragment) {
        if (!isInFragmentMaster(fragment)) {
            throw new IllegalStateException("Fragment {" + fragment
                    + "} not currently in FragmentMaster.");
        }
    }

    protected void onFinishFragment(IMasterFragment fragment, int resultCode,
                                    Request data) {
        doFinishFragment(fragment);
        deliverFragmentResult(fragment, resultCode, data);
    }

    protected final void doFinishFragment(IMasterFragment fragment) {
        if (mRecords.indexOf(fragment) == 0 && mSticky) {
            mActivity.finish();
            return;
        }

        mFragmentManager.beginTransaction().remove(fragment.getFragment())
                .commit();
        mFragmentManager.executePendingTransactions();
        mRecords.remove(fragment);
        mFinishPendingFragments.remove(fragment);
        onFragmentFinished(fragment);
    }

    protected void deliverFragmentResult(IMasterFragment fragment,
                                         int resultCode, Request data) {
        Fragment targetFragment = fragment.getTargetFragment();
        int requestCode = fragment.getTargetRequestCode();
        if (requestCode != -1 && targetFragment instanceof IMasterFragment) {
            dispatchFragmentResult((IMasterFragment) targetFragment,
                    fragment.getTargetRequestCode(), resultCode, data);
        }
    }

    private void dispatchFragmentResult(IMasterFragment who, int requestCode,
                                        int resultCode, Request data) {
        if (who.isFinishing()) {
            return;
        }
        if (who.getTargetChildFragment() == null) {
            who.onFragmentResult(requestCode, resultCode, data);
        } else {
            dispatchFragmentResult(who.getTargetChildFragment(), requestCode,
                    resultCode, data);
        }
        who.setTargetChildFragment(null);
    }

    private void ensureInstalled() {
        if (!isInstalled()) {
            throw new IllegalStateException("Haven't installed.");
        }
    }

    protected abstract void onFragmentFinished(IMasterFragment fragment);

    public IMasterFragment getPrimaryFragment() {
        return mPrimaryFragment;
    }

    protected final void setPrimaryFragment(IMasterFragment fragment) {
        if (fragment != mPrimaryFragment) {
            if (mPrimaryFragment != null) {
                mPrimaryFragment.setPrimary(false);
            }
            if (fragment != null) {
                fragment.setPrimary(true);
            }
            mPrimaryFragment = fragment;
            // Only the primary fragment can receive events.
            mEventDispatcher.setInterceptor(fragment);
        }
    }

    public List<IMasterFragment> getFragments() {
        return mRecords.getFragments();
    }

    protected void setPageAnimator(PageAnimator pageAnimator) {
        mPageAnimator = pageAnimator;
    }

    public PageAnimator getPageAnimator() {
        return mPageAnimator;
    }

    public boolean hasPageAnimator() {
        return mPageAnimator != null;
    }

    public final void install(int containerResID, Request homeRequest,
                              boolean sticky) {
        if (isInstalled()) {
            throw new IllegalStateException("Already installed!");
        } else {
            mContainerResID = containerResID;
            checkInstallProperties();
            performInstall(mContainer);
            mIsInstalled = true;

            if (homeRequest != null) {
                applyHomeFragment(homeRequest, sticky);
            }
        }
    }

    private void applyHomeFragment(Request homeRequest, boolean sticky) {
        mSticky = sticky;
        if (!mHomeFragmentApplied) {
            startFragmentForResult(null, homeRequest, -1);
            mHomeFragmentApplied = true;
        }
    }

    private void checkInstallProperties() {
        View container = mActivity.findViewById(mContainerResID);
        if (container == null) {
            throw new RuntimeException("No view found for id 0x"
                    + Integer.toHexString(mContainerResID));
        } else {
            mContainer = (ViewGroup) container;
        }
    }

    protected abstract void performInstall(ViewGroup container);

    public boolean isInstalled() {
        return mIsInstalled;
    }

    public final void setSlideable(boolean slideable) {
        mIsSlideable = slideable;
    }

    public boolean isSlideable() {
        return hasPageAnimator() && mIsSlideable;
    }

    Parcelable saveAllState() {
        FragmentMasterState state = new FragmentMasterState();
        state.mFragments = mRecords.save(mFragmentManager);
        state.mIsSlideable = mIsSlideable;
        state.mHomeFragmentApplied = mHomeFragmentApplied;

        logState();
        return state;
    }

    private void logState() {
        if (!BuildConfig.DEBUG) {
            return;
        }
        int fragmentsInManagerCount = 0;
        if (mFragmentManager.getFragments() != null) {
            for (Fragment f : mFragmentManager.getFragments()) {
                if (f != null) {
                    fragmentsInManagerCount++;
                }
            }
        }
        Log.d(TAG, "STATE FragmentMaster[" + mRecords.size()
                + "], FragmentManager[" + fragmentsInManagerCount
                + "], mIsSlideable[" + mIsSlideable
                + "], mHomeFragmentApplied[" + mHomeFragmentApplied + "]");
    }

    void restoreAllState(Parcelable state) {
        if (state != null) {
            FragmentMasterState fms = (FragmentMasterState) state;
            mRecords.restore(mFragmentManager, fms.mFragments);
            setSlideable(fms.mIsSlideable);
            mHomeFragmentApplied = fms.mHomeFragmentApplied;
        }
    }

    public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks callback) {
        synchronized (mFragmentLifecycleCallbackses) {
            mFragmentLifecycleCallbackses.add(callback);
        }
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks callback) {
        synchronized (mFragmentLifecycleCallbackses) {
            mFragmentLifecycleCallbackses.remove(callback);
        }
    }

    // ------------------------------------------------------------------------
    // Dispatch events
    // ------------------------------------------------------------------------

    boolean dispatchKeyEvent(KeyEvent event) {
        return mEventDispatcher.dispatchKeyEvent(event);
    }

    boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return mEventDispatcher.dispatchKeyShortcutEvent(event);
    }

    boolean dispatchTouchEvent(MotionEvent event) {
        return mEventDispatcher.dispatchTouchEvent(event);
    }

    boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return mEventDispatcher.dispatchGenericMotionEvent(ev);
    }

    boolean dispatchTrackballEvent(MotionEvent ev) {
        return mEventDispatcher.dispatchTrackballEvent(ev);
    }

    // ------------------------------------------------------------------------
    // Dispatch MasterFragment's lifecycle
    // ------------------------------------------------------------------------

    void dispatchFragmentAttached(IMasterFragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onFragmentAttached(fragment);
            }
        }
    }

    void dispatchFragmentCreated(IMasterFragment fragment, Bundle savedInstanceState) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onFragmentCreated(fragment, savedInstanceState);
            }
        }
    }

    void dispatchFragmentViewCreated(IMasterFragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onFragmentViewCreated(fragment);
            }
        }
    }

    void dispatchFragmentStarted(IMasterFragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onFragmentStarted(fragment);
            }
        }
    }

    void dispatchFragmentResumed(IMasterFragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onFragmentResumed(fragment);
            }
        }
    }

    void dispatchFragmentActivated(IMasterFragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onFragmentActivated(fragment);
            }
        }
    }

    void dispatchFragmentDeactivated(IMasterFragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onFragmentDeactivated(fragment);
            }
        }
    }

    void dispatchFragmentPaused(IMasterFragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onFragmentPaused(fragment);
            }
        }
    }

    void dispatchFragmentStopped(IMasterFragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onFragmentStopped(fragment);
            }
        }
    }

    void dispatchFragmentSaveInstanceState(IMasterFragment fragment, Bundle outState) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onFragmentSaveInstanceState(fragment, outState);
            }
        }
    }

    void dispatchFragmentDestroyed(IMasterFragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onFragmentDestroyed(fragment);
            }
        }
    }

    void dispatchFragmentDetached(IMasterFragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (Object callback : callbacks) {
                ((FragmentLifecycleCallbacks) callback).onFragmentDetached(fragment);
            }
        }
    }

    private Object[] collectFragmentLifecycleCallbacks() {
        Object[] callbacks = null;
        synchronized (mFragmentLifecycleCallbackses) {
            if (mFragmentLifecycleCallbackses.size() > 0) {
                callbacks = mFragmentLifecycleCallbackses.toArray();
            }
        }
        return callbacks;
    }

    public interface FragmentLifecycleCallbacks {
        void onFragmentAttached(IMasterFragment fragment);

        void onFragmentCreated(IMasterFragment fragment, Bundle savedInstanceState);

        void onFragmentViewCreated(IMasterFragment fragment);

        void onFragmentStarted(IMasterFragment fragment);

        void onFragmentResumed(IMasterFragment fragment);

        void onFragmentActivated(IMasterFragment fragment);

        void onFragmentDeactivated(IMasterFragment fragment);

        void onFragmentPaused(IMasterFragment fragment);

        void onFragmentStopped(IMasterFragment fragment);

        void onFragmentSaveInstanceState(IMasterFragment fragment, Bundle outState);

        void onFragmentDestroyed(IMasterFragment fragment);

        void onFragmentDetached(IMasterFragment fragment);
    }

    public static class SimpleFragmentLifecycleCallbacks implements FragmentLifecycleCallbacks {
        public void onFragmentAttached(IMasterFragment fragment) {
        }

        public void onFragmentCreated(IMasterFragment fragment, Bundle savedInstanceState) {
        }

        public void onFragmentViewCreated(IMasterFragment fragment) {
        }

        public void onFragmentStarted(IMasterFragment fragment) {
        }

        public void onFragmentResumed(IMasterFragment fragment) {
        }

        public void onFragmentActivated(IMasterFragment fragment) {
        }

        public void onFragmentDeactivated(IMasterFragment fragment) {
        }

        public void onFragmentPaused(IMasterFragment fragment) {
        }

        public void onFragmentStopped(IMasterFragment fragment) {
        }

        public void onFragmentSaveInstanceState(IMasterFragment fragment, Bundle outState) {
        }

        public void onFragmentDestroyed(IMasterFragment fragment) {
        }

        public void onFragmentDetached(IMasterFragment fragment) {
        }
    }
}

final class FragmentMasterState implements Parcelable {

    Bundle mFragments;

    boolean mIsSlideable;

    boolean mHomeFragmentApplied;

    public FragmentMasterState() {
    }

    private FragmentMasterState(Parcel in) {
        mFragments = in.readBundle();
        mIsSlideable = in.readInt() == 0;
        mHomeFragmentApplied = in.readInt() == 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(mFragments);
        dest.writeInt(mIsSlideable ? 0 : 1);
        dest.writeInt(mHomeFragmentApplied ? 0 : 1);
    }

    public static final Parcelable.Creator<FragmentMasterState> CREATOR
            = new Parcelable.Creator<FragmentMasterState>() {
        public FragmentMasterState createFromParcel(Parcel in) {
            return new FragmentMasterState(in);
        }

        public FragmentMasterState[] newArray(int size) {
            return new FragmentMasterState[size];
        }
    };
}
