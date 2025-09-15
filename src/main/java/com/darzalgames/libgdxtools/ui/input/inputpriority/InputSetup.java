package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.List;

import com.darzalgames.libgdxtools.maingame.StageLikeRenderable;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class InputSetup {

	private final InputPriorityStack inputPriorityStack;
	private final InputReceiver inputReceiver;
	private final ScrollingManager scrollingManager;

	public InputSetup(InputStrategySwitcher inputStrategySwitcher, Runnable toggleFullscreenRunnable, List<StageLikeRenderable> allStagesInOrderForInput, Pause pause) {
		inputPriorityStack = new InputPriorityStack(allStagesInOrderForInput, pause.getOptionsMenu(), inputStrategySwitcher);
		inputReceiver = new InputReceiver(inputStrategySwitcher, inputPriorityStack, toggleFullscreenRunnable);
		scrollingManager = new ScrollingManager(inputReceiver);

		pause.setInformationalSuppliers(inputPriorityStack::doesTopPauseGame, inputPriorityStack::getNameOfPausingStage);
		inputReceiver.setPause(pause);
	}

	public InputPriorityStack getInputPriorityStack() {
		return inputPriorityStack;
	}

	public ScrollingManager getScrollingManager() {
		return scrollingManager;
	}

	public InputReceiver getInputReceiver() {
		return inputReceiver;
	}

}
