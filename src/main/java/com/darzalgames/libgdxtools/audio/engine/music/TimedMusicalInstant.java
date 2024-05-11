package com.darzalgames.libgdxtools.audio.engine.music;

public class TimedMusicalInstant {
	public TimedMusicalInstant(int startingSixteenth, MusicalInstant musicalInstant) {
		this.startingSixteenth = startingSixteenth;
		this.musicalInstant = musicalInstant;
	}

	public final int startingSixteenth;
	public final MusicalInstant musicalInstant;
	
}