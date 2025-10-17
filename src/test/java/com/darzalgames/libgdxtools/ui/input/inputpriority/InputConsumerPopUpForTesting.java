package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;

public class InputConsumerPopUpForTesting extends InputConsumerForTesting implements PopUp {

	@Override
	public boolean addListener(EventListener listener) {
		return false;
	}

	@Override
	public boolean removeListener(EventListener listener) {
		return false;
	}

	@Override
	public Actor getAsActor() {
		return new Actor();
	}

}
