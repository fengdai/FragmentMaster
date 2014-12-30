package com.fragmentmaster.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.fragmentmaster.app.event.FragmentEventDispatcher;

public class MasterFragmentDelegate {

    private static final String BUNDLE_KEY_TARGET_CHILD_FRAGMENT
            = "FragmentMaster:TARGET_CHILD_FRAGMENT";

    private static final String BUNDLE_KEY_STATE = "FragmentMaster:MASTER_FRAGMENT_STATE";

    private ContextThemeWrapper mContextThemeWrapper;

    IMasterFragment mMasterFragment;

    Request mRequest = null;

    // SoftInputMode, SOFT_INPUT_ADJUST_UNSPECIFIED is default.
    int mSoftInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED;

    boolean mIsSlideable = true;

    private IMasterFragment mTargetChildFragment;

    private static final int MSG_USER_ACTIVE = 1;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_USER_ACTIVE:
                    performUserActive();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private MasterActivity mActivity;

    private boolean mStateSaved = false;

    private int mResultCode = IMasterFragment.RESULT_CANCELED;

    private Request mResultData = null;

    private boolean mIsUserActive = false;

    private boolean mIsPrimary = false;

    private boolean mFinished = false;

    private FragmentEventDispatcher mEventDispatcher;

    public MasterFragmentDelegate(IMasterFragment masterFragment) {
        mMasterFragment = masterFragment;
        mEventDispatcher = new FragmentEventDispatcher(masterFragment);
    }

    public void onAttach(Activity activity) {
        if (activity instanceof MasterActivity) {
            mActivity = (MasterActivity) activity;
        }
    }

    public void onDetach() {
        mActivity = null;
    }

    public MasterActivity getMasterActivity() {
        return mActivity;
    }

    public FragmentMaster getFragmentMaster() {
        return mActivity == null ? null : mActivity.getFragmentMaster();
    }

    public LayoutInflater getLayoutInflater() {
        return mActivity.getLayoutInflater().cloneInContext(getContextThemeWrapper());
    }

    public ContextThemeWrapper getContextThemeWrapper() {
        if (mContextThemeWrapper == null) {
            mContextThemeWrapper = FragmentThemeHelper.createContextThemeWrapper(mActivity, mMasterFragment.getFragment());
        }
        return mContextThemeWrapper;
    }

    /**
     * Starts a specific fragment.
     */
    public void startFragment(Class<? extends IMasterFragment> clazz) {
        startFragmentForResult(new Request(clazz), -1);
    }

    /**
     * Starts a fragment.
     *
     * @param request The request.
     */
    public void startFragment(Request request) {
        startFragmentForResult(request, -1);
    }

    public void startFragmentForResult(Class<? extends IMasterFragment> clazz,
                                       int requestCode) {
        startFragmentForResult(new Request(clazz), requestCode);
    }

    public void startFragmentForResult(Request request, int requestCode) {
        checkState();
        if (mMasterFragment.getParentFragment() instanceof IMasterFragment) {
            ((IMasterFragment) mMasterFragment.getParentFragment())
                    .startFragmentFromChild(mMasterFragment, request,
                            requestCode);
        } else {
            getFragmentMaster().startFragmentForResult(mMasterFragment,
                    request, requestCode);
        }
    }

    private void checkState() {
        if (mActivity == null) {
            throw new IllegalStateException(
                    "Can not perform this action. Fragment "
                            + this.mMasterFragment
                            + " not attached to MasterActivity!");
        }
    }

    public void startFragmentFromChild(IMasterFragment childFragment,
                                       Request request, int requestCode) {
        if (requestCode != -1) {
            mTargetChildFragment = childFragment;
        }
        startFragmentForResult(request, requestCode);
    }

    /**
     * Call this to set the result that your fragment will return to its caller.
     */
    public final void setResult(int resultCode) {
        synchronized (this) {
            mResultCode = resultCode;
            mResultData = null;
        }
    }

    /**
     * Call this to set the result that your fragment will return to its caller.
     */
    public final void setResult(int resultCode, Request data) {
        synchronized (this) {
            mResultCode = resultCode;
            mResultData = data;
        }
    }

    public void finish() {
        checkState();

        int resultCode;
        Request resultData;
        synchronized (this) {
            resultCode = mResultCode;
            resultData = mResultData;
        }
        getFragmentMaster().finishFragment(mMasterFragment, resultCode,
                resultData);
        mFinished = true;
    }

    public boolean isFinishing() {
        return mFinished;
    }

    /**
     * Called when the fragment has detected the user's press of the back key.
     * The default implementation simply finishes the current fragment, but you
     * can override this to do whatever you want.
     */
    public void onBackPressed() {
        mMasterFragment.finish();
    }

    public Request getRequest() {
        return mRequest;
    }

    public void setRequest(Request newRequest) {
        mRequest = newRequest;
    }

    public void onSaveInstanceState(Bundle outState) {
        if (mTargetChildFragment != null) {
            mMasterFragment.getChildFragmentManager().putFragment(outState,
                    BUNDLE_KEY_TARGET_CHILD_FRAGMENT,
                    mTargetChildFragment.getFragment());
        }
        outState.putParcelable(BUNDLE_KEY_STATE, new MasterFragmentState(this));
        mStateSaved = true;
    }

    public void onCreate(Bundle savedInstanceState) {
        mStateSaved = false;
        if (savedInstanceState != null) {
            MasterFragmentState state = savedInstanceState
                    .getParcelable(BUNDLE_KEY_STATE);
            state.restore(this);
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Use window background as the top level background.
        // Note: The view is an instance of NoSaveStateFrameLayout,
        // which is inserted between the Fragment's view and its container by FragmentManager.
        TypedValue outValue = new TypedValue();
        getContextThemeWrapper().getTheme().resolveAttribute(android.R.attr.windowBackground, outValue, true);
        view.setBackgroundResource(
                outValue.resourceId);
        // Set the "clickable" of the fragment's root view to true to avoid
        // touch events to be passed to the views behind the fragment.
        view.setClickable(true);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        mStateSaved = false;
        if (savedInstanceState != null) {
            mTargetChildFragment = (IMasterFragment) mMasterFragment
                    .getChildFragmentManager().getFragment(savedInstanceState,
                            BUNDLE_KEY_TARGET_CHILD_FRAGMENT);
        }
    }

    public void onStart() {
        mStateSaved = false;
    }

    public void onResume() {
        mStateSaved = false;
        if (isPrimary()) {
            mHandler.sendEmptyMessage(MSG_USER_ACTIVE);
        }
    }

    public void onPause() {

        if (mHandler.hasMessages(MSG_USER_ACTIVE)) {
            mHandler.removeMessages(MSG_USER_ACTIVE);
            this.performUserActive();
        }

        if (isPrimary()) {
            performUserLeave();
        }
    }

    /**
     * Whether the state have been saved by system.
     */
    public boolean hasStateSaved() {
        return mStateSaved;
    }

    public void setSoftInputMode(int mode) {
        if (mSoftInputMode != mode) {
            mSoftInputMode = mode;
            invalidateWindowConfiguration();
        }
    }

    public int getSoftInputMode() {
        return mSoftInputMode;
    }

    void invalidateWindowConfiguration() {
        if (mMasterFragment.getActivity() != null) {
            mMasterFragment.getActivity().getWindow()
                    .setSoftInputMode(mSoftInputMode);
        }
    }

    public void setSlideable(boolean slideable) {
        if (mIsSlideable != slideable) {
            mIsSlideable = slideable;
            invalidateMasterConfiguration();
        }
    }

    public boolean isSlideable() {
        return mIsSlideable;
    }

    void invalidateMasterConfiguration() {
        checkState();
        FragmentMaster fragmentMaster = getFragmentMaster();
        fragmentMaster.setSlideable(mIsSlideable);
    }

    public void setPrimary(boolean isPrimary) {
        mMasterFragment.setMenuVisibility(isPrimary);
        mMasterFragment.setUserVisibleHint(isPrimary);
        onSetPrimary(isPrimary);
    }

    private void onSetPrimary(boolean isPrimary) {
        boolean oldPrimaryState = mIsPrimary;
        mIsPrimary = isPrimary;
        if (!oldPrimaryState && isPrimary) {
            invalidateWindowConfiguration();
            invalidateMasterConfiguration();
            if (mMasterFragment.isResumed()) {
                performUserActive();
            }
        } else if (oldPrimaryState && !isPrimary) {
            if (mMasterFragment.isResumed()) {
                performUserLeave();
            }
        }
    }

    private void performUserActive() {
        mIsUserActive = true;
        mMasterFragment.onUserActive();
    }

    private void performUserLeave() {
        mIsUserActive = false;
        mMasterFragment.onUserLeave();
    }

    public boolean isUserActive() {
        return mIsUserActive;
    }

    public boolean isPrimary() {
        return mIsPrimary;
    }

    public IMasterFragment getTargetChildFragment() {
        return mTargetChildFragment;
    }

    public void setTargetChildFragment(IMasterFragment targetChildFragment) {
        mTargetChildFragment = targetChildFragment;
    }

    // ------------------------------------------------------------------------
    // Dispatch events
    // ------------------------------------------------------------------------

    public boolean dispatchKeyEvent(KeyEvent event) {
        return mEventDispatcher.dispatchKeyEvent(event);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return mEventDispatcher.dispatchKeyShortcutEvent(event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEventDispatcher.dispatchTouchEvent(ev);
    }

    public boolean dispatchTrackballEvent(MotionEvent ev) {
        return mEventDispatcher.dispatchTrackballEvent(ev);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return mEventDispatcher.dispatchGenericMotionEvent(ev);
    }
}

final class MasterFragmentState implements Parcelable {

    Request mRequest;

    int mSoftInputMode;

    boolean mIsSlideable;

    public MasterFragmentState(MasterFragmentDelegate mfd) {
        mRequest = mfd.mRequest;
        mSoftInputMode = mfd.mSoftInputMode;
        mIsSlideable = mfd.mIsSlideable;
    }

    public MasterFragmentState(Parcel in) {
        mRequest = in
                .readParcelable(MasterFragmentState.class.getClassLoader());
        mSoftInputMode = in.readInt();
        mIsSlideable = in.readInt() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mRequest, flags);
        dest.writeInt(mSoftInputMode);
        dest.writeInt(mIsSlideable ? 1 : 0);
    }

    public static final Parcelable.Creator<MasterFragmentState> CREATOR
            = new Parcelable.Creator<MasterFragmentState>() {
        public MasterFragmentState createFromParcel(Parcel in) {
            return new MasterFragmentState(in);
        }

        public MasterFragmentState[] newArray(int size) {
            return new MasterFragmentState[size];
        }
    };

    public void restore(MasterFragmentDelegate mfd) {
        mfd.mRequest = mRequest;
        mfd.mSoftInputMode = mSoftInputMode;
    }
}
