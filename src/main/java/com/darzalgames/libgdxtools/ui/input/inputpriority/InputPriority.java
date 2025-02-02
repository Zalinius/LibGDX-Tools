package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.darzalgames.libgdxtools.ui.input.InputConsumer;

public class InputPriority {

	private static InputPriorityStack inputPriorityStack;

	public static void claimPriority(InputConsumer inputConsumer) {
		inputPriorityStack.claimPriority(inputConsumer);
	}
	
	public static void releasePriority(InputConsumer inputConsumer) {
		inputPriorityStack.releasePriority(inputConsumer);
	}

	static void setInputPriorityStack(InputPriorityStack inputPriorityStack) {
		InputPriority.inputPriorityStack = inputPriorityStack;
	}
}
