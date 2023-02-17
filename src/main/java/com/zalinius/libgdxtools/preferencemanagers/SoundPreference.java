package com.zalinius.libgdxtools.preferencemanagers;

public class SoundPreference {

	private static final String MUSIC_KEY = "musicSetting";
	
	private final PreferenceManager preferenceManager;
	
	protected SoundPreference(PreferenceManager preferenceManager) {
		this.preferenceManager = preferenceManager;
	}

	public void setMusicMuted(final boolean isMuted) {
		preferenceManager.savePrefValue(MUSIC_KEY, isMuted);
	}

	public int getMaxMusicVol() {
		return isMusicMuted() ? 0 : 1;
	}

	public boolean isMusicMuted() {
		return preferenceManager.getBooleanPrefValue(MUSIC_KEY, false);
	}
}
