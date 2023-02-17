package com.zalinius.libgdxtools.preferencemanagers;

public class I18NPreference {
	
	private static final String LANGUAGE_KEY = "language";
	
	private final PreferenceManager preferenceManager;
	
	protected I18NPreference(PreferenceManager preferenceManager) {
		this.preferenceManager = preferenceManager;
	}
	
	public String getPreferedLanguage() {
		return preferenceManager.getStringPrefValue(LANGUAGE_KEY);
	}
	public String getPreferedLanguage(String defaultLanguage) {
		return preferenceManager.getStringPrefValue(LANGUAGE_KEY, defaultLanguage);
	}

	public void setPreferedLanguage(String language) {
		preferenceManager.savePrefValue(LANGUAGE_KEY, language);
	}
}
