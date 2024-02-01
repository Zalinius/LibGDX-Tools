package com.darzalgames.libgdxtools.ui.input;

import java.util.*;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.MainGame;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.optionsmenu.OptionsMenu;

/**
 * The amazing ultra big class that handles the input stack, handling which {@link InputConsumer}
 * receives input, manages popups and the pause menu, an toggling full screen (f11).
 * @author DarZal
 *
 */
public class InputPriorityManager {

	private static Deque<InputConsumer> inputConsumerStack;
	private static Image darkScreen;
	
	private static KeyboardButton pauseButton;
	private static OptionsMenu optionsMenu;
	
	private static Stage popUpStage;
	
	private static final Group group = new Group();
	private static Runnable toggleFullscreenRunnable;

	private static GamepadInputHandler gamepadInputHandler;
	private static KeyboardInputHandler keyboardInputHandler;

	/**
	 * Is essential to do this, there'll probably be a crash if you don't \_( ͡⟃ ͜ʖ ⟄)_/
	 */
	public static void initialize() {
		inputConsumerStack = new ArrayDeque<>();
		Pixmap background = new Pixmap(MainGame.getWidth(), MainGame.getHeight(), Format.RGBA8888);
		Color color = Color.BLACK;
		background.setColor(color.r, color.g, color.b, 0.5f);
		background.fillRectangle(0, 0, background.getWidth(), background.getHeight());
		darkScreen = new Image(new Texture(background));
		darkScreen.setBounds(0, 0, background.getWidth(), background.getHeight());
		background.dispose();
		darkScreen.addListener(new InputListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				darkScreen.remove();
				if (!inputConsumerStack.isEmpty()) {
					inputConsumerStack.peek().consumeKeyInput(Input.BACK); 
				}
				return true;
			}
		});
	}

	/**
	 * @param gamepadInputHandler The gamepad input handler to use (fallback versus Steam)
	 */
	public static void setGamepadInputHandler(GamepadInputHandler gamepadInputHandler, List<DoesNotPause> actorsThatDoNotPause) {
		InputPriorityManager.gamepadInputHandler = gamepadInputHandler;
		group.addActor(gamepadInputHandler);
		actorsThatDoNotPause.add(gamepadInputHandler);
	}

	/**
	 * Used to completely clear the current input receivers (useful when changing game screens
	 * to make sure any stray actors are TOTALLY cleared)
	 */
	public static void clearChildren() {
		group.clearChildren(false);
		inputConsumerStack.clear();
		group.addActor(gamepadInputHandler);
		group.addActor(keyboardInputHandler);
		darkScreen.remove();
	}

	public static void enterMouseMode() {
		MainGame.getInputStrategyManager().setToMouseStrategy();
	}

	public static boolean enterKeyboardMode() {
		return MainGame.getInputStrategyManager().setToKeyboardStrategy();
	}

	/**
	 * @param input The input to pass into the system: this can be from the user (keyboard, controller), or simulated input
	 */
	public static void processKeyInput(Input input) {
		if (input == Input.TOGGLE_FULLSCREEN) {
			toggleFullscreenRunnable.run();
		} else if (input == Input.PAUSE) {
			if (pauseButton != null && 
					(!isPaused() // if we're not paused, then we're in game and should open the pause menu
							|| !checkIfLandingOnPopup())) { // we're on the pause menu (and not in a nested pop up, like the quit warning)
				pauseButton.consumeKeyInput(Input.ACCEPT);
			} else if (!inputConsumerStack.isEmpty()) { // Don't try to enter keyboard mode when someone is just pressing escape
				inputConsumerStack.peek().consumeKeyInput(input);
			}	
		} else if (!enterKeyboardMode() && !inputConsumerStack.isEmpty()) {
			inputConsumerStack.peek().consumeKeyInput(input);
		}		
	}

	private static void showDarkScreen(int actorIndex, boolean isTouchable) {
		popUpStage.addActor(darkScreen);
		darkScreen.setZIndex(actorIndex);
		darkScreen.setTouchable(Touchable.disabled);
		DelayAction delayThenTouchable = new DelayAction(1f);
		delayThenTouchable.setAction(new RunnableActionBest(() -> 
			darkScreen.setTouchable(isTouchable ? Touchable.enabled : Touchable.disabled)
		));
		darkScreen.addAction(delayThenTouchable);
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
				popup.removeListener(this);
				popup.consumeKeyInput(Input.BACK);
			}
		};
		if (popup.canDismiss()) {
			popup.addListener(rightClickBack);
		}
		InputPriorityManager.showDarkScreen(popup.getZIndex(), popup.canDismiss());
	}

	private static void showPauseMenu() {
		showPopup(optionsMenu);
		claimPriority(optionsMenu);
	}

	/**
	 * @param inputConsumer The thing to be put at the top of the input stack
	 */
	public static void claimPriority(InputConsumer inputConsumer) {
		if (inputConsumerStack.isEmpty() || !inputConsumer.equals(inputConsumerStack.peek())) {
			unFocusTop();
			inputConsumerStack.push(inputConsumer);
			focusTop(true);
		}
	}

	/**
	 * @param inputConsumer The thing that wants to release its priority, this only works if the requester is currently at the top of the stack
	 */
	public static void releasePriority(InputConsumer inputConsumer) {
		if (!inputConsumerStack.isEmpty() && inputConsumer.equals(inputConsumerStack.peek())) {
			darkScreen.remove();
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

	private static void unFocusTop() {
		if (!inputConsumerStack.isEmpty()) {
			inputConsumerStack.peek().setTouchable(Touchable.disabled);
			inputConsumerStack.peek().loseFocus();
		}
	}

	private static void removeTop() {
		if (!inputConsumerStack.isEmpty()) {
			unFocusTop();
			inputConsumerStack.pop();
			darkScreen.remove();
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

	/**
	 * @param optionsMenu The menu to show when the pause button is pressed
	 */
	public static void setPauseUI(OptionsMenu optionsMenu) {
		clearPauseButtonUI();
		pauseButton = optionsMenu.getButton();
		popUpStage.addActor(pauseButton.getView());
		InputPriorityManager.optionsMenu = optionsMenu;
	}

	public static void setKeyboardInputHandler(KeyboardInputHandler keyboardInputHandler) {
		InputPriorityManager.keyboardInputHandler = keyboardInputHandler;
	}

	/**
	 * @param stage The stage where pop ups are held, insuring that they are in front of the rest of the game
	 */
	public static void setPopUpStage(Stage stage) {
		InputPriorityManager.popUpStage = stage;
	}

	private static void clearPauseButtonUI() {
		if (pauseButton != null) {
			pauseButton.getView().remove();
			pauseButton = null;
			optionsMenu = null;
		}
	}

	public static void inputStrategyChanged() {
		if (!inputConsumerStack.isEmpty()) {
			if (MainGame.getInputStrategyManager().shouldFocusFirstButton()) {
				if (!inputConsumerStack.isEmpty()) {
					inputConsumerStack.peek().selectDefault();
				}
			}  else { // Mouse mode
				clearSelected();
			}
		}
	}

	private static void clearSelected() {
		if (!inputConsumerStack.isEmpty()) {
			inputConsumerStack.peek().clearSelected();
		}
	}

	private static void focusCurrent() {
		if (!inputConsumerStack.isEmpty()) {
			inputConsumerStack.peek().focusCurrent();
		}
	}

	public static boolean isPaused() {
		return optionsMenu != null && optionsMenu.getStage() != null;
	}
	
	/**
	 * Will show the pause menu if the game is not already paused
	 */
	public static void pauseIfNeeded() {
		if (!isPaused()) {
			showPauseMenu();
		}
	}

	public static void addInputActor(Actor actor) {
		group.addActor(actor);
	}

	public static void addInnerActorToStage(Stage stage) {
		stage.addActor(group);
		stage.setKeyboardFocus(keyboardInputHandler);
		
		// Also enter the default strategy (mouse) during this initialization
		enterMouseMode();
	}

	public static void setToggleFullscreenRunnable(Runnable toggleFullscreenRunnable) {
		InputPriorityManager.toggleFullscreenRunnable = toggleFullscreenRunnable;
	}


	/**
	 * To be used in exceptional circumstances only (e.g. better visuals for the trailer)
	 */
	public static void hideDarkScreenForVeryExceptionalCircumstances() {
		darkScreen.remove();
	}

}
