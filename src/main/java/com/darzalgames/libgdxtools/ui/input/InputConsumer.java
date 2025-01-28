package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.input.inputpriority.Priority;

/**
 * Anything that can be interacted with via user input
 */
public interface InputConsumer {
	/**
	 * Choose how to respond to various inputs
	 * @param input
	 */
	public void consumeKeyInput(final Input input);

	/**
	 * NOTE: If the receiver of this function has a child that is also an InputConsumer on top of it, then instead of setting disabled it should set childrenOnly
	 * @param isTouchable
	 */
	public void setTouchable(Touchable isTouchable);

	/**
	 * Called when this object was just put on the input stack
	 */
	public default void gainFocus() {}

	/**
	 * Called when this object gains focus back (i.e. something above it on the stack relinquished focus)
	 */
	public default void regainFocus() {}

	/**
	 * Called when this object has lost focus (sometimes an object does this voluntarily, but it also happens when something else claims priority above this)
	 */
	public default void loseFocus() {}

	/**
	 * If applicable, put the current relevant object into focus (e.g. selecting the current button so it flashes)
	 */
	public void focusCurrent();

	/**
	 * If applicable, clear the currently selected object
	 */
	public void clearSelected();

	/**
	 * If applicable, select a default object (e.g. the first button in a list, or the only intractable object in the current scene)
	 */
	public void selectDefault();

	/**
	 * If this object is the topmost UI, is the game paused?
	 */
	public default boolean isGamePausedWhileThisIsInFocus() {
		return false;
	}

	/**
	 * A convenient shorthand for releasing priority
	 */
	public default void releasePriority() {
		Priority.releasePriority(this);
	}

	/**
	 * These are for more logic-heavy objects which still need to be on the input stack,
	 * but which don't themselves do much interaction with input. For example, in Quest Giver the WorldStateMachine
	 * proceeds to the next state whenever it regains focus (i.e. the previous state released focus)
	 * but doesn't itself have any UI or interactions with the player. Another example is the
	 * SteamAchievement states which grant an achievement and immediately release focus.
	 */
	public static InputConsumer makeLogicalInputConsumer(Runnable onGainFocus) {
		return new InputConsumer() {

			@Override
			public void gainFocus() {
				onGainFocus.run();
				releasePriority();
			}

			@Override public void setTouchable(Touchable isTouchable) {}

			@Override public void selectDefault() {}

			@Override public void focusCurrent() {}

			@Override public void consumeKeyInput(Input input) {}

			@Override public void clearSelected() {}
		};
	}
} 
