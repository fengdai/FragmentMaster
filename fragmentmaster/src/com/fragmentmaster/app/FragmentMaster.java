package com.fragmentmaster.app;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentmaster.BuildConfig;
import com.fragmentmaster.animator.PageAnimator;

import java.util.HashSet;
import java.util.List;

public abstract class FragmentMaster {

    private static final String TAG = "FragmentMaster";

    // The host activity.
    private final MasterActivity mActivity;

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

    private final HashSet<IMasterFragment> mFinishPendingFragments = new HashSet<>();

    // Event dispatcher
    private final MasterEventDispatcher mEventDispatcher;

    FragmentMaster(MasterActivity activity) {
        mActivity = activity;
        mFragmentManager = activity.getSupportFragmentManager();
        mEventDispatcher = new MasterEventDispatcher(activity);
    }

    public MasterActivity getActivity() {
        return mActivity;
    }

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public int getContainerResID() {
        return mContainerResID;
    }

    protected int getFragmentContainerId() {
        return getContainerResID();
    }

    public final void startFragmentForResult(IMasterFragment target,
                                             Request request, int requestCode) {
        ensureInstalled();

        IMasterFragment fragment = newFragment(request.getClassName());
        fragment.setRequest(request);
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
        PageAnimator pageAnimator = null;
        if (fragment != null) {
            pageAnimator = fragment.onCreatePageAnimator();
        }
        this.setPageAnimator(pageAnimator);
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

    // ------------------------------------------------------------------------
    // Dispatch events
    // ------------------------------------------------------------------------

    protected boolean dispatchKeyEvent(KeyEvent event) {
        return mEventDispatcher.dispatchKeyEvent(event);
    }

    protected boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return mEventDispatcher.dispatchKeyShortcutEvent(event);
    }

    protected boolean dispatchTouchEvent(MotionEvent event) {
        return mEventDispatcher.dispatchTouchEvent(event);
    }

    protected boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return mEventDispatcher.dispatchGenericMotionEvent(ev);
    }

    protected boolean dispatchTrackballEvent(MotionEvent ev) {
        return mEventDispatcher.dispatchTrackballEvent(ev);
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
