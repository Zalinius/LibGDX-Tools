package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.Input.Keys;

public enum Input {

	ACCEPT(Keys.Z),
	BACK(Keys.X),

	UP(Keys.UP),
	DOWN(Keys.DOWN),
	LEFT(Keys.LEFT),
	RIGHT(Keys.RIGHT),

	PAUSE(Keys.ESCAPE),
	SKIP(Keys.ENTER),

	TOGGLE_FULLSCREEN(Keys.F11),
	
	NONE(-1),
	;

	private final int key;

	private Input(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}
	
	public static Input getInputFromKey(int key) {
		for (int i = 0; i < Input.values().length; i++) {
			Input input = Input.values()[i];
			if (input.key == key) {
				return input;
			}
		}
		return NONE;
	}
}
