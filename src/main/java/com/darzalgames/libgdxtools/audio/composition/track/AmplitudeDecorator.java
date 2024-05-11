package com.darzalgames.libgdxtools.audio.composition.track;

import java.util.List;

import com.darzalgames.libgdxtools.audio.amplitude.Envelope;
import com.darzalgames.libgdxtools.audio.engine.music.MusicalInstant;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.synth.Synth;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;

public class AmplitudeDecorator extends Track{

	private final Track decoratedTrack;
	private float amplitudeMultiplier;
	
	public AmplitudeDecorator(Track decoratedTrack, String song) {
		super(song, decoratedTrack.getTrackName() + " amplitude");
		this.decoratedTrack = decoratedTrack;
		this.amplitudeMultiplier = 1f;
	}
	
	public void setAmplitudeMultiplier(float amplitudeMultiplier) {
		this.amplitudeMultiplier = amplitudeMultiplier;
	}
	
	public void resetAmplitudeMultiplier() {
		this.amplitudeMultiplier = 1f;
	}
	
	@Override
	public float getAmplitude() {
		return amplitudeMultiplier * decoratedTrack.getAmplitude();
	}
	
	@Override
	public Envelope getEnvelope() {
		return decoratedTrack.getEnvelope();
	}
	
	@Override
	public Synth getSynth() {
		return decoratedTrack.getSynth();
	}
	
	@Override
	public List<TimedMusicalInstant> getMusicalInstantsActiveNow(TimeInstant time) {
		List<TimedMusicalInstant> originalInstants = decoratedTrack.getMusicalInstantsActiveNow(time);
		
		return originalInstants.
				stream().
				map(mi -> new TimedMusicalInstant(mi.startingSixteenth , new MusicalInstant(mi.musicalInstant.getDuration(), mi.musicalInstant.getNote(), mi.musicalInstant.getSynth(), mi.musicalInstant.getEnvelope(), amplitudeMultiplier * mi.musicalInstant.getAmplitude(), mi.musicalInstant.getId()))).
				toList();
	}
	
	@Override
	public int trackLengthInSixteenths() {
		return decoratedTrack.trackLengthInSixteenths();
	}
	
}
