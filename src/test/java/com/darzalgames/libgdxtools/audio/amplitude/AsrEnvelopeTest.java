package com.darzalgames.libgdxtools.audio.amplitude;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AsrEnvelopeTest {

	@Test
	void getEnvelopeOutsideBoundsReturns0() throws Exception {
		Envelope envelope = new AsrEnvelope(1f, 1f, 1f);
		
		assertEquals(0f, envelope.getEnvelope(3, -1f));
		assertEquals(0f, envelope.getEnvelope(3,  0f));
		assertEquals(0f, envelope.getEnvelope(3,  3f));
		assertEquals(0f, envelope.getEnvelope(3,  4f));
	}

	@Test
	void getEnvelopeAtSustainLevelDuringSustainAttackAndRelease() throws Exception {
		Envelope envelope = new AsrEnvelope(1f, 1f, 1f);
		
		assertEquals(1f, envelope.getEnvelope(5, 1f));
		assertEquals(1f, envelope.getEnvelope(5, 2f));
		assertEquals(1f, envelope.getEnvelope(5, 3f));
		assertEquals(1f, envelope.getEnvelope(5, 4f));
	}

	@Test
	void getEnvelopeScalesLinearlyToSustainLevelBetweenAttackAndSustain() throws Exception {
		Envelope envelope = new AsrEnvelope(1f, 0.4f, 1f);
		
		assertEquals(0.0f, envelope.getEnvelope(5, 0.0f));
		assertEquals(0.2f, envelope.getEnvelope(5, 0.5f));
		assertEquals(0.4f, envelope.getEnvelope(5, 1.0f));
	}

	@Test
	void getEnvelopeScalesLinearlyFromSustainLevelBetweenSustainAndRelease() throws Exception {
		Envelope envelope = new AsrEnvelope(1f, 0.4f, 1f);
		
		assertEquals(0.4f, envelope.getEnvelope(5, 4.0f), 0.0001);
		assertEquals(0.2f, envelope.getEnvelope(5, 4.5f), 0.0001);
		assertEquals(0.0f, envelope.getEnvelope(5, 5.0f));
	}

}
