package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UserInterfaceFactory;

public interface VisibleInputConsumer extends InputConsumer {

	Actor getView();

	float getMinHeight();

	static boolean isSpacer(VisibleInputConsumer button) {
		return button != null && UserInterfaceFactory.SPACER_NAME.equalsIgnoreCase(button.getView().getName());
	}

	/**
	 * Only certain systems check isOver: a UniversalDoodad and my TestInputButtons implement this properly,
	 * and other VisibleInputConsumer classes should either have a similarly trivial implementation (from
	 * their click listener) OR this is not a meaningful check for them. AKA this is generally something
	 * you ask specifically of a Button, not a menu or anything like that.
	 * @return whether or not this object is currently in focus
	 */
	default boolean isOver() {
		return false;
	}

}
