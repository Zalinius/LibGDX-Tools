package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class InputSetup {

	private InputPriorityStack inputPriorityStack;
	private InputReceiver inputReceiver;
	private Pause pause;
	private ScrollingManager scrollingManager;
	
	
	public InputSetup(InputStrategySwitcher inputStrategySwitcher, PauseMenu pauseMenu, Runnable toggleFullscreenRunnable, boolean toggleWithF11, Stage popUpStage) {
		inputPriorityStack = new InputPriorityStack(popUpStage, () -> pause.isPauseMenuOpen());
		inputStrategySwitcher.register(inputPriorityStack);
		
		inputReceiver = new InputReceiver(inputStrategySwitcher, inputPriorityStack, toggleFullscreenRunnable, toggleWithF11);

		scrollingManager = new ScrollingManager(inputReceiver);

		pause = new Pause(popUpStage, pauseMenu, inputPriorityStack::doesTopPauseGame);
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

	public InputReceiver getInputReceiver() {
		return inputReceiver;
	}
	
	
}
