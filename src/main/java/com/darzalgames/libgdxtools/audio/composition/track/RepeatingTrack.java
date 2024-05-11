package com.darzalgames.libgdxtools.audio.composition.track;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.darzalgames.libgdxtools.audio.amplitude.Envelope;
import com.darzalgames.libgdxtools.audio.composition.Instrument;
import com.darzalgames.libgdxtools.audio.composition.NoteDuration;
import com.darzalgames.libgdxtools.audio.composition.Pitch;
import com.darzalgames.libgdxtools.audio.engine.music.MusicalInstant;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.synth.Synth;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;

public class RepeatingTrack extends Track {
	
	//The notes in the melody, with their starting sixteenths as integer keys
	private SortedMap<Integer, List<MusicalInstant>> trackMelody;

	private int repetitionSixteenth;
	
	private Synth synth;
	private float amplitude;
	private Envelope envelope;

	public RepeatingTrack(Instrument instrument, String song, String track) {
		this(instrument.synth(), instrument.envelope(), song, track);
	}

	
	public RepeatingTrack(Synth synth, Envelope envelope, String song, String track) {
		this(synth, envelope, 1f, song, track);
	}
	
	public RepeatingTrack(Synth synth, Envelope envelope, float amplitude, String song, String track) {
		super(song, track);
		this.trackMelody = new TreeMap<>();
		this.repetitionSixteenth = 0;
		this.synth = synth;
		this.envelope = envelope;
		this.amplitude = amplitude;
	}
	
	
	public void setRepetitionPoint() {
		this.repetitionSixteenth = trackLengthInSixteenths();
	}
	
	public void addSilence(NoteDuration noteDuration) {
		int currentSixteenths = trackLengthInSixteenths();
		String silenceId = getIdPrefix() + "silence " + currentSixteenths;
		trackMelody.put(currentSixteenths, Arrays.asList(new MusicalInstant(noteDuration, Pitch.NONE, Synth.zero, getEnvelope(), 0f, silenceId)));
	}

	public void addInstant(NoteDuration noteDuration, Pitch... note) {
		addInstant(noteDuration, getEnvelope(), note);
	}

	public void addQuietedInstant(NoteDuration noteDuration, Pitch... note) {
		int currentSixteenths = trackLengthInSixteenths();
		List<MusicalInstant> musicalInstants = new ArrayList<>();
		float amplitudeMod = (float) (1/Math.sqrt(note.length));
		for (int i = 0; i < note.length; i++) {
			String id = getIdPrefix() + currentSixteenths + ": " + i;
			musicalInstants.add(new MusicalInstant(noteDuration, note[i], getSynth(), envelope, amplitudeMod * getAmplitude(), id));
		}
		
		trackMelody.put(currentSixteenths, musicalInstants);
	}

	public void addInstant(NoteDuration noteDuration, Envelope envelope, Pitch... note) {
		int currentSixteenths = trackLengthInSixteenths();
		List<MusicalInstant> musicalInstants = new ArrayList<>();
		for (int i = 0; i < note.length; i++) {
			String id = getIdPrefix() + currentSixteenths + ": " + i;
			musicalInstants.add(new MusicalInstant(noteDuration, note[i], getSynth(), envelope, getAmplitude(), id));
		}
		
		trackMelody.put(currentSixteenths, musicalInstants);
	}

	public boolean hasNext() {
		return true;
	}

	@Override
	public List<TimedMusicalInstant> getMusicalInstantsActiveNow(TimeInstant time) {
		if(trackMelody.isEmpty()) {
			throw new IllegalStateException("Can't call getMusicalInstantsActiveNow when Track melody empty");
		}
		
		List<TimedMusicalInstant> activeMusicalInstants = new ArrayList<>();
		int currentAbsoluteSixteenth = time.getTotalSixteenths();
		
		for (Iterator<Entry<Integer, List<MusicalInstant>>> it = trackMelody.entrySet().iterator(); it.hasNext();) {
			Entry<Integer, List<MusicalInstant>> keyedMusicalInstants = it.next();
			int relativeStartingSixteenth = keyedMusicalInstants.getKey();
			
			for (Iterator<MusicalInstant> instantIt = keyedMusicalInstants.getValue().iterator(); instantIt.hasNext();) {
				MusicalInstant instant = instantIt.next();
				
				if(isActiveNowOrInOneSixteenth(currentAbsoluteSixteenth, repetitionSixteenth, trackLengthInSixteenths(), relativeStartingSixteenth, instant.getDuration().getDurationInSixteenths())) {
					int absoluteStartSixteenth = absoluteStartSixteenth(currentAbsoluteSixteenth, repetitionSixteenth, trackLengthInSixteenths(), relativeStartingSixteenth);
					activeMusicalInstants.add(new TimedMusicalInstant(absoluteStartSixteenth, instant));
				}
			}
		}
		
		return activeMusicalInstants;
	}
	
	public static boolean isActiveNowOrInOneSixteenth(int absoluteSixteenth, int loopSixteenth, int trackDuration, int relativeStartingSixteeth, int noteDuration) {
		return
				isActive(absoluteSixteenth, loopSixteenth, trackDuration, relativeStartingSixteeth, noteDuration) || 
				isActive(absoluteSixteenth + 1, loopSixteenth, trackDuration, relativeStartingSixteeth, noteDuration);
	}
	
	public static boolean isActive(int absoluteSixteenth, int loopSixteenth, int trackDuration, int relativeStartingSixteenth, int noteDuration) {
		//Check notes that start before loop
		if(relativeStartingSixteenth < loopSixteenth) {
			return absoluteSixteenth >= relativeStartingSixteenth && absoluteSixteenth < relativeStartingSixteenth + noteDuration;
		}
		
		int loopCount = (absoluteSixteenth - loopSixteenth) / (trackDuration - loopSixteenth);
		if(loopCount < 0) {
			return false;
		}
		int absoluteStartingSixteenth = relativeStartingSixteenth + loopCount * (trackDuration - loopSixteenth);
		
		return absoluteSixteenth >= absoluteStartingSixteenth && absoluteSixteenth < absoluteStartingSixteenth + noteDuration;
	}
	
	public static int absoluteStartSixteenth(int absoluteSixteenth, int loopSixteenth, int trackDuration, int relativeStartingSixteeth) {
		int loopCount = (absoluteSixteenth - loopSixteenth) / (trackDuration - loopSixteenth);
		return relativeStartingSixteeth + loopCount * (trackDuration - loopSixteenth);
	}
	
	@Override
	public int trackLengthInSixteenths() {
		if(trackMelody.isEmpty()) {
			return 0;
		}
		else {
			int lastInstantsSixteenth = trackMelody.lastKey();
			return lastInstantsSixteenth + trackMelody.get(lastInstantsSixteenth).get(0).getDuration().getDurationInSixteenths();
		}
	}
	
	public int lengthInBeats() {
		int length = trackLengthInSixteenths();
		
		int lengthInBeats = length/NoteDuration.QUARTER.getDurationInSixteenths();
		if(length%NoteDuration.QUARTER.getDurationInSixteenths() != 0) {
			lengthInBeats++;
		}
		
		return lengthInBeats;
	}


	public void setInstrument(Instrument instrument) {
		setSynth(instrument.synth());
		setEnvelope(instrument.envelope());
	}
	
	@Override
	public Synth getSynth()
	{
		return synth;
	}
	public void setSynth(Synth synth) {
		this.synth = synth;
	}
	
	@Override
	public Envelope getEnvelope()
	{
		return envelope;
	}
	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}
	
	@Override
	public float getAmplitude()
	{
		return amplitude;
	}
	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}
	
}
