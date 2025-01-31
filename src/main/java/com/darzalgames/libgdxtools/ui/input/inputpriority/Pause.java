package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

public class Pause extends Actor {

	private UniversalButton pauseButton;
	private PauseMenu pauseMenu;
	private Stage popUpStage;
	private final Supplier<Boolean> doesCurrentInputConsumerPauseGame;

	/**
	 * @param popUpStage
	 * @param pauseMenu 
	 * @param doesCurrentInputConsumerPauseGame A supplier to tell us if whatever's in focus pauses the game (some popups and the pause menu do this)
	 */
	public Pause(Stage popUpStage, PauseMenu pauseMenu, Supplier<Boolean> doesCurrentInputConsumerPauseGame) {
		this.popUpStage = popUpStage;
		this.doesCurrentInputConsumerPauseGame = doesCurrentInputConsumerPauseGame;
		GamePauser.setPauseGameIfNeededRunnable(this::pauseIfNeeded);

		this.pauseMenu = pauseMenu;
		pauseButton = pauseMenu.getButton();

		int padding = 1; // TODO Make this more customizable?
		pauseButton.getView().setPosition(padding, GameInfo.getHeight() - pauseButton.getView().getHeight() - padding);
		showPauseButton(false); // Only enable the button after the splash screen
		popUpStage.addActor(pauseButton.getView());
	}

	public void showPauseButton(boolean show) {
		pauseButton.setTouchable(show ? Touchable.enabled : Touchable.disabled);
		pauseButton.getView().setVisible(show);
	}

	void pressPauseButton() {
		pauseButton.consumeKeyInput(Input.ACCEPT);
	}

	public boolean isPaused() {
		/**
		 * TODO This system makes no distinction between a popup pausing the game versus the pause menu pausing the game
		 * and so when you're on a popup which does pause the game, pressing escape does not open the pause menu
		 * because we're already paused. (So in QG I had to set a lot of popups not to pause the game anymore, feels off...)
		 */
		return doesCurrentInputConsumerPauseGame.get();
	}

	private void pauseIfNeeded() {
		if (!isPaused()) {
			pause();
		}
	}

	private void pause() {
		Priority.claimPriority(pauseMenu);	
	}

	@Override
	public void act(float delta) {
		if (pauseButton != null) {
			popUpStage.addActor(pauseButton.getView());
			pauseButton.getView().toFront();
		}
	}
}
