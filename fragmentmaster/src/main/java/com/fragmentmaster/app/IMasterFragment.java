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

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.fragmentmaster.animator.PageAnimator;

/**
 * Common MasterFragment interface.
 */
public interface IMasterFragment
        extends
        IFragmentWrapper,
        EventDispatcher,
        KeyEvent.Callback {

    /**
     * Standard fragment result: operation canceled.
     */
    int RESULT_CANCELED = 0;

    /**
     * Standard fragment result: operation succeeded.
     */
    int RESULT_OK = -1;

    /**
     * Start a new IMasterFragment.
     *
     * @param request The request used to start.
     * @see #startFragment(Class)
     * @see #startFragmentForResult(Request, int)
     */
    void startFragment(Request request);

    /**
     * Same as calling {@link #startFragment(Request)}
     *
     * @param clazz The {@link java.lang.Class} of the IMasterFragment.
     * @see #startFragment(Request)
     * @see #startFragmentForResult(Request, int)
     */
    void startFragment(Class<? extends IMasterFragment> clazz);

    /**
     * Start an IMasterFragment for which you would like a result when it finished.
     * When this IMasterFragment exits, your
     * {@link #onFragmentResult(int, int, Request)} method will be called with the given requestCode.
     * Using a negative requestCode is the same as calling
     * {@link #startFragment(Request)}
     *
     * @param request     The request used to start.
     * @param requestCode If >= 0, this code will be returned in
     *                    onFragmentResult() when the IMasterFragment exits.
     * @see #startFragment(Request)
     * @see #startFragmentForResult(Class, int)
     */
    void startFragmentForResult(Request request, int requestCode);

    /**
     * Same as calling {@link #startFragmentForResult(Request, int)}.
     *
     * @param clazz       The {@link java.lang.Class} of the IMasterFragment.
     * @param requestCode If >= 0, this code will be returned in
     *                    onFragmentResult() when the IMasterFragment exits.
     * @see #startFragmentForResult(Request, int)
     */
    void startFragmentForResult(Class<? extends IMasterFragment> clazz,
                                int requestCode);

    /**
     * This is called when a child IMasterFragment of this one calls its
     * {@link #startFragment} or {@link #startFragmentForResult} method.
     *
     * @param child       The IMasterFragment making the call.
     * @param request     The request used to start.
     * @param requestCode If >= 0, this code will be returned in
     *                    onFragmentResult() when the IMasterFragment exits.
     * @see #startFragment
     * @see #startFragmentForResult
     */
    void startFragmentFromChild(IMasterFragment child,
                                Request request, int requestCode);

    /**
     * Return the request that started this IMasterFragment.
     */
    Request getRequest();

    /**
     * Change the intent returned by {@link #getRequest()}.
     *
     * @param request The new Request object to return from getRequest
     * @see #getRequest()
     */
    void setRequest(Request request);

    void setTargetChildFragment(IMasterFragment targetChildFragment);

    IMasterFragment getTargetChildFragment();

    /**
     * Call this to set the result that your fragment will return to its caller.
     *
     * @param resultCode The result code to propagate back to the originating
     *                   IMasterFragment, often RESULT_CANCELED or RESULT_OK
     * @see #RESULT_CANCELED
     * @see #RESULT_OK
     * @see #setResult(int, Request)
     */
    void setResult(int resultCode);

    /**
     * Call this to set the result that your fragment will return to its caller.
     *
     * @param resultCode The result code to propagate back to the originating
     *                   IMasterFragment, often RESULT_CANCELED or RESULT_OK
     * @param data       The data to propagate back to the originating IMasterFragment.
     * @see #RESULT_CANCELED
     * @see #RESULT_OK
     * @see #setResult(int)
     */
    void setResult(int resultCode, Request data);

    /**
     * Finish this fragment.
     */
    void finish();

    boolean isFinishing();

    FragmentMaster getFragmentMaster();

    /**
     * Whether the state have been saved by system.
     */
    boolean hasStateSaved();

    void setSoftInputMode(int mode);

    int getSoftInputMode();

    void setPrimary(boolean isPrimary);

    boolean isPrimary();

    boolean isActive();

    void setSlideable(boolean slideable);

    boolean isSlideable();

    PageAnimator onCreatePageAnimator();

    /**
     * Called when user has come to this fragment.
     */
    void onActivate();

    /**
     * Called when user has left this fragment.
     */
    void onDeactivate();

    void onFragmentResult(int requestCode, int resultCode, Request data);

    /**
     * Called when the fragment has detected the user's press of the back key.
     * The default implementation simply finishes the current fragment, but you
     * can override this to do whatever you want.
     */
    void onBackPressed();

    boolean onTouchEvent(MotionEvent ev);

    boolean onKeyShortcut(int keyCode, KeyEvent event);

    boolean onTrackballEvent(MotionEvent event);

    boolean onGenericMotionEvent(MotionEvent event);
}
