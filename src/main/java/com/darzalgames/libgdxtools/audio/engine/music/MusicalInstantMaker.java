package com.darzalgames.libgdxtools.audio.engine.music;

import java.util.List;

import com.darzalgames.libgdxtools.audio.time.TimeInstant;

public interface MusicalInstantMaker {
	/**
``	 * @return The musical instants active during the next TWO sixteenth, in the order of their start time
	 */
	List<TimedMusicalInstant> getMusicalInstantsActiveNow(TimeInstant time);
	int songLengthInSixteenths();
}