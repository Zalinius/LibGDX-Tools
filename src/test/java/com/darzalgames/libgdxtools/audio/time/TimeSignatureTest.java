package com.darzalgames.libgdxtools.audio.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TimeSignatureTest {
	
	@Test
	void time4_4_signatureCalculatesBeatsAndMeasuresAsExpected() throws Exception {
		TimeSignature timeSignature = TimeSignature.TIME_4_4;
		
		assertEquals(0, timeSignature.getMeasure(0));
		assertEquals(0, timeSignature.getMeasure(3));
		assertEquals(1, timeSignature.getMeasure(4));
		assertEquals(1, timeSignature.getMeasure(7));
		assertEquals(4, timeSignature.getMeasure(16));
		
		assertEquals(0, timeSignature.getBeatInMeasure(0));
		assertEquals(3, timeSignature.getBeatInMeasure(3));
		assertEquals(0, timeSignature.getBeatInMeasure(4));
		assertEquals(3, timeSignature.getBeatInMeasure(7));
		assertEquals(0, timeSignature.getBeatInMeasure(16));
		assertEquals(1, timeSignature.getBeatInMeasure(17));
		assertEquals(2, timeSignature.getBeatInMeasure(18));
		assertEquals(3, timeSignature.getBeatInMeasure(19));
	}

	@Test
	void time3_4_signatureCalculatesBeatsAndMeasuresAsExpected() throws Exception {
		TimeSignature timeSignature = TimeSignature.TIME_3_4;
		
		assertEquals(0, timeSignature.getMeasure(0));
		assertEquals(0, timeSignature.getMeasure(1));
		assertEquals(0, timeSignature.getMeasure(2));
		assertEquals(1, timeSignature.getMeasure(3));
		assertEquals(1, timeSignature.getMeasure(4));
		assertEquals(2, timeSignature.getMeasure(7));
		assertEquals(5, timeSignature.getMeasure(15));
		
		assertEquals(0, timeSignature.getBeatInMeasure(0));
		assertEquals(0, timeSignature.getBeatInMeasure(3));
		assertEquals(1, timeSignature.getBeatInMeasure(4));
		assertEquals(0, timeSignature.getBeatInMeasure(15));
		assertEquals(1, timeSignature.getBeatInMeasure(16));
		assertEquals(2, timeSignature.getBeatInMeasure(17));
	}
	
	@Test
	void time5_4_signatureCalculatesBeatsAndMeasuresAsExpected() throws Exception {
		TimeSignature timeSignature = TimeSignature.TIME_5_4;
		
		assertEquals(0, timeSignature.getMeasure(0));
		assertEquals(0, timeSignature.getMeasure(4));
		assertEquals(1, timeSignature.getMeasure(5));
		assertEquals(1, timeSignature.getMeasure(7));
		assertEquals(3, timeSignature.getMeasure(15));
		
		assertEquals(0, timeSignature.getBeatInMeasure(0));
		assertEquals(0, timeSignature.getBeatInMeasure(15));
		assertEquals(1, timeSignature.getBeatInMeasure(16));
		assertEquals(2, timeSignature.getBeatInMeasure(17));
		assertEquals(3, timeSignature.getBeatInMeasure(18));
		assertEquals(4, timeSignature.getBeatInMeasure(19));
	}

	@Test
	void customTimeSignatureCalculatesBeatsAndMeasuresAsExpected() throws Exception {
		TimeSignature timeSignature = TimeSignature.makeSimpleTimeSignature(7);
		
		assertEquals(0, timeSignature.getMeasure(0));
		assertEquals(0, timeSignature.getMeasure(0));
		assertEquals(1, timeSignature.getMeasure(7));
		
		assertEquals(0, timeSignature.getBeatInMeasure(0));
		assertEquals(1, timeSignature.getBeatInMeasure(1));
		assertEquals(2, timeSignature.getBeatInMeasure(2));
		assertEquals(3, timeSignature.getBeatInMeasure(3));
		assertEquals(4, timeSignature.getBeatInMeasure(4));
		assertEquals(5, timeSignature.getBeatInMeasure(5));
		assertEquals(6, timeSignature.getBeatInMeasure(6));
		assertEquals(0, timeSignature.getBeatInMeasure(0));
	}

}
