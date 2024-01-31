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
import com.darzalgames.libgdxtools.ui.PopUp;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.optionsmenu.OptionsMenu;

public class InputPrioritizer extends Actor implements InputConsumer, InputObserver, DoesNotPause {

	private Deque<InputConsumer> inputConsumerStack;
	private Image darkScreen;
	
	private static KeyboardButton pauseButton;
	private static OptionsMenu optionsMenu;
	
	private static Stage popUpStage;
	
	private final Group group;
	private static Runnable toggleFullscreenRunnable;

	public static final InputPrioritizer instance = new InputPrioritizer();
	
	private static GamepadInputHandler gamepadInputHandler;
	private static KeyboardInputHandler keyboardInputHandler;


	private InputPrioritizer() {
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

		group = new Group();
	}

	public static void setDefaultStrategy() {
		enterMouseMode();
	}

	public static void setGamepadInputHandler(GamepadInputHandler gamepadInputHandler) {
		InputPrioritizer.gamepadInputHandler = gamepadInputHandler;
		instance.group.addActor(gamepadInputHandler);
	}

	public void clearChildren() {
		group.clearChildren(false);
		instance.inputConsumerStack.clear();
		group.addActor(gamepadInputHandler);
		group.addActor(keyboardInputHandler);
		instance.darkScreen.remove();
	}

	public static void enterMouseMode() {
		MainGame.getInputStrategyManager().setToMouseStrategy();
	}

	public static boolean enterKeyboardMode() {
		return MainGame.getInputStrategyManager().setToKeyboardStrategy();
	}

	@Override
	public void consumeKeyInput(Input input) {
		if (input == Input.TOGGLE_FULLSCREEN) {
			toggleFullscreenRunnable.run();
		} else if (input == Input.PAUSE) {
			if (pauseButton != null && 
					(!isPaused() // if we're not paused, then we're in game and should open the pause menu
							|| !checkIfLandingOnPopup())) { // we're on the pause menu (and not in a nested pop up, like the quit warning)
				pauseButton.consumeKeyInput(Input.ACCEPT);
			} else if (!instance.inputConsumerStack.isEmpty()) { // Don't try to enter keyboard mode when someone is just pressing escape
				instance.inputConsumerStack.peek().consumeKeyInput(input);
			}	
		} else if (!enterKeyboardMode() && !instance.inputConsumerStack.isEmpty()) {
			instance.inputConsumerStack.peek().consumeKeyInput(input);
		}		
	}

	private static void showDarkScreen(int actorIndex, boolean isTouchable) {
		popUpStage.addActor(instance.darkScreen);
		instance.darkScreen.setZIndex(actorIndex);
		instance.darkScreen.setTouchable(Touchable.disabled);
		DelayAction delayThenTouchable = new DelayAction(1f);
		delayThenTouchable.setAction(new RunnableActionBest(() -> 
			instance.darkScreen.setTouchable(isTouchable ? Touchable.enabled : Touchable.disabled)
		));
		instance.darkScreen.addAction(delayThenTouchable);
	}

	public static <A extends Actor & PopUp> void showPopup(A popup) {
		instance.darkScreen.remove();
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
		InputPrioritizer.showDarkScreen(popup.getZIndex(), popup.canDismiss());
	}

	private static void showPauseMenu() {
		showPopup(optionsMenu);
		claimPriority(optionsMenu);
	}

	public static void claimPriority(InputConsumer inputConsumer) {
		if (instance.inputConsumerStack.isEmpty() || !inputConsumer.equals(instance.inputConsumerStack.peek())) {
			unFocusTop();
			instance.inputConsumerStack.push(inputConsumer);
			focusTop(true);
		}
	}

	public static void releasePriority(InputConsumer inputConsumer) {
		if (!instance.inputConsumerStack.isEmpty() && inputConsumer.equals(instance.inputConsumerStack.peek())) {
			instance.darkScreen.remove();
			boolean isClosingPauseMenu = instance.inputConsumerStack.peek() == optionsMenu; 
			removeTop();
			if (isClosingPauseMenu) {
				instance.inputConsumerStack.peek().setTouchable(Touchable.enabled);
				instance.focusCurrent();
			} else {
				focusTop(false);
			}

		}
	}

	private static void unFocusTop() {
		if (!instance.inputConsumerStack.isEmpty()) {
			instance.inputConsumerStack.peek().setTouchable(Touchable.disabled);
			instance.inputConsumerStack.peek().loseFocus();
		}
	}

	private static void removeTop() {
		if (!instance.inputConsumerStack.isEmpty()) {
			unFocusTop();
			instance.inputConsumerStack.pop();
			instance.darkScreen.remove();
			checkIfLandingOnPopup();	
		}
	}

	// Comparing Actors with InputConsumers, it ain't pretty but it works.
	@SuppressWarnings("unlikely-arg-type")
	private static boolean checkIfLandingOnPopup() {
		if (popUpStage.getRoot().getChildren().size > 0) {
			List<Actor> popups = new ArrayList<>();
			SnapshotArray<Actor> inferiorArray = popUpStage.getRoot().getChildren();
			for (int i = 0; i < inferiorArray.size; i++) {
				popups.add(inferiorArray.get(i));
			}
			Optional<Actor> popupMatch = popups.stream().filter(a -> a.equals(instance.inputConsumerStack.peek())).findFirst(); 
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
		if (!instance.inputConsumerStack.isEmpty()) {
			instance.inputConsumerStack.peek().setTouchable(Touchable.enabled);
			if (isFirstFocus) {
				instance.inputConsumerStack.peek().gainFocus();				
			} else {
				instance.inputConsumerStack.peek().regainFocus();
			}
			instance.focusCurrent();
		}
	}

	public static void setPauseUI(OptionsMenu optionsMenu) {
		clearPauseButtonUI();
		pauseButton = optionsMenu.getButton();
		popUpStage.addActor(pauseButton.getView());
		InputPrioritizer.optionsMenu = optionsMenu;
	}

	public static void setKeyboardInputHandler(KeyboardInputHandler keyboardInputHandler) {
		InputPrioritizer.keyboardInputHandler = keyboardInputHandler;
	}

	public static void setPopUpStage(Stage stage) {
		InputPrioritizer.popUpStage = stage;
	}

	private static void clearPauseButtonUI() {
		if (pauseButton != null) {
			pauseButton.getView().remove();
			pauseButton = null;
			optionsMenu = null;
		}
	}

	@Override
	public void inputStrategyChanged() {
		if (!instance.inputConsumerStack.isEmpty()) {
			if (MainGame.getInputStrategyManager().shouldFocusFirstButton()) {
				selectDefault();
			}  else { // Mouse mode
				clearSelected();
			}
		}
	}

	@Override
	public void selectDefault() {
		if (!instance.inputConsumerStack.isEmpty()) {
			instance.inputConsumerStack.peek().selectDefault();
		}
	}

	@Override
	public boolean shouldBeUnregistered() {
		return this.getStage() == null;
	}

	@Override
	public void clearSelected() {
		if (!instance.inputConsumerStack.isEmpty()) {
			instance.inputConsumerStack.peek().clearSelected();
		}
	}

	@Override
	public void focusCurrent() {
		if (!instance.inputConsumerStack.isEmpty()) {
			instance.inputConsumerStack.peek().focusCurrent();
		}
	}

	public static boolean isPaused() {
		return optionsMenu != null && optionsMenu.getStage() != null;
	}
	
	public static void pauseIfNeeded() {
		if (!isPaused()) {
			showPauseMenu();
		}
	}

	@Override
	public void actWhilePaused(float delta) {
		// We do not call act() on instance.group, which contains the whole (paused) game
		gamepadInputHandler.actWhilePaused(delta);
		super.act(delta);
	}

	public static void addInputActor(Actor actor) {
		instance.group.addActor(actor);
	}

	public static void addInnerActorToStage(Stage stage) {
		stage.addActor(instance.group);
		stage.setKeyboardFocus(keyboardInputHandler);
	}

	public static void setToggleFullscreenRunnable(Runnable toggleFullscreenRunnable) {
		InputPrioritizer.toggleFullscreenRunnable = toggleFullscreenRunnable;
	}


	/**
	 * To be used in exceptional circumstances only (e.g. better visuals for the trailer)
	 */
	public static void hideDarkScreenForVeryExceptionalCircumstances() {
		instance.darkScreen.remove();
	}

}
