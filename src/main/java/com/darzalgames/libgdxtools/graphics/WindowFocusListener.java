package com.darzalgames.libgdxtools.graphics;

import com.badlogic.gdx.ApplicationListener;

public interface WindowFocusListener {

	/** Called when the window lost focus to another window. The window's {@link ApplicationListener} will continue to be called. */
	void focusLost();

	/** Called when the window gained focus. */
	void focusGained();
}
