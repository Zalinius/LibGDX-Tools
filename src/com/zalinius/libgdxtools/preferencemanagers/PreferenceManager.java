package com.zalinius.libgdxtools.preferencemanagers;

import com.badlogic.gdx.Preferences;

public class PreferenceManager {

	private Preferences prefs;

	public PreferenceManager(final Preferences preferencesFile) {
		prefs = preferencesFile;
	}

	protected boolean prefHasSavedKey(final String key) {
		Preferences preferences = getPreferences();
		return preferences.contains(key);
	}

	protected int getIntegerPrefValue(final String key) {
		Preferences preferences = getPreferences();
		return preferences.getInteger(key);
	}

	protected int getIntegerPrefValue(final String key, final int defaultValue) {
		Preferences preferences = getPreferences();
		return preferences.getInteger(key, defaultValue);
	}

	protected long getLongPrefValue(final String key) {
		Preferences preferences = getPreferences();
		return preferences.getLong(key);
	}

	protected long getLongPrefValue(final String key, final long defaultValue) {
		Preferences preferences = getPreferences();
		return preferences.getLong(key, defaultValue);
	}

	protected boolean getBooleanPrefValue(final String key) {
		Preferences preferences = getPreferences();
		return preferences.getBoolean(key);
	}

	protected boolean getBooleanPrefValue(final String key, final boolean defaultValue) {
		Preferences preferences = getPreferences();
		return preferences.getBoolean(key, defaultValue);
	}


	protected void savePrefValue(final String key, final long value) {
		Preferences preferences = getPreferences();
		preferences.putLong(key, value);
		preferences.flush();
	}

	protected void savePrefValue(final String key, final int value) {
		Preferences preferences = getPreferences();
		preferences.putInteger(key, value);
		preferences.flush();
	}

	protected void savePrefValue(final String key, final boolean value) {
		Preferences preferences = getPreferences();
		preferences.putBoolean(key, value);
		preferences.flush();
	}

	protected PreferenceManager() {
		throw new IllegalStateException("Utility class");
	}

	private Preferences getPreferences() {
		return prefs;
	}

}
