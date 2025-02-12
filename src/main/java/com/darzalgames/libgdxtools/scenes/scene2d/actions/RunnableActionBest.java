package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

/**
 * A convenience wrapper for the standard {@link RunnableAction}, which allows you to supply a runnable in the constructor
 */
public class RunnableActionBest extends RunnableAction {

	public RunnableActionBest(Runnable runnable) {
		super();
		this.setRunnable(runnable);
	}
	
}
