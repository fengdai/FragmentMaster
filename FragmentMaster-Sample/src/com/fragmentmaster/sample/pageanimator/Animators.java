package com.fragmentmaster.sample.pageanimator;

import com.fragmentmaster.animator.PageAnimator;

public class Animators {
	public static final PageAnimator STACK_ANIMATOR = new StackAnimator();
	public static final PageAnimator ENTER_OVER_SHOOT_ANIMATOR = new EnterOvershootAnimator();
	public static final PageAnimator VERTICAL_SLIDE_ANIMATOR = new VerticalSlideAnimator();
}
