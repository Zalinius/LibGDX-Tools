package com.darzalgames.libgdxtools.maingame;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StageBest extends Stage implements StageLikeRenderable {

	public StageBest(String name, Viewport viewport) {
		super(viewport);
		getRoot().setName(name);
	}

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height, true);
		getCamera().update();
	}

	@Override
	public String nameOfThingAtCursorPosition(float x, float y) {
		Vector2 cursor = screenToStageCoordinates(new Vector2(x, y));
		Actor hitActor = hit(cursor.x, cursor.y, true);
		if (hitActor != null) {
			return hitActor.getName();
		} else {
			return "";
		}
	}

	@Override
	public String getName() {
		return getRoot().getName();
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public void unfocusAll() {
		setScrollFocus(null);
		// Never clear keyboard focus in our games, as in the original overridden method
		cancelTouchFocus();
	}

	/*
	 * https://stackoverflow.com/questions/36336111/libgdx-listener-enter-and-exit-fires-multiple-times
	 *
	 * If you move the cursor over the button and click and then move away, you get two enter events and two exit events.
	 *
	 * Hover -> entered with pointer -1
	 * Click down -> entered with pointer 0
	 * Release click -> exited with pointer 0
	 * Move cursor away -> exited with pointer -1
	 *
	 */
	public static boolean isHoverEvent(int pointer) {
		return pointer == -1;
	}

	/*
	 * The Actor method isTouchable() is particular to that actor, unrelated to what's going on higher in the hierarchy.
	 * For example, a Button may be Touchable.enabled but a child of a Table which is currently Touchable.disabled.
	 * In that case the stage filters out touch events (so practically speaking the Button cannot be touched),
	 * but the button's isTouchable() would still return true.
	 * This convenience function lets you know whether or not an Actor is truly touchable Stage in the hierarchy.
	 */
	public static boolean isInTouchableBranch(Actor actor) {
		Actor parent = actor;
		while (parent != null) {
			if (Touchable.disabled.equals(parent.getTouchable())) {
				return false;
			}
			parent = parent.getParent();
		}
		return true;
	}

}
