package com.darzalgames.libgdxtools.audio.composition.track;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RepeatingTrackMathTest {
	
	@Test
	void isActive_withTrackLength4() throws Exception {
		int trackLength = 4;
		int loopSixteenth = 0;
		
		int noteStart = 0;
		int noteDuration = 2;
		assertTrue(RepeatingTrack.isActive(0, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActive(1, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActive(2, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActive(3, loopSixteenth, trackLength, noteStart, noteDuration));
	}
	
	@Test
	void isActive_withTrackLength4AndLoopAtBeginning() throws Exception {
		int trackLength = 4;
		int loopSixteenth = 0;
		
		int noteStart = 0;
		int noteDuration = 2;
		assertTrue(RepeatingTrack.isActive(0, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActive(1, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActive(2, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActive(3, loopSixteenth, trackLength, noteStart, noteDuration));
		
		assertTrue(RepeatingTrack.isActive(4, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActive(5, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActive(6, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActive(7, loopSixteenth, trackLength, noteStart, noteDuration));
	}
	
	@Test
	void isActiveNow_withTrackLength16AndLoopAt8_afterALoop() throws Exception {
		int trackLength = 16;
		int loopSixteenth = 8;
		
		int noteStart = 8;
		int noteDuration = 4;

		assertTrue(RepeatingTrack.isActive(16, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActive(17, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActive(18, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActive(19, loopSixteenth, trackLength, noteStart, noteDuration));
	}

	
	@Test
	void isActiveNowOrInOneSixteenth_withTrackLength4() throws Exception {
		int trackLength = 4;
		int loopSixteenth = 0;
		
		int noteStart = 0;
		int noteDuration = 2;
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(0, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(1, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(2, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(3, loopSixteenth, trackLength, noteStart, noteDuration));
	}
	
	@Test
	void isActiveNowOrInOneSixteenth_withTrackLength4AndLoopAtBeginning() throws Exception {
		int trackLength = 4;
		int loopSixteenth = 0;
		
		int noteStart = 0;
		int noteDuration = 2;
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(0, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(1, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(2, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(3, loopSixteenth, trackLength, noteStart, noteDuration));
		
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(4, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(5, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(6, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(7, loopSixteenth, trackLength, noteStart, noteDuration));
	}
	
	@Test
	void isActiveNowOrInOneSixteenth_withTrackLength4AndLoopAtBeginningAndMidLoopNote() throws Exception {
		int trackLength = 4;
		int loopSixteenth = 0;
		
		int noteStart = 2;
		int noteDuration = 2;
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(0, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(1, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(2, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(3, loopSixteenth, trackLength, noteStart, noteDuration));
		
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(4, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(5, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(6, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(7, loopSixteenth, trackLength, noteStart, noteDuration));
	}

	
	@Test
	void isActiveNowOrInOneSixteenth_withTrackLength4AndLoopAtBeginning1LengthNote() throws Exception {
		int trackLength = 4;
		int loopSixteenth = 0;
		
		int noteStart = 0;
		int noteDuration = 1;
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(0, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(1, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(2, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(3, loopSixteenth, trackLength, noteStart, noteDuration));
		
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(4, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(5, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(6, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(7, loopSixteenth, trackLength, noteStart, noteDuration));
	}

	@Test
	void isActiveNowOrInOneSixteenth_withTrackLength4AndLoopAtBeginning1LengthNoteAt1() throws Exception {
		int trackLength = 4;
		int loopSixteenth = 0;
		
		int noteStart = 1;
		int noteDuration = 1;
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(0, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(1, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(2, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(3, loopSixteenth, trackLength, noteStart, noteDuration));
		
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(4, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(5, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(6, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(7, loopSixteenth, trackLength, noteStart, noteDuration));
	}

	@Test
	void isActiveNowOrInOneSixteenth_withTrackLength4AndLoopAtBeginning1LengthNoteAt2() throws Exception {
		int trackLength = 4;
		int loopSixteenth = 0;
		
		int noteStart = 2;
		int noteDuration = 1;
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(0, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(1, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(2, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(3, loopSixteenth, trackLength, noteStart, noteDuration));
		
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(4, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(5, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(6, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(7, loopSixteenth, trackLength, noteStart, noteDuration));
	}

	@Test
	void isActiveNowOrInOneSixteenth_withTrackLength4AndLoopAtBeginning1LengthNoteAt3() throws Exception {
		int trackLength = 4;
		int loopSixteenth = 0;
		
		int noteStart = 3;
		int noteDuration = 1;
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(0, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(1, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(2, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(3, loopSixteenth, trackLength, noteStart, noteDuration));
		
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(4, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(5, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(6, loopSixteenth, trackLength, noteStart, noteDuration));
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(7, loopSixteenth, trackLength, noteStart, noteDuration));
	}

	@Test
	void isActiveNowOrInOneSixteenth_withTrackLength4AndLoopAtBeginning1LengthNotePreLoopNote() throws Exception {
		int trackLength = 4;
		int loopSixteenth = 2;
		
		int noteStart = 0;
		int noteDuration = 1;
		assertTrue(RepeatingTrack.isActiveNowOrInOneSixteenth(0, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(1, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(2, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(3, loopSixteenth, trackLength, noteStart, noteDuration));
		
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(4, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(5, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(6, loopSixteenth, trackLength, noteStart, noteDuration));
		assertFalse(RepeatingTrack.isActiveNowOrInOneSixteenth(7, loopSixteenth, trackLength, noteStart, noteDuration));
	}


	@Test
	void absoluteStartingSixteenth_withTrackLength16AndLoopAt8_afterALoop() throws Exception {
		int trackLength = 16;
		int loopSixteenth = 8;
		
		int noteStart = 8;
		int noteDuration = 4;

		assertEquals(16, RepeatingTrack.absoluteStartSixteenth(16, loopSixteenth, trackLength, noteStart));
		assertEquals(16, RepeatingTrack.absoluteStartSixteenth(17, loopSixteenth, trackLength, noteStart));
		assertEquals(16, RepeatingTrack.absoluteStartSixteenth(18, loopSixteenth, trackLength, noteStart));
		assertEquals(16, RepeatingTrack.absoluteStartSixteenth(19, loopSixteenth, trackLength, noteStart));
	}


	
}
