package com.zalinius.libgdxtools.preferencemanagers;

import com.badlogic.gdx.Preferences;

public class PreferenceManager {

	private static Preferences prefs;

	public static void create(final Preferences preferencesFile) {
		prefs = preferencesFile;
	}

	protected static boolean prefHasSavedKey(final String key) {
		Preferences preferences = getPreferences();
		return preferences.contains(key);
	}

	protected static int getIntegerPrefValue(final String key) {
		Preferences preferences = getPreferences();
		return preferences.getInteger(key);
	}

	protected static int getIntegerPrefValue(final String key, final int defaultValue) {
		Preferences preferences = getPreferences();
		return preferences.getInteger(key, defaultValue);
	}

	protected static long getLongPrefValue(final String key) {
		Preferences preferences = getPreferences();
		return preferences.getLong(key);
	}

	protected static long getLongPrefValue(final String key, final long defaultValue) {
		Preferences preferences = getPreferences();
		return preferences.getLong(key, defaultValue);
	}

	protected static boolean getBooleanPrefValue(final String key) {
		Preferences preferences = getPreferences();
		return preferences.getBoolean(key);
	}

	protected static boolean getBooleanPrefValue(final String key, final boolean defaultValue) {
		Preferences preferences = getPreferences();
		return preferences.getBoolean(key, defaultValue);
	}


	protected static void savePrefValue(final String key, final long value) {
		Preferences preferences = getPreferences();
		preferences.putLong(key, value);
		preferences.flush();
	}

	protected static void savePrefValue(final String key, final int value) {
		Preferences preferences = getPreferences();
		preferences.putInteger(key, value);
		preferences.flush();
	}

	protected static void savePrefValue(final String key, final boolean value) {
		Preferences preferences = getPreferences();
		preferences.putBoolean(key, value);
		preferences.flush();
	}

	protected PreferenceManager() {
		throw new IllegalStateException("Utility class");
	}

	private static Preferences getPreferences() {
		return prefs;
	}

}
