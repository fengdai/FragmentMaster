package android.support.v4.view;

import android.support.v4.view.KeyEventCompat.BaseKeyEventVersionImpl;
import android.support.v4.view.KeyEventCompat.EclairKeyEventVersionImpl;
import android.support.v4.view.KeyEventCompat.HoneycombKeyEventVersionImpl;
import android.support.v4.view.KeyEventCompat.KeyEventVersionImpl;
import android.view.KeyEvent;
import android.view.View;

/**
 * Helper for accessing features in {@link KeyEvent} introduced after API level
 * 4 in a backwards compatible fashion.
 * 
 * Modified from KeyEventCompat of android-support-v4. The official
 * KeyEventCompat doesn't use EclairKeyEventVersionImpl even the SDK is matched.
 */
public class KeyEventCompat2 {

	/**
	 * Select the correct implementation to use for the current platform.
	 */
	static final KeyEventVersionImpl IMPL;
	static {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			IMPL = new HoneycombKeyEventVersionImpl();
		} else if (android.os.Build.VERSION.SDK_INT >= 5) {
			// Use EclairKeyEventVersionImpl.
			IMPL = new EclairKeyEventVersionImpl();
		} else {
			IMPL = new BaseKeyEventVersionImpl();
		}
	}

	// -------------------------------------------------------------------

	public static int normalizeMetaState(int metaState) {
		return IMPL.normalizeMetaState(metaState);
	}

	public static boolean metaStateHasModifiers(int metaState, int modifiers) {
		return IMPL.metaStateHasModifiers(metaState, modifiers);
	}

	public static boolean metaStateHasNoModifiers(int metaState) {
		return IMPL.metaStateHasNoModifiers(metaState);
	}

	public static boolean hasModifiers(KeyEvent event, int modifiers) {
		return IMPL.metaStateHasModifiers(event.getMetaState(), modifiers);
	}

	public static boolean hasNoModifiers(KeyEvent event) {
		return IMPL.metaStateHasNoModifiers(event.getMetaState());
	}

	public static void startTracking(KeyEvent event) {
		IMPL.startTracking(event);
	}

	public static boolean isTracking(KeyEvent event) {
		return IMPL.isTracking(event);
	}

	public static Object getKeyDispatcherState(View view) {
		return IMPL.getKeyDispatcherState(view);
	}

	public static boolean dispatch(KeyEvent event, KeyEvent.Callback receiver,
			Object state, Object target) {
		return IMPL.dispatch(event, receiver, state, target);
	}
}
