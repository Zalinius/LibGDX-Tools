package com.darzalgames.libgdxtools.ui.input.inputpriority;

public interface InputStrategySubject {
	public void register(InputStrategyObserver obj);
	public void unregister(InputStrategyObserver obj);
	
	public void notifyObservers();
}
