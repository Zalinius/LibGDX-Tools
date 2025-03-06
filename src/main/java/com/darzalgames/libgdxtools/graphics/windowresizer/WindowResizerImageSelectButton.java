package com.darzalgames.libgdxtools.graphics.windowresizer;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

public abstract class WindowResizerImageSelectButton extends UniversalButton implements WindowResizerButton {

	protected WindowResizer windowResizer;

	protected WindowResizerImageSelectButton(TextButton button, Image image, Runnable runnable,
			InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(button, () -> "", image, runnable, inputStrategySwitcher, soundInteractListener);
	}

	@Override
	public void setWindowResizer(WindowResizer windowResizer) {
		this.windowResizer = windowResizer;
	}

	@Override
	public UniversalButton getButton() {
		return this;
	}
	
}
