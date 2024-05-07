package com.darzalgames.libgdxtools.audio.amplitude;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ArEnvelopeTest {
	
	@Test
	void getEnvelopeOutsideBoundsReturns0() throws Exception {
		Envelope envelope = new ArEnvelope(1f, 1f);
		
		assertEquals(0f, envelope.getEnvelope(2, -1f));
		assertEquals(0f, envelope.getEnvelope(2,  0f));
		assertEquals(0f, envelope.getEnvelope(2,  2f));
		assertEquals(0f, envelope.getEnvelope(2,  3f));
	}

	@Test
	void getEnvelope1BetweenAttackAndRelease() throws Exception {
		Envelope envelope = new ArEnvelope(1f, 1f);
		
		assertEquals(1f, envelope.getEnvelope(2, 1f));
	}

	@Test
	void getEnvelopeScalesLinearlyForAttackAndRelease() throws Exception {
		Envelope envelope = new ArEnvelope(1f, 1f);
		
		assertEquals(0.5f, envelope.getEnvelope(2, 0.5f));
		assertEquals(0.5f, envelope.getEnvelope(2, 1.5f));
	}

	@Test
	void getEnvelopeWith0AttackTimeStartsAt1AndThenScalesDown() throws Exception {
		Envelope envelope = new ArEnvelope(0f, 1f);
		
		assertEquals(1f, envelope.getEnvelope(1, 0));
		assertEquals(0.5f, envelope.getEnvelope(1, 0.5f));
		assertEquals(0f, envelope.getEnvelope(1, 1f));
	}

	@Test
	void getEnvelopeWith0ReleaseTimeStartsAt0ThenScalesUp() throws Exception {
		Envelope envelope = new ArEnvelope(1f, 0f);
		
		assertEquals(0f, envelope.getEnvelope(1, 0));
		assertEquals(0.5f, envelope.getEnvelope(1, 0.5f));
		assertEquals(0.999f, envelope.getEnvelope(1, 0.999f));
		assertEquals(0f, envelope.getEnvelope(1, 1f));
	}

	@Test
	void getEnvelopeWith0Attack0ReleaseTimeStartsIs0() throws Exception {
		Envelope envelope = new ArEnvelope(0f, 0f);
		
		assertEquals(0f, envelope.getEnvelope(0, 0));
	}

}
