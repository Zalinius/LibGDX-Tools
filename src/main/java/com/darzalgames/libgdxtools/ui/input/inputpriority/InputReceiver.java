package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.HashMap;
import java.util.Map;

import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

/**
 * Handles the input stack, handling which {@link InputConsumer}
 * receives input, manages popups and the pause menu, an toggling full screen (f11).
 */
public class InputReceiver {

	private InputPriorityStack inputPriorityStack;
	private InputStrategySwitcher inputStrategySwitcher;
	private Pause pause;

	private Map<Input, UniversalButton> specialButtons = new HashMap<>();

	private Runnable toggleFullscreenRunnable;
	private final boolean toggleWithF11;

	/**
	 * @param popUpStage The stage where pop ups are held, ensuring that they are in front of the rest of the game
	 * @param toggleFullscreenRunnable A runnable that toggles between full screen and windowed mode
	 * @param inputStrategySwitcher
	 * @param toggleWithF11 Whether or not the current running version of the game toggles full screen with f11
	 */
	public InputReceiver(Runnable toggleFullscreenRunnable, InputStrategySwitcher inputStrategySwitcher, boolean toggleWithF11, InputPriorityStack inputPriorityStack) {
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
		if (shouldToggleFullScreen) {
			toggleFullscreenRunnable.run();
		} else if (input == Input.PAUSE) {
			boolean isInGame = !pause.isPaused(); // if we're not paused, then we're in game and should open the pause menu
			boolean isOnBasePauseMenuAndNotNestedMenu = !isInGame && !inputPriorityStack.isLandingOnPopup(); // we're on the pause menu (and not in a nested pop up, like the quit warning)
			// TODO Does the above condition even work? the pause menu is also a popup...
			if (isInGame || isOnBasePauseMenuAndNotNestedMenu) { 
				pause.pressPauseButton();
			} else { // Don't try to enter keyboard mode when someone is just pressing escape/pause
				inputPriorityStack.sendInputToTop(input);
			}	
		} else if (specialButtons.containsKey(input)) {
			specialButtons.get(input).consumeKeyInput(Input.ACCEPT);
		}
		else if (inputStrategySwitcher.showMouseExclusiveUI()) {
			inputStrategySwitcher.setToKeyboardStrategy();
		} else {
			inputPriorityStack.sendInputToTop(input);
		}
	}

	public void addSpecialButton(Input input, UniversalButton button) {
		specialButtons.put(input, button);
	}

	public void processScrollingInput(Input input) {
		inputStrategySwitcher.setToMouseStrategy();
		inputPriorityStack.sendInputToTop(input);
	}

}
