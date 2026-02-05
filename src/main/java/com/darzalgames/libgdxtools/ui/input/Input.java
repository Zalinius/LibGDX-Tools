package com.darzalgames.libgdxtools.ui.input;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;

public class Input {
	
	protected static final List<Input> values = new ArrayList<>();

	/*----------------------------------------------------------------------------------------------------*/
	public static final Input ACCEPT = new Input(Keys.Z, "ACCEPT");
	public static final Input BACK = new Input(Keys.X, "BACK");

	public static final Input UP = new Input(Keys.UP, "UP");
	public static final Input DOWN = new Input(Keys.DOWN, "DOWN");
	public static final Input LEFT = new Input(Keys.LEFT, "LEFT");
	public static final Input RIGHT = new Input(Keys.RIGHT, "RIGHT");
	
	public static final Input SCROLL_UP = new Input(Keys.PAGE_UP, "PAGE UP");
	public static final Input SCROLL_DOWN = new Input(Keys.PAGE_DOWN, "PAGE DOWN");

	public static final Input PAUSE = new Input(Keys.ESCAPE, "ESCAPE");

	public static final Input TOGGLE_FULLSCREEN = new Input(Keys.F11, "F11");
	
	public static final Input NONE = new Input(-1, "N/A");
	/*----------------------------------------------------------------------------------------------------*/
	
	
	private int key;
	private String actionName;
	
	protected Input(int key, String actionName) {
		this.key = key;
		this.actionName = actionName;
		values.add(this);
	}
	
	/**
	 * @param key The new key to be used by this input (ideal if you want to overwrite ACCEPT or BACK's key in a particular game, for example)
	 */
	public void replaceKey(int key) {
		this.key = key;
	}
	
	public boolean isThisKey(int key) {
		return this.key == key;
	}

	/**
	 * Go from a keyboard key to our custom {@link Input}.<p>
	 * NOTE: If a game defines its own input class that extends this one to add other inputs, this still works great!
	 */
	public static Input getInputFromKey(int key) {
		for (int i = 0; i < values.size(); i++) {
			Input input = values.get(i);
			if (input.key == key) {
				return input;
			}
		}
		return NONE;
	}
	
	@Override
	public String toString() {
		return actionName;
	}
}
