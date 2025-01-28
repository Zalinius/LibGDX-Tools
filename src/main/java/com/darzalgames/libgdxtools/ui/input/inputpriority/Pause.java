package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

public class Pause extends Actor implements DoesNotPause {

	private UniversalButton pauseButton;
	private PauseMenu pauseMenu;
	private Stage popUpStage;
	private final Supplier<Boolean> doesCurrentInputConsumerPauseGame;

	/**
	 * @param popUpStage
	 * @param doesCurrentInputConsumerPauseGame A supplier to tell us if whatever's in focus pauses the game (some popups and the options menu do this)
	 */
	public Pause(Stage popUpStage, Supplier<Boolean> doesCurrentInputConsumerPauseGame) {
		this.popUpStage = popUpStage;
		this.doesCurrentInputConsumerPauseGame = doesCurrentInputConsumerPauseGame;
		GamePauser.setPauseGameIfNeededRunnable(this::pauseIfNeeded);
	}

	void addPauseButtonToStage() {
		popUpStage.addActor(pauseButton.getView());
	}

	void sendButtonToFront() {
		pauseButton.getView().toFront();
	}

	void pressPauseButton() {
		pauseButton.consumeKeyInput(Input.ACCEPT);
	}

	// TODO remove this and only set the pause menu once....
	public void setPauseMenu(PauseMenu pauseMenu) {
		this.pauseMenu = pauseMenu;
		pauseButton = pauseMenu.getButton();
		pauseButton.getView().setPosition(3, GameInfo.getHeight() - pauseButton.getView().getHeight() - 3);
		popUpStage.addActor(pauseButton.getView());
	}

	public boolean isPaused() {
		return doesCurrentInputConsumerPauseGame.get();
	}

	/**
	 * Will show the pause menu if the game is not already paused
	 */
	public void pauseIfNeeded() {
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

	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}
}
