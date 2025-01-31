package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.actions.DelegateAction;

/**
 * Basically a copy of the regular RepeatAction, but which restarts instantly
 * and allows others to query the executedCount.
 */
public class InstantRepeatAction extends DelegateAction {
	public static final int FOREVER = -1;

	private int repeatCount;
	private int executedCount;
	private boolean finished;

	@Override
	protected boolean delegate (float delta) {
		if (executedCount == repeatCount) return true;
		if (action.act(delta)) {
			if (finished) {
				return true;
			}
			if (repeatCount > 0) {
				executedCount++;
			}
			if (executedCount == repeatCount) {
				return true;
			}
			action.restart();
			boolean isDelegateDone = act(delta); // THIS IS THE MAIN CHANGE TO MAKE IT INSTANT
			if (isDelegateDone) {
				return true;
			}
			
		}
		return false;
	}

	/** Causes the action to not repeat again. */
	public void finish () {
		finished = true;
	}

	@Override
	public void restart () {
		super.restart();
		executedCount = 0;
		finished = false;
	}

	/** Sets the number of times to repeat. Can be set to {@link #FOREVER}. 
	 * @param count 
	 */
	public void setTotalCount (int count) {
		this.repeatCount = count;
	}

	public int getTotalCount () {
		return repeatCount;
	}

	public int getExecutedCount() {
		return executedCount;
	}

	public int getRemainingCount() {
		return repeatCount - executedCount;
	}
}
