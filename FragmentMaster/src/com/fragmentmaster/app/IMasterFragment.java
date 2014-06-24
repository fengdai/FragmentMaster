package com.fragmentmaster.app;

import android.view.KeyEvent;

public interface IMasterFragment
		extends
			FragmentMaster.Callback,
			KeyEvent.Callback {

	/** Standard fragment result: operation canceled. */
	public static final int RESULT_CANCELED = 0;
	/** Standard fragment result: operation succeeded. */
	public static final int RESULT_OK = -1;
}
