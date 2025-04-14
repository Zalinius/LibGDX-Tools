package com.darzalgames.libgdxtools.graphics.windowresizer;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darzalgames.darzalcommon.functional.Suppliers;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.BasicButton;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

public abstract class WindowResizerImageSelectButton extends UniversalButton implements WindowResizerButton {

	protected WindowResizer windowResizer;

	protected WindowResizerImageSelectButton(BasicButton button, Image image, Runnable runnable,
			InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(button, Suppliers.emptyString(), image, runnable, inputStrategySwitcher, soundInteractListener);
	}

	@Override
	public void setWindowResizer(WindowResizer windowResizer) {
		this.windowResizer = windowResizer;
	}

	@Override
	public UniversalButton getWindowResizerButton() {
		return this;
	}
	
}
