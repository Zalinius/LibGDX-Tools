package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.darzalgames.libgdxtools.ui.input.InputConsumer;

public class InputPriority {

	private InputPriority() {
	}

	private static InputPriorityStack inputPriorityStack;

	public static void claimPriority(InputConsumer inputConsumer, String stageName) {
		inputPriorityStack.claimPriority(inputConsumer, stageName);
	}

	public static void releasePriority(InputConsumer inputConsumer) {
		inputPriorityStack.releasePriority(inputConsumer);
	}

	static void setInputPriorityStack(InputPriorityStack inputPriorityStack) {
		InputPriority.inputPriorityStack = inputPriorityStack;
	}
}
