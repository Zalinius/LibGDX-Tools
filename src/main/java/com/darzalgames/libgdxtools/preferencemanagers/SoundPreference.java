package com.darzalgames.libgdxtools.preferencemanagers;

public class SoundPreference {

	private static final String MUSIC_KEY = "musicVolume";
	private static final String FOCUS_KEY = "muteWhenOutOfFocus";
	
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
	
	public boolean shouldMuteSoundWhenOutOfFocus() {
		return preferenceManager.getBooleanPrefValue(FOCUS_KEY, false);
	}

	public void setShouldMuteSoundWhenOutOfFocus(boolean shouldMuteSoundWhenOutOfFocus) {
		preferenceManager.savePrefValue(FOCUS_KEY, shouldMuteSoundWhenOutOfFocus);
	}
}