package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;

public class DelayActionWithLastMinuteDurationChoice extends DelayAction {

	private boolean hasStarted = false;
	private final Supplier<Float> durationSupplier;

	public DelayActionWithLastMinuteDurationChoice(Supplier<Float> durationSupplier) {
		this.durationSupplier = durationSupplier;
	}

	@Override
	protected boolean delegate(float delta) {
		if (!hasStarted) {
			hasStarted = true;
			setDuration(durationSupplier.get());
		}
		return super.delegate(delta);
	}

	@Override
	public void restart() {
		super.restart();
		hasStarted = false;
	}

}
