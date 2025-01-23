package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class InputSetup {

	private InputPriorityStack inputPriorityStack;
	private InputReceiver inputReceiver;
	private Pause pause;
	private ScrollingManager scrollingManager;
	
	
	public InputSetup(InputStrategySwitcher inputStrategySwitcher, Runnable toggleFullscreenRunnable, boolean toggleWithF11, Stage stage, Stage popUpStage) {
		inputPriorityStack = new InputPriorityStack(popUpStage);
		inputStrategySwitcher.register(inputPriorityStack);
		
		inputReceiver = new InputReceiver(toggleFullscreenRunnable, inputStrategySwitcher, toggleWithF11, inputPriorityStack);

		scrollingManager = new ScrollingManager(inputReceiver::processScrollingInput);

		pause = new Pause(popUpStage, inputPriorityStack::doesTopPauseGame);
		pause.setClaimPriority(inputPriorityStack::claimPriority);
		inputReceiver.setPause(pause);

		stage.addActor(pause);
		stage.addActor(inputStrategySwitcher);
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

	public InputReceiver getInputReceiver() {
		return inputReceiver;
	}
	
	
}
