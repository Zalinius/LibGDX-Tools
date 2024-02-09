package com.darzalgames.libgdxtools.ui.input.strategy;

public class MouseInputStrategy implements InputStrategy {
	
	@Override
	public boolean shouldFlashButtons() {
		return false;
	}

	@Override
	public boolean showMouseExclusiveUI() {
		return true;
	}

}
