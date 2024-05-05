package com.darzalgames.libgdxtools.audio.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TimeInstantTest {
	
	@Test
	void nextOnTimeInstantReturnsOneSixteenthLater() throws Exception {
		TimeInstant timeInstant = new TimeInstant(0);
		
		TimeInstant timeInstant2 = timeInstant.next();
		TimeInstant timeInstant3 = timeInstant2.next();
		TimeInstant timeInstant3Alt = timeInstant.next().next();
		
		assertEquals(1, timeInstant2.getTotalSixteenths());
		assertEquals(2, timeInstant3.getTotalSixteenths());
		assertEquals(2, timeInstant3Alt.getTotalSixteenths());
		assertEquals(timeInstant3, timeInstant3Alt);
		assertNotEquals(timeInstant2, timeInstant3Alt);
	}
	
	@Test
	void getTotalBeats() throws Exception {
		TimeInstant timeInstant = new TimeInstant(0);
		
		assertEquals(0, timeInstant.getTotalBeats());
		assertEquals(0, timeInstant.next().getTotalBeats());
		assertEquals(0, timeInstant.next().next().getTotalBeats());
		assertEquals(0, timeInstant.next().next().next().getTotalBeats());
		assertEquals(1, timeInstant.next().next().next().next().getTotalBeats());
	}


	@Test
	void isStartOfBeat() throws Exception {
		TimeInstant timeInstant0 = new TimeInstant(0);
		TimeInstant timeInstant1 = new TimeInstant(1);
		TimeInstant timeInstant2 = new TimeInstant(2);
		TimeInstant timeInstant3 = new TimeInstant(3);
		TimeInstant timeInstant4 = new TimeInstant(4);
		
		assertTrue(timeInstant0.isStartOfBeat());
		assertFalse(timeInstant1.isStartOfBeat());
		assertFalse(timeInstant2.isStartOfBeat());
		assertFalse(timeInstant3.isStartOfBeat());
		assertTrue(timeInstant4.isStartOfBeat());
	}

}
