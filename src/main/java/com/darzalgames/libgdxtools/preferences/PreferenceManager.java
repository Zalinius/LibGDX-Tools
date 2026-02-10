package com.darzalgames.libgdxtools.preferences;

public interface PreferenceManager {

	SoundPreference sound();

	GraphicsPreference graphics();

	boolean shouldPauseGameWhenOutOfFocus();

	void setShouldPauseGameWhenOutOfFocus(boolean shouldPauseSoundWhenOutOfFocus);

}
