package com.darzalgames.libgdxtools.ui.input;

import com.darzalgames.libgdxtools.i18n.TextSupplier;

/**
 * For the purposes of Quest Giver, this is also the gamepad input strategy.
 * (In a game where we show more control prompts then just directional ones,
 * they'd likely have to be split out into two strategies to show different hints.)
 * @author DarZal
 *
 */
public class KeyboardInputStrategy extends InputStrategy {

	@Override
	public boolean shouldFocusFirstButton() {
		return true;
	}

	@Override
	public String getRosterButtonInputHint() {
		return TextSupplier.getLine("press_button", "→");
	}

	@Override  
	public String getContractButtonInputHint() {
		return TextSupplier.getLine("press_button", "←") + " " + TextSupplier.getLine("twice_prompt");
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
