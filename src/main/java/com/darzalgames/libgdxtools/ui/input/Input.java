package com.darzalgames.libgdxtools.ui.input;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;

public class Input {
	
	protected static final List<Input> values = new ArrayList<>();

	/*----------------------------------------------------------------------------------------------------*/
	public static final Input ACCEPT = new Input(Keys.Z);
	public static final Input BACK = new Input(Keys.X);

	public static final Input UP = new Input(Keys.UP);
	public static final Input DOWN = new Input(Keys.DOWN);
	public static final Input LEFT = new Input(Keys.LEFT);
	public static final Input RIGHT = new Input(Keys.RIGHT);
	
	public static final Input SCROLL_UP = new Input(Keys.PAGE_UP);
	public static final Input SCROLL_DOWN = new Input(Keys.PAGE_DOWN);

	public static final Input PAUSE = new Input(Keys.ESCAPE);

	public static final Input TOGGLE_FULLSCREEN = new Input(Keys.F11);
	
	public static final Input NONE = new Input(-1);
	/*----------------------------------------------------------------------------------------------------*/
	
	
	private int key;
	
	protected Input(int key) {
		this.key = key;
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
	 * @param key
	 * @return
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
}
