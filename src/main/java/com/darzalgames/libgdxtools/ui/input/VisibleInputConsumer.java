package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface VisibleInputConsumer extends InputConsumer {

	boolean isOver();
	Actor getView();
	float getMinHeight();


	static boolean isSpacer(VisibleInputConsumer button) {
		return button.isDisabled() && button.isBlank();
	}

}
