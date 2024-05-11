package com.darzalgames.libgdxtools.audio.composition.track;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.darzalgames.libgdxtools.audio.amplitude.Envelope;
import com.darzalgames.libgdxtools.audio.composition.Instrument;
import com.darzalgames.libgdxtools.audio.composition.NoteDuration;
import com.darzalgames.libgdxtools.audio.composition.Pitch;
import com.darzalgames.libgdxtools.audio.engine.music.MusicalInstant;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.synth.Synth;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;

public class SixteenthRhythmTrack extends Track{

	private Instrument instrument;
	private float amplitude;
	
	private Function<Integer, Boolean> rhythm;
	private int rhythmLength;
	
	private int startDelay;
	
	public SixteenthRhythmTrack(String song, String track, Instrument instrument) {
		super(song, track);
		this.instrument = instrument;
		this.amplitude = 1f;
		this.rhythm = CONSTANT_FAST;
		this.rhythmLength = 4;
		this.startDelay = 0;
	}
	
	@Override
    public List<TimedMusicalInstant> getMusicalInstantsActiveNow(TimeInstant timeNow){
		List<TimedMusicalInstant> timedMusicalInstants = new ArrayList<>();
		
		for (TimeInstant time = timeNow; time.getTotalSixteenths() <= timeNow.getTotalSixteenths() + 1; time = time.next()) {
			if(time.getTotalBeats() >= startDelay && rhythm.apply(time.getTotalSixteenths() % rhythmLength)) {
				String id = getIdPrefix() + time.getTotalSixteenths();
				timedMusicalInstants.add(new TimedMusicalInstant(time.getTotalSixteenths(), new MusicalInstant(NoteDuration.SIXTEENTH, Pitch.C3, getSynth(), getEnvelope(), getAmplitude(), id)));
			}
		}
		
		return timedMusicalInstants;
	}
	
	@Override
	public int trackLengthInSixteenths() {
		return rhythmLength;
	}

	
	public void setRhythm(Function<Integer, Boolean> rhythm) {
		setRhythm(rhythm, 4);
	}

	public void setRhythm(Function<Integer, Boolean> rhythm, int rhythmLength) {
		this.rhythm = rhythm;
		this.rhythmLength = rhythmLength;
	}


	public static final Function<Integer, Boolean> CONSTANT_FAST = makeSixteenthRhythm(true, true, true, true);
	public static final Function<Integer, Boolean> GALLOP_FAST = makeSixteenthRhythm(true, false, true, true);
	public static final Function<Integer, Boolean> TRIPLE_FAST = makeSixteenthRhythm(true, true, true, false);
	
	public static Function<Integer, Boolean> makeSixteenthRhythm(boolean s0, boolean s1, boolean s2, boolean s3){
		return (sixteenth) -> {
			switch (sixteenth) {
			case 0: return s0;
			case 1: return s1;
			case 2: return s2;
			case 3: return s3;
			default:
				throw new IllegalArgumentException();
			}
		};
	}
	
	public static Function<Integer, Boolean> makeDoubleSixteenthRhythm(boolean s0, boolean s1, boolean s2, boolean s3, boolean s4, boolean s5, boolean s6, boolean s7){
		return (sixteenth) -> {
			switch (sixteenth) {
			case 0: return s0;
			case 1: return s1;
			case 2: return s2;
			case 3: return s3;
			case 4: return s4;
			case 5: return s5;
			case 6: return s6;
			case 7: return s7;
			default:
				throw new IllegalArgumentException();
			}
		};
	}
	
	public void setAnySixteenthRhythm(Boolean...s){
		Function<Integer, Boolean> newRhythm = sixteenth -> s[sixteenth];
		setRhythm(newRhythm, s.length);
	}
	
	
	@Override
	public float getAmplitude() {
		return amplitude;
	}
	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}
	
	@Override
	public Envelope getEnvelope() {
		return instrument.envelope();
	}
	@Override
	public Synth getSynth() {
		return instrument.synth();
	}

	public void setInitialDelay(int lengthInBeats) {
		this.startDelay = lengthInBeats;
	}

}
