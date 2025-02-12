package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * An action that does nothing, and ends the first frame is called
 */
public class NullAction extends Action {

	@Override
	public boolean act(float delta) {
		return true;
	}

}
