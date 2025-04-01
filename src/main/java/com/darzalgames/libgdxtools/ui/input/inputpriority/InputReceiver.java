package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.HashMap;
import java.util.Map;

import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

/**
 * Receives and processes input from the various handlers, manages special inputs like ESC to pause and toggling full screen with F11.
 */
public class InputReceiver {

	private InputPriorityStack inputPriorityStack;
	private InputStrategySwitcher inputStrategySwitcher;
	private Pause pause;

	private Map<Input, UniversalButton> specialButtons = new HashMap<>();

	private Runnable toggleFullscreenRunnable;
	private final boolean toggleWithF11;

	/**
	 * @param inputStrategySwitcher
	 * @param inputPriorityStack
	 * @param toggleFullscreenRunnable A runnable that toggles between full screen and windowed mode
	 * @param toggleWithF11 Whether or not the current running version of the game toggles full screen with f11
	 */
	public InputReceiver(InputStrategySwitcher inputStrategySwitcher, InputPriorityStack inputPriorityStack, Runnable toggleFullscreenRunnable, boolean toggleWithF11) {
		this.toggleFullscreenRunnable = toggleFullscreenRunnable;
		this.inputStrategySwitcher = inputStrategySwitcher;
		this.toggleWithF11 = toggleWithF11;

		this.inputPriorityStack = inputPriorityStack;
	}

	void setPause(Pause pause) {
		this.pause = pause;
	}

	/**
	 * @param input The input to pass into the system: this can be from the user (keyboard, controller), or simulated input
	 */
	public void processKeyInput(Input input) {
		boolean shouldToggleFullScreen = input == Input.TOGGLE_FULLSCREEN && toggleWithF11;
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
