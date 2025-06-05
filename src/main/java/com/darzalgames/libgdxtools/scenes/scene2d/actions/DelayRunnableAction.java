package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;

public class DelayRunnableAction extends DelayAction {

	public DelayRunnableAction(float delay, Runnable runnable) {
		setDuration(delay);
		setAction(new RunnableActionBest(runnable));
	}

}
