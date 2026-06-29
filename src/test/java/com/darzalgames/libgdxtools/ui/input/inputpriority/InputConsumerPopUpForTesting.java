package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.PopUpMenu;

public class InputConsumerPopUpForTesting extends InputConsumerForTesting implements PopUpMenu {

	private final Actor innerActor;

	public InputConsumerPopUpForTesting() {
		innerActor = new Actor();
	}

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
		return innerActor;
	}

	@Override
	public Actor getView() {
		return innerActor;
	}

	@Override
	public float getMinHeight() {
		return 0;
	}

	@Override
	public void setUpDesiredSize() {
		// not needed for testing
	}

}
