package com.darzalgames.libgdxtools.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferenceManager {

	private final Preferences prefs;

	/**
	 * @param preferencePrefix A name for the preferences file, which should be in a package format corresponding to the game, e.g. com.darzalgames.cultivar
	 */
	public PreferenceManager(final String preferencePrefix) {
		this(Gdx.app.getPreferences(preferencePrefix));
	}

	public PreferenceManager(final Preferences preferencesFile) {
		prefs = preferencesFile;
	}

	public SoundPreference sound() {
		return new SoundPreference(this);
	}

	public GraphicsPreference graphics() {
		return new GraphicsPreference(this);
	}

	public PausePreference pause() {
		return new PausePreference(this);
	}

	public OtherPreferences other() {
		return new OtherPreferences(this);
	}

	protected boolean prefHasSavedKey(final String key) {
		Preferences preferences = getPreferences();
		return preferences.contains(key);
	}

	protected String getStringPrefValue(final String key) {
		Preferences preferences = getPreferences();
		return preferences.getString(key);
	}

	protected String getStringPrefValue(final String key, final String defaultValue) {
		Preferences preferences = getPreferences();
		return preferences.getString(key, defaultValue);
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

	protected float getFloatPrefValue(final String key) {
		Preferences preferences = getPreferences();
		return preferences.getFloat(key);
	}

	protected float getFloatPrefValue(final String key, final float defaultValue) {
		Preferences preferences = getPreferences();
		return preferences.getFloat(key, defaultValue);
	}

	protected void savePrefValue(final String key, final String value) {
		Preferences preferences = getPreferences();
		preferences.putString(key, value);
		preferences.flush();
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

	protected void savePrefValue(final String key, final float value) {
		Preferences preferences = getPreferences();
		preferences.putFloat(key, value);
		preferences.flush();
	}

	protected void savePrefValue(final String key, final boolean value) {
		Preferences preferences = getPreferences();
		preferences.putBoolean(key, value);
		preferences.flush();
	}

	private Preferences getPreferences() {
		return prefs;
	}

}
