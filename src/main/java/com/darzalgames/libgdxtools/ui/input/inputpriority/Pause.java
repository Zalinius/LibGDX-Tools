package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.libgdxtools.maingame.MultipleStage;

public class Pause extends Actor {

	private final OptionsMenu optionsMenu;
	private Supplier<Boolean> doesCurrentInputConsumerPauseGame;
	private Supplier<String> getNameOfPausingStage;

	/**
	 * @param optionsMenu the game's pause/options menu
	 */
	public Pause(OptionsMenu optionsMenu) {
		GamePauser.setPauseGameRunnable(this::pause);

		this.optionsMenu = optionsMenu;

		showOptionsButton(false); // Only enable the button after the splash screen
	}

	public void showOptionsButton(boolean show) {
		optionsMenu.showOptionsButton(show);
	}

	public boolean isPaused() {
		return doesCurrentInputConsumerPauseGame.get();
	}

	public String getNameOfPausingStage() {
		return getNameOfPausingStage.get();
	}

	private void pause() {
		if (!isOptionsMenuOpen()) {
			InputPriority.claimPriority(optionsMenu, MultipleStage.OPTIONS_STAGE_NAME);
		}
	}

	@Override
	public void act(float delta) {
		optionsMenu.addOptionsButtonToStage();
	}


	/**
	 * @param doesCurrentInputConsumerPauseGame A supplier to tell us if whatever's in focus pauses the game (some popups and the options menu do this)
	 * @param getNameOfPausingStage a supplier for the name of the stage that the current pausing the game, if any
	 */
	void setInformationalSuppliers(Supplier<Boolean> doesCurrentInputConsumerPauseGame, Supplier<String> getNameOfPausingStage) {
		this.doesCurrentInputConsumerPauseGame = doesCurrentInputConsumerPauseGame;
		this.getNameOfPausingStage = getNameOfPausingStage;
	}

	boolean isOptionsMenuOpen() {
		return optionsMenu.getStage() != null;
	}

	OptionsMenu getOptionsMenu() {
		return optionsMenu;
	}

}
