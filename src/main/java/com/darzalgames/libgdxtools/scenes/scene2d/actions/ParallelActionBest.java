package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

/**
 * @author DarZal
 * This is basically all taken from the standard ParallelAction, but allows constructing with more than five actions at once (i.e. any number of actions)
 */
public class ParallelActionBest extends Action {
	protected List<Action> actions = new ArrayList<>();
	private boolean complete;

	public ParallelActionBest(Action... allActions) {
		actions.addAll(Arrays.asList(allActions));
	}

	public ParallelActionBest(List<Action> allActions) {
		actions = allActions;
	}

	@Override
	public boolean act (float delta) {
		if (complete) { 
			return true;
		}
		complete = true;
		Pool<?> pool = getPool();
		setPool(null); // Ensure this action can't be returned to the pool while executing.
		try {
			for (int i = 0, n = actions.size(); i < n && actor != null; i++) {
				Action currentAction = actions.get(i);
				if (currentAction.getActor() != null && !currentAction.act(delta)) {
					complete = false;
				}
			}
			if (actor == null) {
				// This action was removed.
				return true;
			}
			return complete;
		} finally {
			setPool(pool);
		}
	}

	@Override
	public void restart () {
		complete = false;
		actions.forEach(Action::restart);
	}

	@Override
	public void reset () {
		super.reset();
		actions.clear();
	}

	public void addAction (Action action) {
		actions.add(action);
		if (actor != null) action.setActor(actor);
	}

	@Override
	public void setActor (Actor actor) {
		actions.forEach(action -> action.setActor(actor));
		super.setActor(actor);
	}

	@Override
	public String toString () {
		StringBuilder buffer = new StringBuilder(64);
		buffer.append(super.toString());
		buffer.append('(');
		for (int i = 0, n = actions.size(); i < n; i++) {
			if (i > 0) buffer.append(", ");
			buffer.append(actions.get(i));
		}
		buffer.append(')');
		return buffer.toString();
	}
}
