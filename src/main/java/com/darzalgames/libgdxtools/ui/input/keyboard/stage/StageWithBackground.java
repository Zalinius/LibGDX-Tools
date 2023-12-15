package com.darzalgames.libgdxtools.ui.input.keyboard.stage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.LabelMaker;

public class StageWithBackground extends KeyboardStage {
	private Image background;

	public StageWithBackground(final Viewport viewport, Texture backgroundTex) {
		super(viewport);

		background = new Image(backgroundTex);
		LabelMaker.makeActorCentered(background);
		background.setTouchable(Touchable.disabled);
		addActor(background);
	}

	@Override
	public void clear() {
		super.clear();
		addActor(background); // Always keep the background, just in case
	}

}
