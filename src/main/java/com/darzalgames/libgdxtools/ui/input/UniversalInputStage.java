package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputStrategyObserver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class UniversalInputStage extends StageBest implements InputStrategyObserver {

	private boolean mouseMode;

	/**
	 * Creates a stage which can filter mouse input depending on the current {@link InputStrategySwitcher} input mode
	 * @param name The name of the stage, used for stage layering and useful for debugging
	 * @param viewport
	 * @param inputStrategySwitcher The stage needs to register with this
	 */
	public UniversalInputStage(String name, Viewport viewport, InputStrategySwitcher inputStrategySwitcher) {
		super(name, viewport);
		inputStrategySwitcher.register(this);
	}

	@Override
	public void act(final float delta) {
		if (mouseMode) {
			// if playing mouse-driven, use a normal stage
			super.act(delta);
		} else {
			// if playing keyboard-driven, skip all the stage's code to do with detecting and firing mouse enter/exit events,
			// and just call act() on our actors like in Stage
			getRoot().act(delta);
		}
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategySwitcher) {
		mouseMode = inputStrategySwitcher.isMouseMode();
	}

	@Override
	public boolean shouldBeUnregistered() {
		return false;
	}

}
