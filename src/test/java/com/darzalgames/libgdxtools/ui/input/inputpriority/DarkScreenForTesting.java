package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.libgdxtools.maingame.StageLikeRenderable;

public class DarkScreenForTesting implements DarkScreenBehindPopUp {

	@Override
	public boolean remove() {
		return false;
	}

	@Override
	public void fadeIn(Actor actor, boolean canDismiss, StageLikeRenderable stageLikeRenderable) {
		// Meaningless for testing
	}

	@Override
	public void fadeOutAndRemove() {
		// Meaningless for testing
	}

}
