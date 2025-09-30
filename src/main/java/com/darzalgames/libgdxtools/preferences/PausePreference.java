package com.darzalgames.libgdxtools.preferences;

public class PausePreference {
	private static final String FOCUS_KEY = "pauseWhenOutOfFocus";

	private final PreferenceManager preferenceManager;

	protected PausePreference(PreferenceManager preferenceManager) {
		this.preferenceManager = preferenceManager;
	}

	public boolean shouldPauseGameWhenOutOfFocus() {
		return preferenceManager.getBooleanPrefValue(FOCUS_KEY, true);
	}

	public void setShouldPauseGameWhenOutOfFocus(boolean shouldPauseSoundWhenOutOfFocus) {
		preferenceManager.savePrefValue(FOCUS_KEY, shouldPauseSoundWhenOutOfFocus);
	}
}
