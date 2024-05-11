package com.darzalgames.libgdxtools.audio.amplitude;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AdsrEnvelopeTest {
	
	@Test
	void getEnvelopeOutsideBoundsReturns0() throws Exception {
		Envelope envelope = new AdsrEnvelope(1f, 1f, 0.5f, 1f);
		
		assertEquals(0f, envelope.getEnvelope(4, -1f));
		assertEquals(0f, envelope.getEnvelope(4,  0f));
		assertEquals(0f, envelope.getEnvelope(4,  4f));
		assertEquals(0f, envelope.getEnvelope(4,  5f));
	}

	@Test
	void getEnvelopeScalesLinearlyBetweenAttackAndDecay() throws Exception {
		Envelope envelope = new AdsrEnvelope(1f, 1f, 0.5f, 1f);
		
		assertEquals(0f, envelope.getEnvelope(4, 0.0f));
		assertEquals(0.5f, envelope.getEnvelope(4, 0.5f));
		assertEquals(1f, envelope.getEnvelope(4, 1.0f));
	}

	@Test
	void getEnvelopeScalesLinearlyToSustainLevelBetweenDecayAndSustain() throws Exception {
		Envelope envelope = new AdsrEnvelope(1f, 1f, 0.5f, 1f);
		
		assertEquals(1f, envelope.getEnvelope(4, 1.0f));
		assertEquals(0.75f, envelope.getEnvelope(4, 1.5f));
		assertEquals(0.5f, envelope.getEnvelope(4, 2.0f));
	}


	@Test
	void getEnvelopeAtSustainLevelDuringSustain() throws Exception {
		Envelope envelope = new AdsrEnvelope(1f, 1f, 0.5f, 1f);
		
		assertEquals(0.5f, envelope.getEnvelope(4, 2f));
		assertEquals(0.5f, envelope.getEnvelope(4, 2.5f));
		assertEquals(0.5f, envelope.getEnvelope(4, 3f));
	}
	
	@Test
	void getEnvelopeScalesLinearlyBetweenSustainAndRelease() throws Exception {
		Envelope envelope = new AdsrEnvelope(1f, 1f, 0.5f, 1f);
		
		assertEquals(0.5f, envelope.getEnvelope(4, 3.0f));
		assertEquals(0.25f, envelope.getEnvelope(4, 3.5f));
		assertEquals(0.0f, envelope.getEnvelope(4, 4.0f));
	}

}
