package com.darzalgames.libgdxtools.graphics.windowresizer;

import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer.ScreenMode;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

public interface WindowResizerButton {

	public void setWindowResizer(WindowResizer windowResizer);
	
	public void setSelectedScreenMode(ScreenMode currentScreenMode);

	public UniversalButton getButton();
	public InputConsumer getRevertMenu();
	
	public default ScreenMode getModeFromPreference(String screenMode) {
		ScreenMode preferredMode = ScreenMode.BORDERLESS;
		for (int i = 0; i < ScreenMode.values().length; i++) {
			if (screenMode.equalsIgnoreCase(ScreenMode.values()[i].name())) {
				preferredMode = ScreenMode.values()[i];
			}
		}
		return preferredMode;
	}
}
