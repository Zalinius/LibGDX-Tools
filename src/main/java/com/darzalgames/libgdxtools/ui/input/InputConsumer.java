package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.Alignment;
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
	void consumeKeyInput(final Input input);

	/**
	 * NOTE: If the receiver of this function has a child that is also an InputConsumer on top of it, then instead of setting disabled it should set childrenOnly
	 * @param isTouchable
	 */
	void setTouchable(Touchable isTouchable);

	/**
	 * Called when this object was just put on the input stack
	 */
	default void gainFocus() {}

	/**
	 * Called when this object gains focus back (i.e. something above it on the stack relinquished focus)
	 */
	default void regainFocus() {}

	/**
	 * Called when this object has lost focus (sometimes an object does this voluntarily, but it also happens when something else claims priority above this)
	 */
	default void loseFocus() {}

	/**
	 * If applicable, put the current relevant object into focus (e.g. selecting the current button so it flashes)
	 */
	void focusCurrent();

	/**
	 * If applicable, clear the currently selected object
	 */
	void clearSelected();

	/**
	 * If applicable, select a default object (e.g. the first button in a list, or the only intractable object in the current scene)
	 */
	void selectDefault();

	boolean isDisabled();

	boolean isBlank();

	void setAlignment(Alignment alignment);

	void setDisabled(boolean disabled);

	default void setFocused(boolean focused) {
		if (focused) {
			focusCurrent();
		} else {
			clearSelected();
		}
	}

	default boolean isPopUp() {
		return false;
	}

	default PopUp getPopUp() {
		throw new UnsupportedOperationException(toString() + ": A base InputConsumer isn't necessarily a popup! Override isPopUp() & getPopUp() if this truly is one.");
	}

	/**
	 * @return If this object is the topmost UI, is the game paused?
	 */
	default boolean isGamePausedWhileThisIsInFocus() {
		return false;
	}

	/**
	 * A convenient shorthand for releasing priority
	 */
	default void releasePriority() {
		InputPriority.releasePriority(this);
	}

	/**
	 * Called every frame, objects should resize their UI in case the window/font have changed size
	 */
	void resizeUI();

}
