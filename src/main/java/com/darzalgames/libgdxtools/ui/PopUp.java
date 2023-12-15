package com.darzalgames.libgdxtools.ui;

import com.darzalgames.libgdxtools.ui.input.InputConsumer;

public interface PopUp extends InputConsumer {

	public default boolean canDismiss() {
		return true;
	}
	public default void hideThis() {}
}
