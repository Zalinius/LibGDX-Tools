package com.darzalgames.libgdxtools.preferencemanagers;

public class SoundPreference {

	private static final String MUSIC_KEY = "musicVolume";
	private static final String SOUND_EFFECT_KEY = "soundEffectVolume";
	private static final String FOCUS_KEY = "muteWhenOutOfFocus";
	
	private final PreferenceManager preferenceManager;
	
	protected SoundPreference(PreferenceManager preferenceManager) {
		this.preferenceManager = preferenceManager;
	}

	public float getMusicVolume() {
		return preferenceManager.getFloatPrefValue(MUSIC_KEY, 0.5f);
	}

	public void setMusicVolume(float newVolume) {
		preferenceManager.savePrefValue(MUSIC_KEY, newVolume);
	}
	
	public float getSoundEffectVolume() {
		return preferenceManager.getFloatPrefValue(SOUND_EFFECT_KEY, 0.5f);
	}

	public void setSoundEffectVolume(float newVolume) {
		preferenceManager.savePrefValue(SOUND_EFFECT_KEY, newVolume);
	}
	
	public boolean shouldMuteSoundWhenOutOfFocus() {
		return preferenceManager.getBooleanPrefValue(FOCUS_KEY, false);
	}

	public void setShouldMuteSoundWhenOutOfFocus(boolean shouldMuteSoundWhenOutOfFocus) {
		preferenceManager.savePrefValue(FOCUS_KEY, shouldMuteSoundWhenOutOfFocus);
	}
}
