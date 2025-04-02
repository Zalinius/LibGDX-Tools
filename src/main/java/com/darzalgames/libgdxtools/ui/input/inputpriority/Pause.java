package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Pause extends Actor {

	private OptionsMenu optionsMenu;
	private Stage popUpStage;
	private final Supplier<Boolean> doesCurrentInputConsumerPauseGame;

	/**
	 * @param doesCurrentInputConsumerPauseGame A supplier to tell us if whatever's in focus pauses the game (some popups and the options menu do this)
	 */
	public Pause(Stage popUpStage, OptionsMenu optionsMenu, Supplier<Boolean> doesCurrentInputConsumerPauseGame) {
		this.popUpStage = popUpStage;
		this.doesCurrentInputConsumerPauseGame = doesCurrentInputConsumerPauseGame;
		GamePauser.setPauseGameRunnable(this::pause);

		this.optionsMenu = optionsMenu;

		showOptionsButton(false); // Only enable the button after the splash screen
	}

	public void showOptionsButton(boolean show) {
		optionsMenu.showOptionsButton(show);
	}

	boolean isOptionsMenuOpen() {
		return optionsMenu.getStage() != null;
	}

	public boolean isPaused() {
		return doesCurrentInputConsumerPauseGame.get();
	}

	private void pause() {
		InputPriority.claimPriority(optionsMenu);
	}

	@Override
	public void act(float delta) {
		optionsMenu.addOptionsButtonToStage(popUpStage);
	}
}
