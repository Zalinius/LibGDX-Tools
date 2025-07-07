package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class UniversalInputStageWithBackground extends UniversalInputStage {

	private final Actor background;

	/**
	 * Creates a stage that will ensure that the supplied background is always present
	 * @param name The name of the stage, used for stage layering and useful for debugging
	 * @param viewport
	 * @param background the background to always keep visible as the back-most actor
	 * @param inputStrategySwitcher The stage needs to register with this
	 */
	public UniversalInputStageWithBackground(String name, Viewport viewport, Actor background, InputStrategySwitcher inputStrategySwitcher) {
		super(name, viewport, inputStrategySwitcher);

		this.background = background;
	}


	@Override
	public void clear() {
		super.clear();
		addActor(background); // Always keep the background
	}

}
