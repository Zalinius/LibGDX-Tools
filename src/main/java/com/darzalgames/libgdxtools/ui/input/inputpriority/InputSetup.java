package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.List;

import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer;
import com.darzalgames.libgdxtools.maingame.StageLikeRenderable;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class InputSetup {

	private final InputPriorityStack inputPriorityStack;
	private final InputReceiver inputReceiver;

	public InputSetup(InputStrategySwitcher inputStrategySwitcher, WindowResizer windowResizer, List<StageLikeRenderable> allStagesInOrderForInput, Pause pause) {
		windowResizer.initialize(inputStrategySwitcher);
		Runnable onClickDarkScreen = () -> getInputPriorityStack().sendInputToTop(Input.BACK);
		inputPriorityStack = new InputPriorityStack(allStagesInOrderForInput, pause.getOptionsMenu(), inputStrategySwitcher, new DarkScreen(onClickDarkScreen));
		inputReceiver = new InputReceiver(inputStrategySwitcher, inputPriorityStack, windowResizer);

		pause.setInformationalSuppliers(inputPriorityStack::doesTopPauseGame, inputPriorityStack::getNameOfPausingStage);
		inputReceiver.setPause(pause);
	}

	public InputPriorityStack getInputPriorityStack() {
		return inputPriorityStack;
	}

	public InputReceiver getInputReceiver() {
		return inputReceiver;
	}

}
