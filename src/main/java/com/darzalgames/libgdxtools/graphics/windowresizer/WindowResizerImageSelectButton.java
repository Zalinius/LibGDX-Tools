package com.darzalgames.libgdxtools.graphics.windowresizer;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public abstract class WindowResizerImageSelectButton extends KeyboardButton implements WindowResizerButton {

	protected WindowResizer windowResizer;

	protected WindowResizerImageSelectButton(TextButton button, Image image, Runnable runnable,
			InputStrategyManager inputStrategyManager, Runnable soundInteractListener) {
		super(button, image, runnable, inputStrategyManager, soundInteractListener);
	}

	@Override
	public void setWindowResizer(WindowResizer windowResizer) {
		this.windowResizer = windowResizer;
	}

	@Override
	public KeyboardButton getButton() {
		return this;
	}
	
}
