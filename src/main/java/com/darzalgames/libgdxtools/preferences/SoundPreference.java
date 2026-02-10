package com.darzalgames.libgdxtools.preferences;

import com.darzalgames.zalaudiolibrary.VolumeListener;

public interface SoundPreference extends VolumeListener {

	/**
	 * Gets the music volume
	 * @return A multiplier for music volume, within [0,1]
	 */
	float getMusicVolume();

	/**
	 * Gets the sound effect volume
	 * @return A multiplier for sound effect volume, within [0,1]
	 */
	float getSoundEffectVolume();

	void setShouldMuteSoundWhenOutOfFocus(boolean shouldMute);

	boolean shouldMuteSoundWhenOutOfFocus();

}
