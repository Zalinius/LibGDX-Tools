package com.darzalgames.libgdxtools.audio.composition.track;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.audio.AudioTestUtils;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;
import com.darzalgames.libgdxtools.audio.time.TimeSignature;

public class SixteenthRhythmTrackTest {
	
	private SixteenthRhythmTrack sixteenthRhythmTrack;
	
	@BeforeEach
	void setup() {
		sixteenthRhythmTrack = new SixteenthRhythmTrack("test song", "test track", AudioTestUtils.testInstrument());
	}
	
	@Test
	void fastRhythm_returns2notesEverySixteenth() throws Exception {
		sixteenthRhythmTrack.setRhythm(SixteenthRhythmTrack.CONSTANT_FAST);
		
		List<TimedMusicalInstant> instantsAt0 = sixteenthRhythmTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 0));
		List<TimedMusicalInstant> instantsAt1 = sixteenthRhythmTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 1));
		List<TimedMusicalInstant> instantsAt2 = sixteenthRhythmTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 2));
		List<TimedMusicalInstant> instantsAt3 = sixteenthRhythmTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 3));
		List<TimedMusicalInstant> instantsAt4 = sixteenthRhythmTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 4));
		List<TimedMusicalInstant> instantsAt5 = sixteenthRhythmTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 5));

		assertEquals(2, instantsAt0.size());
		assertEquals(2, instantsAt1.size());
		assertEquals(2, instantsAt2.size());
		assertEquals(2, instantsAt3.size());
		assertEquals(2, instantsAt4.size());
		assertEquals(2, instantsAt5.size());
	}
	
	@Test
	void fastRhythm_withDelay_returnsNoNotesBefore_1noteOnDelay_2NothesThereafter() throws Exception {
		sixteenthRhythmTrack.setRhythm(SixteenthRhythmTrack.CONSTANT_FAST);
		sixteenthRhythmTrack.setInitialDelay(1);
		
		List<TimedMusicalInstant> instantsAt0 = sixteenthRhythmTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 0));
		List<TimedMusicalInstant> instantsAt1 = sixteenthRhythmTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 1));
		List<TimedMusicalInstant> instantsAt2 = sixteenthRhythmTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 2));
		List<TimedMusicalInstant> instantsAt3 = sixteenthRhythmTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 3));
		List<TimedMusicalInstant> instantsAt4 = sixteenthRhythmTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 4));
		List<TimedMusicalInstant> instantsAt5 = sixteenthRhythmTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 5));

		assertEquals(0, instantsAt0.size());
		assertEquals(0, instantsAt1.size());
		assertEquals(0, instantsAt2.size());
		assertEquals(1, instantsAt3.size());
		assertEquals(2, instantsAt4.size());
		assertEquals(2, instantsAt5.size());
	}

}
