package com.darzalgames.libgdxtools.audio.engine.sound;

import java.util.List;

import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;

public interface SimpleSoundMaker {
	/**
	 * @param musicalInstants
	 * @param bps
	 * @param sixteenthProgress the current exact progress of the song
	 * @return The SimpleSounds active during this sixteenth, created from Musical Instants, with their start time in seconds instead of Sixteenths
	 */
	List<TimedSimpleSound> makeSimpleSounds(List<TimedMusicalInstant> musicalInstants, float bps, float sixteenthProgress, float currentTime);
}