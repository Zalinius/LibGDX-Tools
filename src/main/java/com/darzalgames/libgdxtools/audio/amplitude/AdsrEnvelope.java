package com.darzalgames.libgdxtools.audio.amplitude;

/**
 * An envelope with an Attack period, a decay period, an interpolated sustain period, and a Release period. It's length is defined on a per call basis
 */
public class AdsrEnvelope implements Envelope {
	
	private final float attackTime; //in seconds
	private final float decayTime; //in seconds
	private final float sustainLevel; //as an amplitude ratio (between 0 and 1)
	private final float releaseTime; //in seconds
	
	
	/**
	 * @param attackTime The upward attack time in seconds, can be 0
	 * @param decayTime The downward decay time in seconds, can be 0
	 * @param sustainLevel The amplitude level during the sustain period, within [0,1]
	 * @param releaseTime The downward release time in seconds, can be 0
	 */
	public AdsrEnvelope(float attackTime, float decayTime, float sustainLevel, float releaseTime) {
		this.attackTime = attackTime;
		this.decayTime = decayTime;
		this.sustainLevel = sustainLevel;
		this.releaseTime = releaseTime;
	}

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
