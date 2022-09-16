package com.zalinius.libgdxtools.preferencemanagers;

import com.badlogic.gdx.Preferences;

public class SoundPreferenceManager extends PreferenceManager {

	private static final String MUSIC_KEY = "musicSetting";

	public SoundPreferenceManager(final Preferences preferencesFile) {
		super(preferencesFile);
	}

	public void setMusicMuted(final boolean isMuted) {
		savePrefValue(MUSIC_KEY, isMuted);
	}

	public int getMaxMusicVol() {
		return isMusicMuted() ? 0 : 1;
	}

	public boolean isMusicMuted() {
		return getBooleanPrefValue(MUSIC_KEY, false);
	}
}
