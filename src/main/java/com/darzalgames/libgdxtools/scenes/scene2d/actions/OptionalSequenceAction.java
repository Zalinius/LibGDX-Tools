package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

/**
 * A sequence of OptionalAction, which will instantly skip an action if it isn't supposed to be executed.
 * Effectively, in a single act call, OptionalActions are called until one is enable. 
 */
public class OptionalSequenceAction extends Action{

	private List<OptionalAction> optionalActions;
	private int index;

	public OptionalSequenceAction (OptionalAction... actions) {
		this.optionalActions = new ArrayList<>();
		for (int i = 0; i < actions.length; i++) {
			addAction(actions[i]);
		}
	}

	@Override
	public boolean act (float delta) {
		if (index >= optionalActions.size()) {
			return true;
		}
		Pool<?> pool = getPool();
		setPool(null); // Ensure this action can't be returned to the pool while executing.
		try {
			while (index < optionalActions.size()) {
				OptionalAction action = optionalActions.get(index);
				if(action.shouldAct()) {
					boolean actionDone = action.act(delta);
					if(!actionDone) {
						return false;
					}
					else {
						index++;
						return index >= optionalActions.size();
					}
				}
				else {
					index++;
					if (index >= optionalActions.size()) {
						return true;
					}
				}
			}

			return true;
		} finally {
			setPool(pool);
		}
	}

	@Override
	public void restart () {
		super.restart();
		index = 0;
	}
	
	public void addAction (OptionalAction optionalAction) {
		optionalActions.add(optionalAction);
		if (actor != null) {
			optionalAction.setActor(actor);
		}
	}
	
	/**
	 * Adds an action that is non optional
	 */
	public void addMandatoryAction(Action action) {
		OptionalAction mandatoryAction = new OptionalAction(() -> true);
		mandatoryAction.setAction(action);
		addAction(mandatoryAction);
	}

	@Override
	public void setActor (Actor actor) {
		optionalActions.forEach(action -> action.setActor(actor));
		super.setActor(actor);
	}


}
