package com.darzalgames.libgdxtools.maingame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TestViewport extends Viewport {

	@Override
	public void apply(boolean centerCamera) {
		// don't do any GL stuff
	}

	@Override
	public Vector2 unproject(Vector2 touchCoords) {
		// don't mess with testing coordinates
		return touchCoords;
	}
}
