package com.darzalgames.libgdxtools.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class CenterActor {

	/**
	 * Center a CHILD actor on its PARENT
	 * @param actorToMove this actor will be moved to be centered on its parent
	 */
	public static void centerActorOnParent(Actor actorToMove) {
		Actor actorToCenterOn = actorToMove.getParent();
		if (actorToCenterOn == null) {
			throw new IllegalArgumentException("Can't center " + actorToMove + " since it has no parent");
		}
		actorToMove.setPosition(
				(actorToCenterOn.getWidth() * actorToCenterOn.getScaleX()) / 2f - (actorToMove.getWidth() * actorToMove.getScaleX()) / 2f,
				(actorToCenterOn.getHeight() * actorToCenterOn.getScaleY()) / 2f - (actorToMove.getHeight() * actorToMove.getScaleY()) / 2f
		);
	}
}
