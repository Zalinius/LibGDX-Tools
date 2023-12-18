package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import java.util.Arrays;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Pool;

/**
 * Almost entirely copied from the original SequenceAction, this class will call act()
 * on the next action as soon as the first one ends.
 * @author DarZal
 *
 */
public class InstantSequenceAction extends ParallelActionBest {

	private int index;

	public InstantSequenceAction (Action... allActions) {
		actions.addAll(Arrays.asList(allActions));
	}
	
	@Override
	public boolean act(float delta) {
		if (index >= actions.size()) return true;
		@SuppressWarnings("rawtypes")
		Pool pool = getPool();
		setPool(null); // Ensure this action can't be returned to the pool while executing.
		try {
			if (actions.get(index).act(delta)) {
				if (actor == null) return true; // This action was removed.
				index++;
				if (index >= actions.size()) return true;
				act(delta); // THIS is the change.
			}
			return false;
		} finally {
			setPool(pool);
		}
	}


	@Override
	public void restart () {
		super.restart();
		index = 0;
	}
}
