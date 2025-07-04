package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.libgdxtools.maingame.MultiStage;

public class Pause extends Actor {

	private final OptionsMenu optionsMenu;
	private final Supplier<Boolean> doesCurrentInputConsumerPauseGame;
	private final Supplier<String> getNameOfPausingStage;

	/**
	 * @param optionsMenu the game's pause/options menu
	 * @param doesCurrentInputConsumerPauseGame A supplier to tell us if whatever's in focus pauses the game (some popups and the options menu do this)
	 * @param getNameOfPausingStage a supplier for the name of the stage that the current pausing the game, if any
	 */
	public Pause(OptionsMenu optionsMenu, Supplier<Boolean> doesCurrentInputConsumerPauseGame, Supplier<String> getNameOfPausingStage) {
		this.doesCurrentInputConsumerPauseGame = doesCurrentInputConsumerPauseGame;
		this.getNameOfPausingStage = getNameOfPausingStage;
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

	public String getNameOfPausingStage() {
		return getNameOfPausingStage.get();
	}

	private void pause() {
		InputPriority.claimPriority(optionsMenu, MultiStage.OPTIONS_STAGE_NAME);
	}

	@Override
	public void act(float delta) {
		optionsMenu.addOptionsButtonToStage();
	}
}
