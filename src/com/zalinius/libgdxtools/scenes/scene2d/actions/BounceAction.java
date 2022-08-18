package com.zalinius.libgdxtools.scenes.scene2d.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public abstract class BounceAction {

	private BounceAction() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * An action which makes an actor scale larger or smaller, returning to the original scale
	 * @param scaleTo The fullest scale of the bounce, 1 = no change, 0.5 = half size, 2 = double size
	 * @param duration The time each part of the bounce takes (scales up for duration, then down for duration)
	 * @return A single bounce action that takes 2*duration to complete
	 */
	public static Action getSingleBounceAction(final float scaleTo, final float duration) {
		float originalScale = 1 / scaleTo;
		return Actions.sequence(
				Actions.scaleTo(scaleTo, scaleTo, duration, Interpolation.sine),
				Actions.scaleTo(originalScale, originalScale, duration, Interpolation.sine)
				);}

	/**
	 * An action which makes an actor scale up 20%, then returning to the original scale all over 1 second
	 * @return A single bounce action that takes 2*duration to complete
	 */
	public static Action getSingleBounceAction() {
		return getSingleBounceAction(1.2f, 0.5f);
	}

	/**
	 * An action which FOREVER makes an actor scale up 20%, then returning to the original scale all over 1 second per cycle
	 * @return A single bounce action that takes 2*duration to complete
	 */
	public static Action getForeverBounceAction() {
		return Actions.forever(getSingleBounceAction());
	}

	/**
	 * An action which FOREVER makes an actor scale larger or smaller, returning to the original scale
	 * @param scaleTo The fullest scale of the bounce, 1 = no change, 0.5 = half size, 2 = double size
	 * @param duration The time each part of the bounce takes (scales up for duration, then down for duration)
	 * @return A FOREVER bounce action that takes 2*duration to complete
	 */
	public static Action getForeverBounceAction(final float scaleTo, final float duration) {
		return Actions.forever(getSingleBounceAction(scaleTo, duration));
	}
}
