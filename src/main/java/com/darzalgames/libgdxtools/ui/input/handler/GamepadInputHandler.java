package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.InputPrioritizer;

/**
 * This is the entry point for gamepad input, and where we define all button mappings to game actions.
 * In response to button or axis input, mapped input is passed on (simulating keyboard input)
 */
public abstract class GamepadInputHandler extends InputHandler {

	protected enum ButtonState { HELD_DOWN, NOT_HELD_DOWN }

	private final InputConsumer inputConsumer;
	protected final Map<Input, ButtonState> buttonStates;
	private static final boolean LOG_INPUT = false;
	
	protected abstract List<Input> getTrackedInputs();

	protected GamepadInputHandler() {
		inputConsumer = InputPrioritizer.instance;
		buttonStates = new EnumMap<>(Input.class);
		getTrackedInputs().forEach(input -> buttonStates.put(input, ButtonState.NOT_HELD_DOWN));
	}

	protected final void justPressed(Input buttonKey) {
		if (LOG_INPUT) {
			Gdx.app.log("GamepadInputHandler", "Just pressed:" + buttonKey);
		}
		setLatestInputMethod(InputMethod.GAMEPAD);
		inputConsumer.consumeKeyInput(buttonKey);
	}

	protected final void justReleased(Input buttonKey) {
		if (LOG_INPUT) {
			Gdx.app.log("GamepadInputHandler", "Just released:" + buttonKey);
		}
		setLatestInputMethod(InputMethod.GAMEPAD);
	}
	
	protected final void controllerDisconnected() {
		InputPrioritizer.enterMouseMode();
		InputPrioritizer.pauseIfNeeded();
	}

}