package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class OptionalDrawStage extends Stage {

	private boolean shouldDraw;

	public OptionalDrawStage(Viewport viewport) {
		super(viewport);
	}

	@Override
	public void draw() {
		if (shouldDraw) {
			getViewport().apply();
			super.draw();
		}
	}

	public void setShouldDraw(boolean shouldDraw) {
		this.shouldDraw = shouldDraw;
	}
}
