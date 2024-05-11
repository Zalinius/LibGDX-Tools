package com.darzalgames.libgdxtools.audio.engine.samples;

import java.util.List;

import com.darzalgames.libgdxtools.audio.engine.sound.TimedSimpleSound;

public interface SampleMaker {
	/**
	 * @param simpleSounds
	 * @param volume
	 * @param sampleCount
	 * @param sampleDuration
	 * @param samplingStartTime
	 * @return
	 */
	float[] makeSamples(List<TimedSimpleSound>  simpleSounds, float volume, int sampleCount, float sampleDuration, float samplingStartTime);
}