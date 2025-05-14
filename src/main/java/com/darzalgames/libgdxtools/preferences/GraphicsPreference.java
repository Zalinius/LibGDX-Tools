package com.darzalgames.libgdxtools.preferences;

import com.darzalgames.darzalcommon.data.Coordinate;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer.ScreenMode;

public class GraphicsPreference {

	private static final String USER_INTERFACE_SCALING_KEY = "userInterfaceScaling";
	private static final String SCREEN_MODE_KEY = "screenMode";
	private static final String WINDOW_WIDTH_KEY = "windowWidth";
	private static final String WINDOW_HEIGHT_KEY = "windowHeight";

	private final PreferenceManager preferenceManager;

	protected GraphicsPreference(PreferenceManager preferenceManager) {
		this.preferenceManager = preferenceManager;
	}


	public float getUserInterfaceScaling() {
		return preferenceManager.getFloatPrefValue(USER_INTERFACE_SCALING_KEY, 1);
	}
	public void setUserInterfaceScaling(float newScaling) {
		preferenceManager.savePrefValue(USER_INTERFACE_SCALING_KEY, newScaling);
	}


	public String getPreferredScreenMode() {
		return preferenceManager.getStringPrefValue(SCREEN_MODE_KEY, ScreenMode.FULLSCREEN.name());
	}
	public void setPreferredScreenMode(String preferredScreenMode) {
		preferenceManager.savePrefValue(SCREEN_MODE_KEY, preferredScreenMode);
	}


	public Coordinate getPreferredWindowSize() {
		int width = preferenceManager.getIntegerPrefValue(WINDOW_WIDTH_KEY, 1920);
		int height = preferenceManager.getIntegerPrefValue(WINDOW_HEIGHT_KEY, 1080);
		return new Coordinate(width, height);
	}
	public void setPreferredWindowSize(int width, int height) {
		preferenceManager.savePrefValue(WINDOW_WIDTH_KEY, width);
		preferenceManager.savePrefValue(WINDOW_HEIGHT_KEY, height);
	}
}
