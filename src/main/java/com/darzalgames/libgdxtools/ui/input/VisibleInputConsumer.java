package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UserInterfaceFactory;

public interface VisibleInputConsumer extends InputConsumer {

	Actor getView();

	float getMinHeight();

	static boolean isSpacer(VisibleInputConsumer button) {
		return button != null && UserInterfaceFactory.SPACER_NAME.equalsIgnoreCase(button.getView().getName());
	}

}
