package com.darzalgames.libgdxtools.ui.input.popup;

import com.darzalgames.libgdxtools.ui.input.InputConsumer;

public interface PopUp extends InputConsumer {

	/**
	 * @return Whether or not the popup can be dismissed by pressing "back", or if one of the options must be chosen
	 */
	public default boolean canDismiss() {
		return true;
	}
	
	/**
	 * Handles hiding the pop up and unregistering it from the input system
	 */
	public default void hideThis() {}

	@Override
	public default boolean isGamePausedWhileThisIsInFocus() {
		return true;
	}
}