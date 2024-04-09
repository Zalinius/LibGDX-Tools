package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

/**
 * This is the entry point for gamepad input, and where we define all button mappings to game actions.
 * In response to button or axis input, mapped input is passed on (simulating keyboard input)
 */
public abstract class GamepadInputHandler extends InputHandler {

	protected enum ButtonState { HELD_DOWN, NOT_HELD_DOWN }

	protected final Map<Input, ButtonState> buttonStates;
	public static final boolean LOG_INPUT = false;
	
	protected abstract List<Input> getTrackedInputs();
	protected abstract Texture getTextureFromDescriptor(AssetDescriptor<Texture> descriptor);

	protected GamepadInputHandler(InputStrategyManager inputStrategyManager) {
		super(inputStrategyManager, InputMethod.GAMEPAD);
		buttonStates = new HashMap<>();
		getTrackedInputs().forEach(input -> buttonStates.put(input, ButtonState.NOT_HELD_DOWN));
	}

	protected final void justPressed(Input buttonKey) {
		if (LOG_INPUT) {
			Gdx.app.log("GamepadInputHandler", "Just pressed:" + buttonKey);
		}
		setLatestInputMethod(this.inputMethod);
		InputPriorityManager.processKeyInput(buttonKey);
	}

	protected final void justReleased(Input buttonKey) {
		if (LOG_INPUT) {
			Gdx.app.log("GamepadInputHandler", "Just released:" + buttonKey);
		}
		setLatestInputMethod(this.inputMethod);
	}
	
	protected final void controllerDisconnected() {
		inputStrategyManager.setToMouseStrategy();
		InputPriorityManager.pauseIfNeeded();
	}

}
