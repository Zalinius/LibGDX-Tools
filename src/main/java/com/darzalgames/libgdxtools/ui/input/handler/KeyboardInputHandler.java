package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputPrioritizer;

/**
 * @author DarZal
 * This is the single entry point for keyboard input
 */
public class KeyboardInputHandler extends InputHandler {
	
	private final List<Input> keysToAllow;

	public KeyboardInputHandler(InputPrioritizer inputPrioritizer) {
		super();
		
		keysToAllow = new ArrayList<>();
		keysToAllow.addAll(Arrays.asList(Input.values()));
		keysToAllow.remove(Input.NONE);
		

		addListener(new InputListener() {
			@Override
			public boolean keyDown(final InputEvent event, int keycode) {
				Input input = Input.getInputFromKey(keycode);
				if (keycode == Keys.W) {
					input = Input.UP;
				} else if (keycode == Keys.A) {
					input = Input.LEFT;
				} else if (keycode == Keys.S) {
					input = Input.DOWN;
				} else if (keycode == Keys.D) {
					input = Input.RIGHT;
				} else if (keycode == Keys.E || keycode == Keys.CONTROL_RIGHT || keycode == Keys.CONTROL_LEFT) {
					input = Input.ACCEPT;
				} else if (keycode == Keys.Q || keycode == Keys.SHIFT_RIGHT || keycode == Keys.SHIFT_LEFT) {
					input = Input.BACK;
				}
				
				if (keysToAllow.contains(input)) {
					latestInputMethod = InputMethod.KEYBOARD;
					inputPrioritizer.consumeKeyInput(input);	
					return true;
				}
				
				return false;
			}
		});
	}
}
