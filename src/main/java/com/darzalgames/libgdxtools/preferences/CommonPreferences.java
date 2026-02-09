package com.darzalgames.libgdxtools.preferences;

public interface CommonPreferences {

	SoundPreference sound();

	GraphicsPreference graphics();

	boolean shouldPauseGameWhenOutOfFocus();

	void setShouldPauseGameWhenOutOfFocus(boolean shouldPauseSoundWhenOutOfFocus);

}
