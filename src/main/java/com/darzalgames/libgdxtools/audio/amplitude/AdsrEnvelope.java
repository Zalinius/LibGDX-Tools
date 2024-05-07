package com.darzalgames.libgdxtools.audio.amplitude;

public class AdsrEnvelope implements Envelope {
	
	private final float attackTime; //in seconds
	private final float decayTime; //in seconds
	private final float sustainLevel; //as an amplitude ratio (between 0 and 1)
	private final float releaseTime; //in seconds
	
	
	public AdsrEnvelope(float attackTime, float decayTime, float sustainLevel, float releaseTime) {
		this.attackTime = attackTime;
		this.decayTime = decayTime;
		this.sustainLevel = sustainLevel;
		this.releaseTime = releaseTime;
	}

	/**
	 * Returns the value of the ADSR envelope at a given time in it's life.
	 * As a convenience, if the currentTime is outside the envelope, 0f is returned. 
	 * @param envelopeDuration The duration of the envelope in seconds
	 * @param currentTime The time input of the envelope in seconds
	 * @return The amplitude of the envelope, within [0,1]
	 */
	public float getEnvelope(float envelopeDuration, float currentTime) {
		if(currentTime < 0f || currentTime >= envelopeDuration) {
			return 0f;
		}
		
		float sustainTime = envelopeDuration - attackTime - decayTime - releaseTime;
		
		if(currentTime < attackTime) {
			return currentTime / attackTime;
		}
		else if (currentTime < attackTime + decayTime) {
			return ((sustainLevel - 1) / decayTime) * (currentTime - attackTime) + 1;
		}
		else if (currentTime < attackTime + decayTime + sustainTime) {
			return sustainLevel;
		}
		else {
			return -sustainLevel/releaseTime*currentTime + sustainLevel/releaseTime*(envelopeDuration);
		}
		
	}
	
}
