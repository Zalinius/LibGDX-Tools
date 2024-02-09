package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

/**
 * @author DarZal
 * This is the single entry point for keyboard input
 */
public abstract class KeyboardInputHandler extends InputHandler {
	
	private final List<Input> keysToAllow;

	/**
	 * Sets up the listener for keyboard input, and maps various keybindings to others
	 */
	protected KeyboardInputHandler(InputStrategyManager inputStrategyManager) {
		super(inputStrategyManager);
		
		keysToAllow = getKeyWhitelist();

		addListener(new InputListener() {
			@Override
			public boolean keyDown(final InputEvent event, int keycode) {
				Input input = getInputFromKey(keycode);
				input = remapInputIfNecessary(input, keycode);
				
				if (keysToAllow.contains(input)) {
					setLatestInputMethod(InputMethod.KEYBOARD);
					InputPriorityManager.processKeyInput(input);	
					return true;
				}
				
				return false;
			}
		});
	}
	
	protected abstract Input getInputFromKey(int keycode);
	protected abstract Input remapInputIfNecessary(Input input, int keycode);
	protected abstract List<Input> getKeyWhitelist();
}
