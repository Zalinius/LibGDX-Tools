package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputObserver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;


/**
 * A {@link UniversalButton} which will only be visible and intractable in mouse mode
 */
public class MouseOnlyButton extends UniversalButton implements InputObserver {

	public MouseOnlyButton(TextButton textButton, Runnable runnable, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(textButton, runnable, inputStrategySwitcher, soundInteractListener);
		inputStrategySwitcher.register(this);
		setVisibilityBasedOnCurrentInputStrategy(inputStrategySwitcher);
	}

	public MouseOnlyButton(TextButton textButton, Image image, Runnable runnable, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(textButton, image, runnable, inputStrategySwitcher, soundInteractListener);
		inputStrategySwitcher.register(this);
		setVisibilityBasedOnCurrentInputStrategy(inputStrategySwitcher);
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategySwitcher) {
		setVisibilityBasedOnCurrentInputStrategy(inputStrategySwitcher);
	}
	
	private void setVisibilityBasedOnCurrentInputStrategy(InputStrategySwitcher inputStrategySwitcher) {
		this.getView().setVisible(inputStrategySwitcher.isMouseMode());
	}

	@Override
	public boolean shouldBeUnregistered() {
		return this.getView().getStage() == null;
	}
	
}
