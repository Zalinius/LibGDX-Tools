package com.darzalgames.libgdxtools.ui.input.strategy;

import java.util.Map;

/**
 * For the purposes of Quest Giver, this is also the gamepad input strategy.
 * (In a game where we show more control prompts then just directional ones,
 * they'd likely have to be split out into two strategies to show different hints.)
 * @author DarZal
 *
 */
public class KeyboardInputStrategy extends InputStrategyConcrete {
	
	public KeyboardInputStrategy(Map<String, String> buttonHints) {
		super(buttonHints);
	}

	@Override
	public boolean shouldFocusFirstButton() {
		return true;
	}
	
	@Override
	public boolean shouldFlashButtons() {
		return true;
	}

	@Override
	public boolean showMouseExclusiveButtons() {
		return false;
	}

}
