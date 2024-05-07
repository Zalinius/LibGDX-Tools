package com.darzalgames.libgdxtools.audio.amplitude;

/**
 * An envelope with an Attack period and a Release period. It's length is the sum of the two
 */
public class ArEnvelope implements Envelope {

	private final float attackTime; //in seconds
	private final float releaseTime; //in seconds
	
	/**
	 * @param attackTime The upward attack time in seconds, can be 0
	 * @param releaseTime The downward release time in seconds, can be 0
	 */
	public ArEnvelope(float attackTime, float releaseTime) {
		this.attackTime = attackTime;
		this.releaseTime = releaseTime;
	}
	
	public float getEnvelope(float envelopeDuration, float currentTime) {
		if(currentTime < 0f || currentTime >= attackTime + releaseTime) {
			return 0f;
		}
		
		if(currentTime < attackTime) {
			return currentTime / attackTime;
		}
		else {
			float releaseScaledTime = currentTime -attackTime;
			return 1f - releaseScaledTime/releaseTime;
		}
	}
}
