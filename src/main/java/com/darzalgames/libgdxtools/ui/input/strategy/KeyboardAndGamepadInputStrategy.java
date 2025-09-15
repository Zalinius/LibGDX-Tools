package com.darzalgames.libgdxtools.ui.input.strategy;

public class KeyboardAndGamepadInputStrategy implements InputStrategy {

	@Override
	public boolean shouldFlashButtons() {
		return true;
	}

	@Override
	public boolean isMouseMode() {
		return false;
	}

}
