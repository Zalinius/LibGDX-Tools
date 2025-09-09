package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.HashMap;
import java.util.Map;

import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UniversalButton;

/**
 * Receives and processes input from the various handlers, manages special inputs like ESC to pause and toggling full screen with F11.
 */
public class InputReceiver {

	private final InputPriorityStack inputPriorityStack;
	private final InputStrategySwitcher inputStrategySwitcher;
	private Pause pause;

	private final Map<Input, UniversalButton> specialButtons = new HashMap<>();

	private final Runnable toggleFullscreenRunnable;

	/**
	 * @param toggleFullscreenRunnable A runnable that toggles between full screen and windowed mode
	 */
	public InputReceiver(InputStrategySwitcher inputStrategySwitcher, InputPriorityStack inputPriorityStack, Runnable toggleFullscreenRunnable) {
		this.toggleFullscreenRunnable = toggleFullscreenRunnable;
		this.inputStrategySwitcher = inputStrategySwitcher;

		this.inputPriorityStack = inputPriorityStack;
	}

	void setPause(Pause pause) {
		this.pause = pause;
	}

	/**
	 * @param input The input to pass into the system: this can be from the user (keyboard, controller), or simulated input
	 */
	public void processKeyInput(Input input) {
		boolean shouldToggleFullScreen = input == Input.TOGGLE_FULLSCREEN;
		boolean isScrolling = input == Input.SCROLL_UP || input == Input.SCROLL_DOWN;

		if (shouldToggleFullScreen) {
			toggleFullscreenRunnable.run();
		}else if (input == Input.PAUSE) {
			// Don't try to enter keyboard mode when someone is just pressing escape/pause, simply let the pause system consume the input
			togglePause();
		} else if (isScrolling) {
			inputStrategySwitcher.setToMouseStrategy();
			inputPriorityStack.sendInputToTop(input);
		}
		else if (specialButtons.containsKey(input)) {
			specialButtons.get(input).consumeKeyInput(Input.ACCEPT);
		}
		else if (inputStrategySwitcher.isMouseMode()) {
			inputStrategySwitcher.setToKeyboardAndGamepadStrategy();
		} else {
			inputPriorityStack.sendInputToTop(input);
		}
	}

	public void addSpecialButton(Input input, UniversalButton button) {
		specialButtons.put(input, button);
	}

	private void togglePause() {
		boolean isInGame = !pause.isOptionsMenuOpen();
		if (isInGame) {
			GamePauser.pause();
		} else {
			inputPriorityStack.sendInputToTop(Input.PAUSE);
		}
	}

}
