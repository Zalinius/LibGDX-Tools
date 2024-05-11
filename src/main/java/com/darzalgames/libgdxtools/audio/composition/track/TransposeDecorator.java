package com.darzalgames.libgdxtools.audio.composition.track;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.darzalgames.libgdxtools.audio.amplitude.Envelope;
import com.darzalgames.libgdxtools.audio.composition.Pitch;
import com.darzalgames.libgdxtools.audio.engine.music.MusicalInstant;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.synth.Synth;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;

public class TransposeDecorator extends Track {

	private final Track decoratedTrack;
	private Function<Pitch, Pitch> transposer;

	public TransposeDecorator(Track decoratedTrack, String songName) {
		super(songName, decoratedTrack.getTrackName() + " transpose");
		this.decoratedTrack = decoratedTrack;
		this.resetTransposer();
	}
	
	public void setTransposer(UnaryOperator<Pitch> transposer) {
		this.transposer = transposer;
	}
	
	public void resetTransposer() {
		this.transposer = p -> p;
	}
	
	@Override
	public float getAmplitude() {
		return decoratedTrack.getAmplitude();
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
				map(mi -> new TimedMusicalInstant(mi.startingSixteenth , new MusicalInstant(mi.musicalInstant.getDuration(), transposer.apply(mi.musicalInstant.getNote()), mi.musicalInstant.getSynth(), mi.musicalInstant.getEnvelope(), mi.musicalInstant.getAmplitude(), mi.musicalInstant.getId()))).
				toList();
	}
	
	@Override
	public int trackLengthInSixteenths() {
		return decoratedTrack.trackLengthInSixteenths();
	}
}
