package com.darzalgames.libgdxtools.ui.input;

import java.util.*;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantSequenceAction;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;
import com.darzalgames.libgdxtools.ui.optionsmenu.OptionsMenu;

/**
 * The amazing ultra big class that handles the input stack, handling which {@link InputConsumer}
 * receives input, manages popups and the pause menu, an toggling full screen (f11).
 * @author DarZal
 *
 */
public class InputPriorityManager {

	private static Deque<InputConsumer> inputConsumerStack = new ArrayDeque<>();
	private static Image darkScreen;

	private static KeyboardButton pauseButton;
	private static OptionsMenu optionsMenu;
	private static Map<Input, KeyboardButton> specialButtons = new HashMap<>();

	private static Stage popUpStage;

	private static final Group group = new Group();
	private static Runnable toggleFullscreenRunnable;

	private static GamepadInputHandler gamepadInputHandler;
	private static KeyboardInputHandler keyboardInputHandler;
	private static InputStrategyManager inputStrategyManager;

	private static boolean toggleWithF11 = true;

	private InputPriorityManager() {}

	/**
	 * It's essential to do this, there'll probably be a crash if you don't \_( ͡⟃ ͜ʖ ⟄)_/
	 * @param mainStage The stage where the game is  
	 * @param popUpStage The stage where pop ups are held, ensuring that they are in front of the rest of the game
	 * @param toggleFullscreenRunnable A runnable that toggles between full screen and windowed mode
	 * @param gamepadInputHandler The gamepad input handler to use (fallback versus Steam)
	 * @param keyboardInputHandler The keyboard input handler to use
	 */
	public static void initialize(Stage mainStage, Stage popUpStage, Runnable toggleFullscreenRunnable,
			GamepadInputHandler gamepadInputHandler, KeyboardInputHandler keyboardInputHandler, InputStrategyManager inputStrategyManager) {
		InputPriorityManager.popUpStage = popUpStage;
		InputPriorityManager.toggleFullscreenRunnable = toggleFullscreenRunnable;
		InputPriorityManager.gamepadInputHandler = gamepadInputHandler;
		InputPriorityManager.keyboardInputHandler = keyboardInputHandler;
		InputPriorityManager.inputStrategyManager = inputStrategyManager;

		inputConsumerStack.add((InputConsumerWrapper) isTouchable -> {/*not needed*/});

		// Set up the dark background screen that goes behind popups
		darkScreen = new Image(ColorTools.getColoredTexture(new Color(0, 0, 0, 0.5f), GameInfo.getWidth(), GameInfo.getHeight()));
		darkScreen.addListener(new InputListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				hideDarkScreen();
				if (!inputConsumerStack.isEmpty()) {
					inputConsumerStack.peek().consumeKeyInput(Input.BACK); 
				}
				return true;
			}
		});

		// Add the inner group to the stage
		mainStage.addActor(group);
		mainStage.setKeyboardFocus(keyboardInputHandler);
		group.addActor(gamepadInputHandler);

		group.addAction(Actions.forever(new Action() {

			@Override
			public boolean act(float delta) {
				InputPriorityManager.timeSinceScroll += delta;
				return false;
			}
		}));


		// Enter the default strategy (mouse) during this initialization
		inputStrategyManager.setToMouseStrategy();
		inputStrategyManager.register(new InputChangeObserver());
	}

	/**
	 * Used to completely clear the current input receivers (useful when changing game screens
	 * to make sure any stray actors are TOTALLY cleared)
	 */
	public static void clearChildren() {
		group.clearChildren(false);
		inputConsumerStack.clear();
		inputConsumerStack.add((InputConsumerWrapper) isTouchable -> {/*not needed*/});
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
		if (input == Input.TOGGLE_FULLSCREEN && toggleWithF11) {
			toggleFullscreenRunnable.run();
		} else if (input == Input.PAUSE) {
			if (pauseButton != null && 
					(!isPaused() // if we're not paused, then we're in game and should open the pause menu
							|| !checkIfLandingOnPopup())) { // we're on the pause menu (and not in a nested pop up, like the quit warning)
				pauseButton.consumeKeyInput(Input.ACCEPT);
			} else if (!inputConsumerStack.isEmpty()) { // Don't try to enter keyboard mode when someone is just pressing escape
				inputConsumerStack.peek().consumeKeyInput(input);
			}	
		} else if (specialButtons.containsKey(input)) {
			specialButtons.get(input).consumeKeyInput(Input.ACCEPT);
		}
		else if (!inputConsumerStack.isEmpty() && !inputStrategyManager.setToKeyboardStrategy()) {
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
		if (pauseButton != null) {
			pauseButton.getView().toFront();
		}
		ClickListener rightClickBack = new ClickListener(Buttons.RIGHT) {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (inputConsumerStack.peek().equals(popup)) {
					popup.removeListener(this);
					popup.consumeKeyInput(Input.BACK);
				}
			}
		};
		if (popup.canDismiss()) {
			popup.addListener(rightClickBack);
		}
		InputPriorityManager.showDarkScreen(popup.getZIndex(), popup.canDismiss());
	}

	private static float timeSinceScroll = 0;
	private static boolean hasFinishedScrolling = true;
	/**
	 * This can handle scrolling both from a mouse wheel or something more like a tablet or touchpad.
	 * @param amount The amount of scrolling on the y-axis
	 */
	public static void receiveScrollInput(float amount) {
		// It seems the mouse wheel returns either 1 or -1, and a tablet returns any value between these two. 
		// So, I'm using a threshold of 0.1f for the tablet/touchpad, and an input delay of 0.15f
		if (!inputConsumerStack.isEmpty()) {
			if (Math.abs(amount) < 0.1f || timeSinceScroll > 0.15f) {
				hasFinishedScrolling = true;
			}

			if (Math.abs(amount) > 0.1f && hasFinishedScrolling) {
				timeSinceScroll = 0;
				inputStrategyManager.setToMouseStrategy();
				inputConsumerStack.peek().consumeKeyInput(amount < 0 ? Input.SCROLL_UP : Input.SCROLL_DOWN);
				hasFinishedScrolling = false;
			}
		}
	}

	/**
	 * @param inputConsumer The thing to be put at the top of the input stack
	 */
	public static void claimPriority(InputConsumer inputConsumer) {
		if (inputConsumerStack.isEmpty() || !inputConsumer.equals(inputConsumerStack.peek())) {
			unFocusTop(inputConsumerStack.peek());
			inputConsumerStack.push(inputConsumer);
			focusTop(true);
		}
	}

	/**
	 * @param inputConsumer The thing that wants to release its priority, this only works if the requester is currently at the top of the stack
	 */
	public static void releasePriority(InputConsumer inputConsumer) {
		if (!inputConsumerStack.isEmpty() && inputConsumer.equals(inputConsumerStack.peek())) {
			hideDarkScreen();
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

	private static void inputStrategyChanged(InputStrategyManager inputStrategyManager) {
		if (!inputConsumerStack.isEmpty()) {
			if (inputStrategyManager.shouldFlashButtons()) {
				if (!inputConsumerStack.isEmpty()) {
					inputConsumerStack.peek().selectDefault();
				}
			}  else { // Mouse mode
				clearSelected();
			}
		}
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
	
	public static void addSpecialButton(Input input, KeyboardButton button) {
		specialButtons.put(input, button);
	}

	//-----------------------------------------------------------

	private static void focusTop(boolean isFirstFocus) {
		if (!inputConsumerStack.isEmpty()) {
			inputConsumerStack.peek().setTouchable(Touchable.enabled);
			if (isFirstFocus) {
				inputConsumerStack.peek().gainFocus();				
			} else {
				inputConsumerStack.peek().regainFocus();
			}
			focusCurrent();
		}
	}

	private static void unFocusTop(InputConsumer top) {
		if (!inputConsumerStack.isEmpty()) {
			top.setTouchable(Touchable.disabled);
			top.loseFocus();
		}
	}

	private static void removeTop() {
		if (!inputConsumerStack.isEmpty()) {
			unFocusTop(inputConsumerStack.pop());
			hideDarkScreen();
			checkIfLandingOnPopup();	
		}
	}

	// Comparing Actors with InputConsumers, it ain't pretty but it works.
	// I wish that Actor were an interface so that InputConsumer could extend it, but alas.
	@SuppressWarnings("unlikely-arg-type")
	private static boolean checkIfLandingOnPopup() {
		if (popUpStage.getRoot().getChildren().size > 0) {
			List<Actor> popups = new ArrayList<>();
			SnapshotArray<Actor> inferiorArray = popUpStage.getRoot().getChildren();
			for (int i = 0; i < inferiorArray.size; i++) {
				popups.add(inferiorArray.get(i));
			}
			Optional<Actor> popupMatch = popups.stream().filter(a -> a.equals(inputConsumerStack.peek())).findFirst(); 
			if (popupMatch.isPresent()) {
				// We are landing back on to a popup
				Actor actor = popupMatch.get();
				PopUp popUp = (PopUp)actor;
				showDarkScreen(actor.getZIndex(), popUp.canDismiss());
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

	private static void showDarkScreen(int actorIndex, boolean isTouchable) {
		darkScreen.clearActions();
		popUpStage.addActor(darkScreen);
		darkScreen.setZIndex(actorIndex);
		// They used to be a 1 second delay here before setting the dark screen touchable, I'm not sure why.
		darkScreen.setTouchable(isTouchable ? Touchable.enabled : Touchable.disabled);
		darkScreen.addAction(Actions.fadeIn(0.25f, Interpolation.circle));
	}


	private static void hideDarkScreen() {
		darkScreen.clearActions();
		darkScreen.addAction(new InstantSequenceAction(
				Actions.fadeOut(0.25f, Interpolation.circle),
				Actions.removeActor(darkScreen)
				));
	}

	private static void focusCurrent() {
		if (!inputConsumerStack.isEmpty()) {
			inputConsumerStack.peek().focusCurrent();
		}
	}

	private static void clearSelected() {
		if (!inputConsumerStack.isEmpty()) {
			inputConsumerStack.peek().clearSelected();
		}
	}

	private static void clearPauseButtonUI() {
		if (pauseButton != null) {
			pauseButton.getView().remove();
			pauseButton = null;
			optionsMenu = null;
		}
	}

	public static void setToggleWithF11(boolean toggleWithF11) {
		InputPriorityManager.toggleWithF11 = toggleWithF11;
	}


	private static class InputChangeObserver implements InputObserver {

		@Override
		public void inputStrategyChanged(InputStrategyManager inputStrategyManager) {
			InputPriorityManager.inputStrategyChanged(inputStrategyManager);
		}

		@Override
		public boolean shouldBeUnregistered() {
			return false;
		}

	}

}
