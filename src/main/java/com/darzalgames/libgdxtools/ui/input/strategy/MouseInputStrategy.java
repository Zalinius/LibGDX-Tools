package com.darzalgames.libgdxtools.ui.input.strategy;

import java.util.Map;

public class MouseInputStrategy extends InputStrategyConcrete {

	public MouseInputStrategy(Map<String, String> buttonHints) {
		super(buttonHints);
	}
	
	@Override
	public boolean shouldFocusFirstButton() {
		return false;
	}
	
	@Override
	public boolean shouldFlashButtons() {
		return false;
	}

	@Override
	public boolean showMouseExclusiveButtons() {
		return true;
	}

}
