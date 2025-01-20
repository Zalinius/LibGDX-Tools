package com.darzalgames.libgdxtools.ui.input.universaluserinput.stage;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.inputpriority.InputPriorityManager;

public class UniversalInputStage extends Stage {

	private final InputStrategyManager inputStrategyManager;

	/**
	 * Creates a stage which can filter mouse input depending on the current {@link InputStrategyManager} input mode
	 * @param viewport
	 */
	public UniversalInputStage(Viewport viewport, InputStrategyManager inputStrategyManager) {
		super(viewport);
		this.inputStrategyManager = inputStrategyManager;
	}

	@Override
	public void act(final float delta) {
		if (!inputStrategyManager.shouldFlashButtons()) {
			// if playing mouse-driven, use a normal stage
			super.act(delta);
		} else {
			// if playing keyboard-driven, skip all the stage's code to do with detecting and firing mouse enter/exit events,
			// and just call act() on our actors like in Stage
			getRoot().act(delta);
		}
	}
	
	@Override
	public boolean scrolled(float amountX, float amountY) {
		InputPriorityManager.receiveScrollInput(amountY);
		return true;
	}

	/*
	 * https://stackoverflow.com/questions/36336111/libgdx-listener-enter-and-exit-fires-multiple-times
	 * 
	 * If you move the cursor over the button and click and then move away, you get two enter events and two exit events.

    Hover -> entered with pointer -1
    Click down -> entered with pointer 0
    Release click -> exited with pointer 0
    Move cursor away -> exited with pointer -1

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
		while(parent != null) {
			if (parent.getTouchable().equals(Touchable.disabled)) {
				return false;
			}
			parent = parent.getParent();
		}
		return true;
	}
}
