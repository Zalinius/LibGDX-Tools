package com.darzalgames.libgdxtools.ui.input.strategy;

import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class InputStrategyConcrete extends Actor implements InputStrategy {

	private final Map<String, String> buttonHints;
	
	// TODO I suppose at some point the second half of this map will be glyphs?
	protected InputStrategyConcrete(Map<String, String> buttonHints) {
		this.buttonHints = buttonHints;
	}
	
	@Override
	public final String getButtonInputHint(String hintKey) {
		return buttonHints.get(hintKey);
	}
}
