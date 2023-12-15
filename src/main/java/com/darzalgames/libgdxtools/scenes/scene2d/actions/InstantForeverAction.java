package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

/**
 * Almost entirely copied from the original ForeverAction, this class will call act()
 * on the action right after restart().
 * @author DarZal
 *
 */
public class InstantForeverAction extends RepeatAction {
	static public final int FOREVER = -1;

	private int repeatCount, executedCount;
	private boolean finished;

	public InstantForeverAction(Action action) {
		setCount(RepeatAction.FOREVER);
		setAction(action);
	}

	@Override
	protected boolean delegate (float delta) {
		if (executedCount == repeatCount) return true;
		if (action.act(delta)) {
			if (finished) return true;
			if (repeatCount > 0) executedCount++;
			if (executedCount == repeatCount) return true;
			if (action != null) {
				action.restart();
				action.act(delta); // THIS is the change
			}
		}
		return false;
	}

	/** Causes the action to not repeat again. */
	@Override
	public void finish () {
		finished = true;
	}

	@Override
	public void restart () {
		super.restart();
		executedCount = 0;
		finished = false;
	}

	/** Sets the number of times to repeat. Can be set to {@link #FOREVER}. */
	@Override
	public void setCount (int count) {
		this.repeatCount = count;
	}

	@Override
	public int getCount () {
		return repeatCount;
	}

}
