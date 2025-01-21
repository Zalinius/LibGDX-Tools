package com.darzalgames.libgdxtools.ui.input.universaluserinput.inputpriority;

import java.util.*;

import com.badlogic.gdx.scenes.scene2d.*;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;
import com.darzalgames.libgdxtools.ui.optionsmenu.OptionsMenu;

/**
 * The amazing ultra big class that handles the input stack, handling which {@link InputConsumer}
 * receives input, manages popups and the pause menu, an toggling full screen (f11).
 * @author DarZal
 *
 */
public class InputPriorityManager implements InputObserver {

	private static Deque<InputConsumer> inputConsumerStack = new ArrayDeque<>();
	private static DarkScreen darkScreen;

	private static UniversalButton pauseButton;
	private static OptionsMenu optionsMenu;
	private static Map<Input, UniversalButton> specialButtons = new HashMap<>();

	private static Stage popUpStage;

	private static final Group group = new Group();
	private static Runnable toggleFullscreenRunnable;

	private static GamepadInputHandler gamepadInputHandler;
	private static KeyboardInputHandler keyboardInputHandler;
	private static InputStrategySwitcher inputStrategyManager;

	private static ScrollingManager scrollingManager;

	private static boolean toggleWithF11 = true;

	private InputPriorityManager() {
		inputStrategyManager.register(this);
	}

	/**
	 * It's essential to do this, there'll probably be a crash if you don't \_( ͡⟃ ͜ʖ ⟄)_/
	 * @param mainStage The stage where the game is  
	 * @param popUpStage The stage where pop ups are held, ensuring that they are in front of the rest of the game
	 * @param toggleFullscreenRunnable A runnable that toggles between full screen and windowed mode
	 * @param gamepadInputHandler The gamepad input handler to use (fallback versus Steam)
	 * @param keyboardInputHandler The keyboard input handler to use
	 */
	public static void initialize(Stage mainStage, Stage popUpStage, Runnable toggleFullscreenRunnable,
			GamepadInputHandler gamepadInputHandler, KeyboardInputHandler keyboardInputHandler, InputStrategySwitcher inputStrategyManager,
			boolean toggleWithF11) {
		InputPriorityManager.popUpStage = popUpStage;
		InputPriorityManager.toggleFullscreenRunnable = toggleFullscreenRunnable;
		InputPriorityManager.gamepadInputHandler = gamepadInputHandler;
		InputPriorityManager.keyboardInputHandler = keyboardInputHandler;
		InputPriorityManager.inputStrategyManager = inputStrategyManager;
		InputPriorityManager.toggleWithF11 = toggleWithF11;

		InputPriorityManager.scrollingManager = new ScrollingManager(InputPriorityManager::processScrollingInput);

		clearStackAndAddBlankConsumer();

		// Set up the dark background screen that goes behind popups
		darkScreen = new DarkScreen(popUpStage, () -> {
			inputConsumerStack.peek().consumeKeyInput(Input.BACK); 
		});

		// Add the inner group to the stage
		mainStage.addActor(group);
		mainStage.setKeyboardFocus(keyboardInputHandler);
		group.addActor(gamepadInputHandler);
	}

	/**
	 * Used to completely clear the current input receivers (useful when changing game screens
	 * to make sure any stray actors are TOTALLY cleared)
	 */
	public static void clearChildren() {
		group.clearChildren(false);
		clearStackAndAddBlankConsumer();
		group.addActor(gamepadInputHandler);
		group.addActor(keyboardInputHandler);
		darkScreen.remove();

		popUpStage.clear();
		popUpStage.addActor(pauseButton.getView());
	}

	/**
	 * @param input The input to pass into the system: this can be from the user (keyboard, controller), or simulated input
	 */
	public static void processKeyInput(Input input) {
		boolean shouldToggleFullScreen = input == Input.TOGGLE_FULLSCREEN && toggleWithF11;
		if (shouldToggleFullScreen) {
			toggleFullscreenRunnable.run();
		} else if (input == Input.PAUSE) {
			if (pauseButton != null && 
					(!isPaused() // if we're not paused, then we're in game and should open the pause menu
							|| !checkIfLandingOnPopup())) { // we're on the pause menu (and not in a nested pop up, like the quit warning)
				pauseButton.consumeKeyInput(Input.ACCEPT);
			} else { // Don't try to enter keyboard mode when someone is just pressing escape
				inputConsumerStack.peek().consumeKeyInput(input);
			}	
		} else if (specialButtons.containsKey(input)) {
			specialButtons.get(input).consumeKeyInput(Input.ACCEPT);
		}
		else if (inputStrategyManager.showMouseExclusiveUI()) {
			inputStrategyManager.setToKeyboardStrategy();
		} else {
			inputConsumerStack.peek().consumeKeyInput(input);
		}
	}

	/**
	 * A pop up should request this when claiming input priority, since they're handled a bit differently
	 * @param <A>
	 * @param popup
	 */
	public static <A extends Actor & PopUp> void showPopup(A popup) {
		darkScreen.remove();
		popUpStage.addActor(popup);
		popup.toFront();
		pauseButton.getView().toFront();
		popup.addBackClickListenerIfCanDismiss();
		darkScreen.fadeIn(popup.getZIndex(), popup.canDismiss());
	}


	/**
	 * @param inputConsumer The thing to be put at the top of the input stack
	 */
	public static void claimPriority(InputConsumer inputConsumer) {
		if (!inputConsumer.equals(inputConsumerStack.peek())) {
			unFocusTop(inputConsumerStack.peek());
			inputConsumerStack.push(inputConsumer);
			focusTop(true);
		}
	}

	/**
	 * @param inputConsumer The thing that wants to release its priority, this only works if the requester is currently at the top of the stack
	 */
	public static void releasePriority(InputConsumer inputConsumer) {
		if (inputConsumer.equals(inputConsumerStack.peek())) {
			darkScreen.fadeOutAndRemove();
			boolean isClosingPauseMenu = inputConsumerStack.peek() == optionsMenu; 
			removeTop();
			if (isClosingPauseMenu) {
				inputConsumerStack.peek().setTouchable(Touchable.enabled);
				focusCurrent();
			} else {
				focusTop(false);
			}

		}
	}

	/**
	 * @param optionsMenu The menu to show when the pause button is pressed.
	 * This might be different depending on if you're on the main menu or in the game.
	 */
	public static void setPauseUI(OptionsMenu optionsMenu) {
		clearPauseButtonUI();
		pauseButton = optionsMenu.getButton();
		popUpStage.addActor(pauseButton.getView());
		InputPriorityManager.optionsMenu = optionsMenu;
	}

	public static boolean isPaused() {
		return (optionsMenu != null && optionsMenu.getStage() != null)
				|| inputConsumerStack.peek().isGamePausedWhileThisIsInFocus();
	}

	/**
	 * Will show the pause menu if the game is not already paused
	 */
	public static void pauseIfNeeded() {
		if (!isPaused()) {
			showPauseMenu();
		}
	}

	/**
	 * To be used in exceptional circumstances only (e.g. better visuals for the trailer)
	 */
	public static void hideDarkScreenForVeryExceptionalCircumstances() {
		darkScreen.remove();
	}

	public static void addSpecialButton(Input input, UniversalButton button) {
		specialButtons.put(input, button);
	}

	//-----------------------------------------------------------

	private static void focusTop(boolean isFirstFocus) {
		inputConsumerStack.peek().setTouchable(Touchable.enabled);
		if (isFirstFocus) {
			inputConsumerStack.peek().gainFocus();				
		} else {
			inputConsumerStack.peek().regainFocus();
		}
		focusCurrent();
	}

	private static void unFocusTop(InputConsumer top) {
		top.setTouchable(Touchable.disabled);
		top.loseFocus();
	}

	private static void removeTop() {
		unFocusTop(inputConsumerStack.pop());
		darkScreen.fadeOutAndRemove();
		checkIfLandingOnPopup();	
	}

	// Comparing Actors with InputConsumers, it ain't pretty but it works.
	// I wish that Actor were an interface so that InputConsumer could extend it, but alas.
	@SuppressWarnings("unlikely-arg-type")
	private static boolean checkIfLandingOnPopup() {
		if (popUpStage.getRoot().getChildren().size > 0) {
			Actor[] popups = popUpStage.getRoot().getChildren().toArray();
			Optional<Actor> popupMatch = Arrays.stream(popups).filter(a -> a.equals(inputConsumerStack.peek())).findFirst(); 
			if (popupMatch.isPresent()) {
				// We are landing back on to a popup
				Actor actor = popupMatch.get();
				PopUp popUp = (PopUp)actor;
				darkScreen.fadeIn(actor.getZIndex(), popUp.canDismiss());
				return true;
			}
		}
		return false;
	}

	private static void showPauseMenu() {
		if (optionsMenu != null) {
			showPopup(optionsMenu);
			claimPriority(optionsMenu);	
		}
	}

	private static void focusCurrent() {
		inputConsumerStack.peek().focusCurrent();
	}

	private static void clearSelected() {
		inputConsumerStack.peek().clearSelected();
	}

	private static void clearPauseButtonUI() {
		if (pauseButton != null) {
			pauseButton.getView().remove();
			pauseButton = null;
			optionsMenu = null;
		}
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategyManager) {
		if (inputStrategyManager.shouldFlashButtons()) {
			inputConsumerStack.peek().selectDefault();
		} else { // Mouse mode
			clearSelected();
		}
	}

	@Override
	public boolean shouldBeUnregistered() {
		return false;
	}

	private static void clearStackAndAddBlankConsumer() {
		inputConsumerStack.clear();
		inputConsumerStack.add(new InputConsumer() {
			@Override public void consumeKeyInput(Input input) {/*not needed*/}
			@Override public void setTouchable(Touchable isTouchable)  {/*not needed*/}
			@Override public void focusCurrent()  {/*not needed*/}
			@Override public void clearSelected()  {/*not needed*/}
			@Override public void selectDefault()  {/*not needed*/}
			@Override public void loseFocus() { System.err.println("The base object of the input stack has lost focus! All is lost!"); }
		});
	}

	public static void receiveScrollInput(float amountY) {
		scrollingManager.receiveScrollInput(amountY);
	}

	private static void processScrollingInput(Input input) {
		inputStrategyManager.setToMouseStrategy();
		inputConsumerStack.peek().consumeKeyInput(input);
	}

}
