package com.zalinius.libgdxtools.scenes.scene2d.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class BounceActionFactory {

	private BounceActionFactory() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * An action which makes an actor scale larger or smaller, returning to the original scale
	 * You may wish to do this for alignment:
			table.setTransform(true);
			table.setOrigin(Align.center);
	 * @param scaleTo The fullest scale of the bounce, 1 = no change, 0.5 = half size, 2 = double size
	 * @param duration The time each part of the bounce takes (scales up for duration, then down for duration)
	 * @return A single bounce action that takes 2*duration to complete
	 */
	public static Action makeSingleBounceAction(final float scaleTo, final float duration) {
		float originalScale = 1 / scaleTo;
		return Actions.sequence(
				Actions.scaleTo(scaleTo, scaleTo, duration, Interpolation.sine),
				Actions.scaleTo(originalScale, originalScale, duration, Interpolation.sine)
				);}

	/**
	 * An action which makes an actor scale up 20%, then returning to the original scale all over 1 second
	 * You may wish to do this for alignment:
			table.setTransform(true);
			table.setOrigin(Align.center);
	 * @return A single bounce action that takes 2*duration to complete
	 */
	public static Action makeSingleBounceAction() {
		return makeSingleBounceAction(1.2f, 0.5f);
	}

	/**
	 * An action which FOREVER makes an actor scale up 20%, then returning to the original scale all over 1 second per cycle
	 * You may wish to do this for alignment:
			table.setTransform(true);
			table.setOrigin(Align.center);
	 * @return A single bounce action that takes 2*duration to complete
	 */
	public static Action makeForeverBounceAction() {
		return Actions.forever(makeSingleBounceAction());
	}

	/**
	 * An action which FOREVER makes an actor scale larger or smaller, returning to the original scale
	 * You may wish to do this for alignment:
			table.setTransform(true);
			table.setOrigin(Align.center);
	 * @param scaleTo The fullest scale of the bounce, 1 = no change, 0.5 = half size, 2 = double size
	 * @param duration The time each part of the bounce takes (scales up for duration, then down for duration)
	 * @return A FOREVER bounce action that takes 2*duration to complete
	 */
	public static Action makeForeverBounceAction(final float scaleTo, final float duration) {
		return Actions.forever(makeSingleBounceAction(scaleTo, duration));
	}
}
