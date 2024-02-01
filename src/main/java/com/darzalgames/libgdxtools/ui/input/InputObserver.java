package com.darzalgames.libgdxtools.ui.input;

public interface InputObserver {
	public void inputStrategyChanged();
	
	/*
	 * This is checked regularly by the observer list owner since an actor should be unregistered once it's
	 * no longer on a stage, but actors aren't notified about when they're romoved from the stage
	 * (which would be an ideal time to unregister themselves, see the TODO below)
	 * <p> 
	 * TODO: Before calling this keyboard UI library usable for others, I want to go deep on notifying
	 * ALL actors in the Group's tree when they've been added to or removed from the stage (e.g. when calling
	 * clear() or clearChildren() on a Group or Table. That way, an InputObserver could un/register itself
	 * when put on or taken off the stage.
	*/ 
	public boolean shouldBeUnregistered();
}
