package com.darzalgames.libgdxtools.preferences;

import com.darzalgames.darzalcommon.data.Coordinate;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer.ScreenMode;

public interface GraphicsPreference {

	float getUserInterfaceScaling();

	ScreenMode getPreferredScreenMode();

	Coordinate getPreferredWindowSize();

	void setUserInterfaceScaling(float userInterfaceScaling);

	void setPreferredScreenMode(ScreenMode preferredScreenMode);

	void setPreferredWindowSize(Coordinate preferredWindowSize);

	default void setPreferredWindowSize(int width, int height) {
		setPreferredWindowSize(new Coordinate(width, height));
	}
}
