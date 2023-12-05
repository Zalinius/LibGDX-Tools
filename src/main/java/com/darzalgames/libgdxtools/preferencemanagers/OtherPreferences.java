package com.darzalgames.libgdxtools.preferencemanagers;

public class OtherPreferences {
	
	private final PreferenceManager preferenceManager;
	
	protected OtherPreferences(PreferenceManager preferenceManager) {
		this.preferenceManager = preferenceManager;
	}
	
	public String getStringPrefValue(String key) {
		return preferenceManager.getStringPrefValue(key);
	}
	public String getStringPrefValue(String key, String defaultString) {
		return preferenceManager.getStringPrefValue(key, defaultString);
	}
	public void setStringPrefValue(String key, String value) {
		preferenceManager.savePrefValue(key, value);
	}

	public boolean getBooleanPrefValue(String key) {
		return preferenceManager.getBooleanPrefValue(key);
	}
	public boolean getBooleanPrefValue(String key, boolean defaultValue) {
		return preferenceManager.getBooleanPrefValue(key, defaultValue);
	}
	public void setBooleanPrefValue(String key, boolean value) {
		preferenceManager.savePrefValue(key, value);
	}

}
