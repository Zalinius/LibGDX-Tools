package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class InputSetup {

	private final InputPriorityStack inputPriorityStack;
	private final InputReceiver inputReceiver;
	private final Pause pause;
	private final ScrollingManager scrollingManager;


	public InputSetup(InputStrategySwitcher inputStrategySwitcher, OptionsMenu optionsMenu, Runnable toggleFullscreenRunnable, Stage popUpStage, Stage pauseStage) {
		inputPriorityStack = new InputPriorityStack(popUpStage, optionsMenu);
		inputStrategySwitcher.register(inputPriorityStack);

		inputReceiver = new InputReceiver(inputStrategySwitcher, inputPriorityStack, toggleFullscreenRunnable);

		scrollingManager = new ScrollingManager(inputReceiver);

		pause = new Pause(pauseStage, optionsMenu, inputPriorityStack::doesTopPauseGame);
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
