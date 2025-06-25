package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darzalgames.libgdxtools.maingame.StageLikeRenderable;

public class OptionalDrawStage extends Stage implements StageLikeRenderable {

	private boolean shouldDraw;

	public OptionalDrawStage(String name, Viewport viewport) {
		super(viewport);
		getRoot().setName(name);
	}

	@Override
	public void draw() {
		if (shouldDraw) {
			getViewport().apply();
			super.draw();
		}
	}

	@Override
	public void setShouldDraw(boolean shouldDraw) {
		this.shouldDraw = shouldDraw;
	}

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height, true);
		getCamera().update();
	}

	@Override
	public String nameOfThingAtCursorPosition(float x, float y, boolean b) {
		Actor hitActor = hit(x, y, true);
		if (hitActor != null) {
			return hitActor.getName();
		} else {
			return "";
		}
	}

	@Override
	public String getName() {
		return getRoot().getName();
	}
}
