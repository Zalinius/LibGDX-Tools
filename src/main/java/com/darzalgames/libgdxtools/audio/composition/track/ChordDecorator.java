package com.darzalgames.libgdxtools.audio.composition.track;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.darzalgames.libgdxtools.audio.amplitude.Envelope;
import com.darzalgames.libgdxtools.audio.composition.Pitch;
import com.darzalgames.libgdxtools.audio.engine.music.MusicalInstant;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.synth.Synth;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;

/**
 * A decorator that can create chords from single notes. Should not be used with pre-existing chords
 */
public class ChordDecorator extends Track {

	private final Track decoratedTrack;
	private Function<Pitch, SortedSet<Pitch>> chordFunction;
	
	public ChordDecorator(Track decoratedTrack, String songName) {
		super(songName, decoratedTrack.getTrackName() + " chord");
		this.decoratedTrack = decoratedTrack;
		this.resetChordFunction();
	}
	
	public void setChordFunction(Function<Pitch, SortedSet<Pitch>> chordFunction) {
		this.chordFunction = p -> {
			if(p == Pitch.NONE) {
				return easySortedSet(Pitch.NONE);
			}
			else {
				return chordFunction.apply(p);
			}

		};
	}
	
	public void setSimpleChordFunction(UnaryOperator<Pitch> simpleChordFunction) {
		setChordFunction(p -> easySortedSet(p, simpleChordFunction.apply(p)));
	}
	
	public void resetChordFunction() {
		this.chordFunction = p -> easySortedSet(p);
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
		List<TimedMusicalInstant> chordedInstants = new ArrayList<>();
		
		Function<Set<Pitch>, Float> chordAmplification = set -> 1f/(float)(Math.sqrt(set.size()));
		
		for (int i = 0; i < originalInstants.size(); i++) {
			TimedMusicalInstant timedMusicalInstant = originalInstants.get(i);
			MusicalInstant musicalInstant = timedMusicalInstant.musicalInstant;
			
			int pitchIndex = 0;
			SortedSet<Pitch> pitches = chordFunction.apply(musicalInstant.getNote());
			for (Iterator<Pitch> pitchIt = pitches.iterator(); pitchIt.hasNext();) {
				Pitch newPitch = pitchIt.next();
				
				String id = musicalInstant.getId();
				if(pitchIndex != 0) {
					id += " " + pitchIndex;
				}
				
				MusicalInstant pitchedMusicalInstant = new MusicalInstant(musicalInstant.getDuration(), newPitch, musicalInstant.getSynth(), musicalInstant.getEnvelope(), chordAmplification.apply(pitches), id);
				chordedInstants.add(new TimedMusicalInstant(timedMusicalInstant.startingSixteenth, pitchedMusicalInstant));
				
				pitchIndex ++;
			}
		}
		
		return chordedInstants;
		
	}

	@Override
	public int trackLengthInSixteenths() {
		return decoratedTrack.trackLengthInSixteenths();
	}
	
	public static SortedSet<Pitch> easySortedSet(Pitch original, Pitch...pitches){
		Comparator<Pitch> specialPitchComparator = (p1, p2) -> {
			if(p1.equals(original)) {
				return Integer.MIN_VALUE;
			}
			else if(p2.equals(original)) {
				return Integer.MAX_VALUE;
			}
			else {
				return p1.compareTo(p2);
			}
		};
		SortedSet<Pitch> sortedSet = new TreeSet<Pitch>(specialPitchComparator);
		
		sortedSet.add(original);
		for (int i = 0; i < pitches.length; i++) {
			sortedSet.add(pitches[i]);
		}
		
		return sortedSet;
	}
}
