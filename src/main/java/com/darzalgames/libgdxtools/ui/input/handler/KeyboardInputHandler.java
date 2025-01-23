package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputReceiver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

/**
 * @author DarZal
 * This is the single entry point for keyboard input
 */
public abstract class KeyboardInputHandler extends InputHandler {
	
	private final List<Input> keysToAllow;
	protected final Map<Input, AssetDescriptor<Texture>> buttonMappings;

	/**
	 * Sets up the listener for keyboard input, and maps various keybindings to others
	 */
	protected KeyboardInputHandler(InputStrategySwitcher inputStrategySwitcher, InputReceiver inputReceiver) {
		super(inputStrategySwitcher);
		
		keysToAllow = getKeyWhitelist();
		buttonMappings = makeButtonMappings();

		addListener(new InputListener() {
			@Override
			public boolean keyDown(final InputEvent event, int keycode) {
				Input input = Input.getInputFromKey(keycode);
				input = remapInputIfNecessary(input, keycode);
				
				if (keysToAllow.contains(input)) {
					updateLatestInputMethod();
					inputReceiver.processKeyInput(input);
					return true;
				}
				
				return false;
			}
		});
	}
	
	protected abstract Input remapInputIfNecessary(Input input, int keycode);
	protected abstract List<Input> getKeyWhitelist();
	protected abstract Map<Input, AssetDescriptor<Texture>> makeButtonMappings();
}
