package com.zalinius.libgdxtools.preferencemanagers;

public class SoundPreferenceManager extends PreferenceManager {

	private static final String MUSIC_KEY = "musicSetting";

	public static void setMusicMuted(final boolean isMuted) {
		savePrefValue(MUSIC_KEY, isMuted);
	}

	public static int getMaxMusicVol() {
		return isMusicMuted() ? 0 : 1;
	}

	public static boolean isMusicMuted() {
		return getBooleanPrefValue(MUSIC_KEY, false);
	}
}
