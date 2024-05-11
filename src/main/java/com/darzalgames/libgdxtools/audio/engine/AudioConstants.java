package com.darzalgames.libgdxtools.audio.engine;

public class AudioConstants {
	public static final int SAMPLING_RATE = 44100; //hz
	public static final int SAMPLES_PER_STEP = SAMPLING_RATE/60;
	
	public static final float SAMPLE_DURATION = 1 / (float)SAMPLING_RATE;
	public static final float STEP_DURATION = SAMPLES_PER_STEP / (float)SAMPLING_RATE;
}
