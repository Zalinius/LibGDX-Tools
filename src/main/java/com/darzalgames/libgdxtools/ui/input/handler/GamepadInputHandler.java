package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.HashMap;
import java.util.Map;

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

	public GamepadInputHandler() {
		inputConsumer = InputPrioritizer.instance;
		buttonStates = new HashMap<>();

		buttonStates.put(Input.ACCEPT, ButtonState.NOT_HELD_DOWN);
		buttonStates.put(Input.BACK, ButtonState.NOT_HELD_DOWN);
		buttonStates.put(Input.PAUSE, ButtonState.NOT_HELD_DOWN);
		buttonStates.put(Input.SKIP, ButtonState.NOT_HELD_DOWN);

		buttonStates.put(Input.UP, ButtonState.NOT_HELD_DOWN);
		buttonStates.put(Input.DOWN, ButtonState.NOT_HELD_DOWN);
		buttonStates.put(Input.LEFT, ButtonState.NOT_HELD_DOWN);
		buttonStates.put(Input.RIGHT, ButtonState.NOT_HELD_DOWN);
	}

	protected final void justPressed(Input buttonKey) {
		latestInputMethod = InputMethod.GAMEPAD;
		inputConsumer.consumeKeyInput(buttonKey);
	}

	protected final void justReleased(Input buttonKey) {
		latestInputMethod = InputMethod.GAMEPAD;
	}
	
	protected final void controllerDisconnected() {
		InputPrioritizer.enterMouseMode();
		InputPrioritizer.pauseIfNeeded();
	}

}
