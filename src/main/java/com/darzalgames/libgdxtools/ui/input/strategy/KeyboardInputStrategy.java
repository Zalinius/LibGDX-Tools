package com.darzalgames.libgdxtools.ui.input.strategy;

public class KeyboardInputStrategy implements InputStrategy {
	
	@Override
	public boolean shouldFocusFirstButton() {
		return true;
	}
	
	@Override
	public boolean shouldFlashButtons() {
		return true;
	}

	@Override
	public boolean showMouseExclusiveUI() {
		return false;
	}

}
