package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.libgdxtools.maingame.MultiStage;
import com.darzalgames.libgdxtools.maingame.StageLikeRenderable;

public class Pause extends Actor {

	private final OptionsMenu optionsMenu;
	private final StageLikeRenderable popUpStage;
	private final Supplier<Boolean> doesCurrentInputConsumerPauseGame;
	private final Supplier<String> getNameOfPausingStage;

	/**
	 * @param doesCurrentInputConsumerPauseGame A supplier to tell us if whatever's in focus pauses the game (some popups and the options menu do this)
	 */
	public Pause(StageLikeRenderable stageLikeRenderable, OptionsMenu optionsMenu, Supplier<Boolean> doesCurrentInputConsumerPauseGame, Supplier<String> getNameOfPausingStage) {
		popUpStage = stageLikeRenderable;
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
		optionsMenu.addOptionsButtonToStage(popUpStage);
	}
}
