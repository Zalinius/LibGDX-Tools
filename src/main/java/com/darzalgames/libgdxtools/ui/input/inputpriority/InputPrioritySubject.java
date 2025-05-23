package com.darzalgames.libgdxtools.ui.input.inputpriority;

public interface InputPrioritySubject {
	void register(InputPriorityObserver observer);
	void notifyInputPriorityObservers();
}
