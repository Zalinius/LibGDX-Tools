package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.utils.Pool;

/**
 * Almost entirely copied from the original SequenceAction, this class will call act()
 * on the next action as soon as the first one ends.
 * @author DarZal
 *
 */
public class InstantSequenceAction extends ParallelAction {

	private int index;
	
	// TODO divorce myself from these terrible limited LibGDX SeqActions that have max 5 actions and these awful constructors
	public InstantSequenceAction(Action action1, Action action2) {
		super(action1, action2);
	}
	
	public InstantSequenceAction(Action action1, Action action2, Action action3) {
		super(action1, action2, action3);
	}
	
	public InstantSequenceAction(Action action1, Action action2, Action action3, Action action4) {
		super(action1, action2, action3, action4);
	}
	
	@Override
	public boolean act(float delta) {
		if (index >= getActions().size) return true;
		@SuppressWarnings("rawtypes")
		Pool pool = getPool();
		setPool(null); // Ensure this action can't be returned to the pool while executings.
		try {
			if (getActions().get(index).act(delta)) {
				if (actor == null) return true; // This action was removed.
				index++;
				if (index >= getActions().size) return true;
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
