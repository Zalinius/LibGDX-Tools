package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;

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
	
	public default boolean isPopUp() {
		return false;
	}
	
	public default Tuple<Actor, PopUp> getPopUp() {
		throw new UnsupportedOperationException(this.toString() + ": A base InputConsumer isn't necessarily a popup! Override isPopUp() & getPopUp() if this truly is one.");
	}

	/**
	 * @return If this object is the topmost UI, is the game paused?
	 */
	public default boolean isGamePausedWhileThisIsInFocus() {
		return false;
	}

	/**
	 * A convenient shorthand for releasing priority
	 */
	public default void releasePriority() {
		InputPriority.releasePriority(this);
	}
	
} 
