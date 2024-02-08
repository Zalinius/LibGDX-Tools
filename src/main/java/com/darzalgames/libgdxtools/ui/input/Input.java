package com.darzalgames.libgdxtools.ui.input;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;

public class Input {

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

	protected int key;

	protected Input(int key) {
		this.key = key;
	}
	
	/**
	 * @param key The new key to be used by this input (ideal if you want to overwrite ACCEPT or BACK's key in a particular game, for example)
	 */
	public void replaceKey(int key) {
		this.key = key;
	}

	/**
	 * Go from a keyboard key to our custom {@link Input}.<p>
	 * NOTE: If a game defines its own input class that extends this one, it should also
	 * make a method similar to this (referred to Quest Giver's InputQG) and use that one
	 * in its {@link KeyboardInputHandler}
	 * @param key
	 * @return
	 */
	public static Input getToolsInputFromKey(int key) {
		for (int i = 0; i < Input.values().size(); i++) {
			Input input = Input.values().get(i);
			if (input.key == key) {
				return input;
			}
		}
		return NONE;
	}
	
	private static List<Input> values() {
		List<Input> values = new ArrayList<>();
		Field[] fields = ClassReflection.getFields(Input.class);
		for (Field field : fields) {
			if(field.getType().equals(Input.class)) {
				Input inputValue = null;
				try {
					inputValue = (Input)field.get(null);
				} catch (ReflectionException e) {
					e.printStackTrace();
				}
				values.add(inputValue);
			}
		}
		return values;
	}
}
