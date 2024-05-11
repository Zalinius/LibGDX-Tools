package com.darzalgames.libgdxtools.audio.composition;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PitchTest {
	
	@Test
	void pitchUpReturnsHigherNaturalPitch() throws Exception {
		Pitch higher = Pitch.C4.up();
		
		assertEquals(Pitch.D4, higher);
		assertTrue(higher.isNatural());
	}

	@Test
	void pitchDownReturnsLowerNaturalPitch() throws Exception {
		Pitch lower = Pitch.C4.down();
		
		assertEquals(Pitch.B3, lower);
		assertTrue(lower.isNatural());
	}

	@Test
	void pitchOctaveUpReturnsHigherOctaveNaturalPitch() throws Exception {
		Pitch higher = Pitch.C4.octaveUp();
		
		assertEquals(Pitch.C5, higher);
		assertTrue(higher.isNatural());
	}

	@Test
	void pitchOctaveDownReturnsOctaveLowerNaturalPitch() throws Exception {
		Pitch lower = Pitch.C4.octaveDown();
		
		assertEquals(Pitch.C3, lower);
		assertTrue(lower.isNatural());
	}
	
	@Test
	void makePitchMakesUnnaturalPitch() throws Exception {
		Pitch newPitch = Pitch.makePitch(200f);
		
		assertEquals(200f, newPitch.getFrequency());
		assertFalse(newPitch.isNatural());
	}
	
	@Test
	void getLowerOnLowestPitchReturnsSameNote() throws Exception {
		Pitch lowest = Pitch.C1;
		
		assertEquals(lowest, lowest.down());
	}

	@Test
	void getHigherOnHighestPitchReturnsSameNote() throws Exception {
		Pitch highest = Pitch.B6;
		
		assertEquals(highest, highest.up());
	}
	
	@Test
	void octaveDownOnNotesOnBottomOctaveReturnsSameNotes() throws Exception {
		assertEquals(Pitch.C1, Pitch.C1.octaveDown());
		assertEquals(Pitch.D1, Pitch.D1.octaveDown());
		assertEquals(Pitch.E1, Pitch.E1.octaveDown());
		assertEquals(Pitch.F1, Pitch.F1.octaveDown());
		assertEquals(Pitch.G1, Pitch.G1.octaveDown());
		assertEquals(Pitch.A1, Pitch.A1.octaveDown());
		assertEquals(Pitch.B1, Pitch.B1.octaveDown());

		assertEquals(Pitch.C1, Pitch.C2.octaveDown());
	}

	@Test
	void octaveUpOnNotesOnTopOctaveReturnsSameNotes() throws Exception {
		assertEquals(Pitch.C6, Pitch.C6.octaveUp());
		assertEquals(Pitch.D6, Pitch.D6.octaveUp());
		assertEquals(Pitch.E6, Pitch.E6.octaveUp());
		assertEquals(Pitch.F6, Pitch.F6.octaveUp());
		assertEquals(Pitch.G6, Pitch.G6.octaveUp());
		assertEquals(Pitch.A6, Pitch.A6.octaveUp());
		assertEquals(Pitch.B6, Pitch.B6.octaveUp());

		assertEquals(Pitch.B6, Pitch.B5.octaveUp());
	}

}
