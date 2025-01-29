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
	 * @param doesCurrentInputConsumerPauseGame A supplier to tell us if whatever's in focus pauses the game (some popups and the options menu do this)
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
