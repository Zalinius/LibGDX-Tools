package com.darzalgames.libgdxtools.audio.amplitude;

public interface Envelope {
	/**
	 * Computes the value for an envelope at a certain, given an envelope length.
	 * @param envelopeDuration The length of the envelope in seconds
	 * @param currentTime The time to compute the value at in seconds
	 * @return An envelope value, typically in the interval [0,1]
	 */
	public float getEnvelope(float envelopeDuration, float currentTime);
}
