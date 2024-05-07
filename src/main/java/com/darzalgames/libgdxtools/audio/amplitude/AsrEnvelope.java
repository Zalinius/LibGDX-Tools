package com.darzalgames.libgdxtools.audio.amplitude;

/**
 * An envelope with an Attack period and a Release period, as well as a sustain period of undefined length between the two.
 */
public class AsrEnvelope implements Envelope {
	
	private final float attackTime;
	private final float sustainLevel;
	private final float releaseTime;
	
	
	/**
	 * @param attackTime The upward attack time in seconds, can be 0
	 * @param sustainLevel The amplitude level during the sustain period, within [0,1]
	 * @param releaseTime The downward release time in seconds, can be 0
	 */
	public AsrEnvelope(float attackTime, float sustainLevel, float releaseTime) {
		this.attackTime = attackTime;
		this.sustainLevel = sustainLevel;
		this.releaseTime = releaseTime;
	}
	
	public float getEnvelope(float envelopeDuration, float currentTime) {
		if(currentTime < 0f || currentTime >= envelopeDuration) {
			return 0f;
		}
		
		float sustainTime = envelopeDuration - attackTime - releaseTime;
		
		if(currentTime < attackTime) {
			return sustainLevel * currentTime / attackTime;
		}
		else if (currentTime < attackTime + sustainTime) {
			return sustainLevel;
		}
		else {
			return -sustainLevel/releaseTime*currentTime + sustainLevel/releaseTime*(envelopeDuration);
		}

	}
	
	
}
