package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.inputpriority.InputObserver;


/**
 * @author DarZal
 * A {@link UniversalButton} which will only be visible and intractable in mouse mode
 */
public class MouseOnlyButton extends UniversalButton implements InputObserver {

	public MouseOnlyButton(TextButton textButton, Runnable runnable, InputStrategySwitcher inputStrategyManager, Runnable soundInteractListener) {
		super(textButton, runnable, inputStrategyManager, soundInteractListener);
		inputStrategyManager.register(this);
		setVisibilityBasedOnCurrentInputStrategy(inputStrategyManager);
	}

	public MouseOnlyButton(TextButton textButton, Image image, Runnable runnable, InputStrategySwitcher inputStrategyManager, Runnable soundInteractListener) {
		super(textButton, image, runnable, inputStrategyManager, soundInteractListener);
		inputStrategyManager.register(this);
		setVisibilityBasedOnCurrentInputStrategy(inputStrategyManager);
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategyManager) {
		setVisibilityBasedOnCurrentInputStrategy(inputStrategyManager);
	}
	
	private void setVisibilityBasedOnCurrentInputStrategy(InputStrategySwitcher inputStrategyManager) {
		this.getView().setVisible(inputStrategyManager.showMouseExclusiveUI());
	}

	@Override
	public boolean shouldBeUnregistered() {
		return this.getView().getStage() == null;
	}
	
}
