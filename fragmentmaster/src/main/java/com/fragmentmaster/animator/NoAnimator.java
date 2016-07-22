package com.fragmentmaster.animator;

import android.view.View;

public final class NoAnimator extends PageAnimator {

    public static final NoAnimator INSTANCE = new NoAnimator();

    private NoAnimator() {
    }

    @Override
    public void transformPage(View page, float position, boolean enter) {
        page.setVisibility(View.VISIBLE);
    }

    @Override
    protected void transformBackgroundPage(View page, float position, boolean enter) {
    }

    @Override
    protected void transformForegroundPage(View page, float position, boolean enter) {
    }
}
