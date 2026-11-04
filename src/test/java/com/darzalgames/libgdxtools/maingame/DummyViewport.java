package com.darzalgames.libgdxtools.maingame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * A dummy extension of LibGDX's {@link Viewport Viewport} class which inherits as much functionality as possible but makes no OpenGL calls.
 * Useful to instantiate a Stage for testing.
 */
public class DummyViewport extends Viewport {

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
