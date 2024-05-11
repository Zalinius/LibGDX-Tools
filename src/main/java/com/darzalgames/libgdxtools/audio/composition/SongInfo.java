package com.darzalgames.libgdxtools.audio.composition;

public interface SongInfo {
	
	/**
	 * @return the song's starting BPS
	 */
    float getInitialBPS();
    
    /**
     * @return The current (usually fixed) amplitude of the song. Should be between 0 and 1
     */
	float getAmplitude();
	
	/**
	 * @return The song title
	 */
	String getName();
	
}
