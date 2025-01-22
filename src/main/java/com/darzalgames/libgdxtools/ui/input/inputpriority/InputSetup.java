package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class InputSetup {

	private InputPriorityStack inputPriorityStack;
	private InputStrategySwitcher inputStrategySwitcher;
	private InputReceiver inputReceiver;
	private Pause pause;
	private ScrollingManager scrollingManager;
	
	
	public InputSetup(InputStrategySwitcher inputStrategySwitcher, Runnable toggleFullscreenRunnable, boolean toggleWithF11, PauseMenu optionsMenu, Stage stage, Stage popUpStage) {
		this.inputStrategySwitcher = inputStrategySwitcher;
		inputReceiver = new InputReceiver(toggleFullscreenRunnable, inputStrategySwitcher, toggleWithF11, inputPriorityStack);

		scrollingManager = new ScrollingManager(inputReceiver::processScrollingInput);
		

		inputPriorityStack = new InputPriorityStack(popUpStage);
		inputStrategySwitcher.register(inputPriorityStack);

		pause = new Pause(popUpStage, optionsMenu, inputPriorityStack::doesTopPauseGame);
		pause.setClaimPriority(inputPriorityStack::claimPriority);
		stage.addActor(pause);
		inputReceiver.setPause(pause);
	}


	public InputPriorityStack getInputPriorityStack() {
		return inputPriorityStack;
	}

	public Pause getPause() {
		return pause;
	}

	public ScrollingManager getScrollingManager() {
		return scrollingManager;
	}
	
	
}
