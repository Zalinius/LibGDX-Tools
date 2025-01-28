package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

/**
 * Almost entirely copied from the original RepeatAction implementation of a forever action,
 * this class will call act() on the action right after restart().
 */
public class InstantForeverAction extends RepeatAction {
	private int repeatCount;
	private int executedCount;
	private boolean finished;

	/**
	 * An action which will repeat the supplier action until {@link #finish()} is called. On the frame
	 * the action is completed, it will be restarted instantly.
	 * @param action The action to repeat indefinitely
	 */
	public InstantForeverAction(Action action) {
		setCount(RepeatAction.FOREVER);
		setAction(action);
		Actions.forever(action);
	}

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
			boolean isDelegateDone = action.act(delta); // THIS IS THE MAIN CHANGE TO MAKE IT INSTANT
			if (isDelegateDone) {
				return true;
			}
		}
		return false;
	}

	/** Causes the action to not repeat again. */
	@Override
	public void finish() {
		finished = true;
	}

	@Override
	public void restart() {
		super.restart();
		executedCount = 0;
		finished = false;
	}

	/** Sets the number of times to repeat. Can be set to {@link #FOREVER}. */
	@Override
	public void setCount(int count) {
		this.repeatCount = count;
	}

	@Override
	public int getCount() {
		return repeatCount;
	}

}
