package com.darzalgames.libgdxtools.ui.input.inputpriority;

public interface InputSubject {
	public void register(InputObserver obj);
	public void unregister(InputObserver obj);
	
	public void notifyObservers();
}
