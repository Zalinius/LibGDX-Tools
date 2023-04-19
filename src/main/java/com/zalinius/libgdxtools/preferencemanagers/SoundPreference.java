package com.zalinius.libgdxtools.preferencemanagers;

public class SoundPreference {

	private static final String MUSIC_KEY = "musicVolume";
	
	private final PreferenceManager preferenceManager;
	
	protected SoundPreference(PreferenceManager preferenceManager) {
		this.preferenceManager = preferenceManager;
	}

	public float getMusicVolume() {
		return preferenceManager.getFloatPrefValue(MUSIC_KEY, 1);
	}

	public void setMusicVolume(float newVolume) {
		preferenceManager.savePrefValue(MUSIC_KEY, newVolume);
	}
}
