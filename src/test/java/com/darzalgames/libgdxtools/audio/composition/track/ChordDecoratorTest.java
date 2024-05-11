package com.darzalgames.libgdxtools.audio.composition.track;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.audio.AudioTestUtils;
import com.darzalgames.libgdxtools.audio.composition.NoteDuration;
import com.darzalgames.libgdxtools.audio.composition.Pitch;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;
import com.darzalgames.libgdxtools.audio.time.TimeSignature;

class ChordDecoratorTest {

	private ChordDecorator chordDecorator;
	
	@BeforeEach
	void setup() {
		RepeatingTrack repeatingTrack = new RepeatingTrack(AudioTestUtils.testInstrument(), "song name", "track");
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C4);
		chordDecorator = new ChordDecorator(repeatingTrack, "song name");
	}
	
	@Test
	void noChording_leavesNoteUnchanged() throws Exception {
		List<TimedMusicalInstant> timedMusicalInstants = chordDecorator.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 0));
		
		assertEquals(1, timedMusicalInstants.size());
		assertEquals(Pitch.C4, timedMusicalInstants.get(0).musicalInstant.getNote());
	}
	
	@Test
	void setTransposeToOctave_twoNoteChord() throws Exception {
		chordDecorator.setSimpleChordFunction(p -> p.octaveUp());
		List<TimedMusicalInstant> timedMusicalInstants = chordDecorator.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 0));
		Set<Pitch> pitches = timedMusicalInstants.stream().map(mi -> mi.musicalInstant.getNote()).collect(Collectors.toSet());
		
		assertEquals(2, timedMusicalInstants.size());
		assertTrue(pitches.containsAll(Set.of(Pitch.C4, Pitch.C5)));
	}

	@Test
	void setTransposeToCChord_threeNoteChord() throws Exception {
		chordDecorator.setChordFunction(p -> ChordDecorator.easySortedSet(p, p.up().up(), p.up().up().up().up()));
		List<TimedMusicalInstant> timedMusicalInstants = chordDecorator.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 0));
		Set<Pitch> pitches = timedMusicalInstants.stream().map(mi -> mi.musicalInstant.getNote()).collect(Collectors.toSet());
		
		assertEquals(3, timedMusicalInstants.size());
		assertTrue(pitches.containsAll(Set.of(Pitch.C4, Pitch.E4, Pitch.G4)));
	}

}
