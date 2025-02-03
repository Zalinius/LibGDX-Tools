package com.darzalgames.libgdxtools.ui.input.strategy;

public class MouseInputStrategy implements InputStrategy {
	
	@Override
	public boolean shouldFlashButtons() {
		return false;
	}

	@Override
	public boolean isMouseMode() {
		return true;
	}

}
