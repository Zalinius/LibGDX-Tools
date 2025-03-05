package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Pause extends Actor {

	private PauseMenu pauseMenu;
	private Stage popUpStage;
	private final Supplier<Boolean> doesCurrentInputConsumerPauseGame;

	/**
	 * @param doesCurrentInputConsumerPauseGame A supplier to tell us if whatever's in focus pauses the game (some popups and the pause menu do this)
	 */
	public Pause(Stage popUpStage, PauseMenu pauseMenu, Supplier<Boolean> doesCurrentInputConsumerPauseGame) {
		this.popUpStage = popUpStage;
		this.doesCurrentInputConsumerPauseGame = doesCurrentInputConsumerPauseGame;
		GamePauser.setPauseGameRunnable(this::pause);

		this.pauseMenu = pauseMenu;

		pauseMenu.addPauseButtonToStage(popUpStage);
		showPauseButton(false); // Only enable the button after the splash screen
	}

	public void showPauseButton(boolean show) {
		pauseMenu.showPauseButton(show);
	}

	boolean isPauseMenuOpen() {
		return pauseMenu.getStage() != null;
	}

	public boolean isPaused() {
		return doesCurrentInputConsumerPauseGame.get();
	}

	private void pause() {
		InputPriority.claimPriority(pauseMenu);
	}

	@Override
	public void act(float delta) {
		pauseMenu.addPauseButtonToStage(popUpStage);
	}
}
