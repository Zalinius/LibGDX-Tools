package com.darzalgames.libgdxtools.preferences;

import com.darzalgames.darzalcommon.data.Coordinate;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer.ScreenMode;

public interface GraphicsPreference {

	float getUserInterfaceScaling();

	ScreenMode getPreferredScreenMode();

	Coordinate getPreferredWindowSize();

	void setUserInterfaceScaling(float newScaling);

	void setPreferredScreenMode(ScreenMode preferredScreenMode);

	void setPreferredWindowSize(Coordinate coordinate);

	default void setPreferredWindowSize(int width, int height) {
		setPreferredWindowSize(new Coordinate(width, height));
	}
}
