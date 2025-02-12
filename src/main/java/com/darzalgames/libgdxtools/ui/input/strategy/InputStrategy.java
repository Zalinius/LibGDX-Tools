package com.darzalgames.libgdxtools.ui.input.strategy;

public interface InputStrategy  {
	/**
	 * @return Whether or not to flash buttons when they're in focus
	 */
	public boolean shouldFlashButtons();
	
	/**
	 * @return Whether or not to show mouse exclusive buttons, the cursor, etc. (probably the opposite answer as the above method)
	 */
	public boolean isMouseMode();

}
