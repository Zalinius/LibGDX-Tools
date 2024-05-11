package com.darzalgames.libgdxtools.audio.composition.track;

import java.util.List;

import com.darzalgames.libgdxtools.audio.amplitude.Envelope;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.synth.Synth;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;

public abstract class Track {
	
	private final String songName;
	private final String trackName;
	
	public Track(String songName, String trackName) {
		this.songName = songName;
		this.trackName = trackName;
	}
	/**
	 * @return The musical instants active during the  sixteenth, AND any instants starting in the next Sixteenth, ordererd by their start time?
	 */
    public abstract List<TimedMusicalInstant> getMusicalInstantsActiveNow(TimeInstant time);
	public abstract int trackLengthInSixteenths();
	
    public abstract Envelope getEnvelope();
    public abstract Synth getSynth();
    public abstract float getAmplitude();
    
    public String getIdPrefix() {
    	return songName + " " + trackName + " ";
    }
    
    public String getTrackName() {
		return trackName;
	}


}
