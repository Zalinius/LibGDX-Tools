package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.darzalgames.libgdxtools.ui.input.InputObserver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;


/**
 * @author DarZal
 * A {@link KeyboardButton} which will only be visible and intractable in mouse mode
 */
public class MouseOnlyButton extends KeyboardButton implements InputObserver {

	public MouseOnlyButton(TextButton textButton, Runnable runnable, InputStrategyManager inputStrategyManager, Runnable soundInteractListener) {
		super(textButton, runnable, inputStrategyManager, soundInteractListener);
		inputStrategyManager.register(this);
		setVisibilityBasedOnCurrentInputStrategy(inputStrategyManager);
	}

	@Override
	public void inputStrategyChanged(InputStrategyManager inputStrategyManager) {
		setVisibilityBasedOnCurrentInputStrategy(inputStrategyManager);
	}
	
	private void setVisibilityBasedOnCurrentInputStrategy(InputStrategyManager inputStrategyManager) {
		this.getView().setVisible(inputStrategyManager.showMouseExclusiveUI());
	}

	@Override
	public boolean shouldBeUnregistered() {
		return this.getView().getStage() == null;
	}
	
}
