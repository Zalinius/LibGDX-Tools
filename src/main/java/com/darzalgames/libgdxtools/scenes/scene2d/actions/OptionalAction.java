package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import java.util.function.BooleanSupplier;

import com.badlogic.gdx.scenes.scene2d.actions.DelegateAction;

public class OptionalAction extends DelegateAction {

	private BooleanSupplier shouldExecuteAction;

	public OptionalAction(BooleanSupplier shouldExecuteAction) {
		super();
		this.shouldExecuteAction = shouldExecuteAction;
	}

	@Override
	protected boolean delegate(float delta) {
		if (shouldExecuteAction.getAsBoolean()) {
			if (action == null) {
				return true;
			} else {
				return action.act(delta);
			}
		} else {
			return true;
		}
	}

	public boolean shouldAct() {
		return shouldExecuteAction.getAsBoolean();
	}

}
