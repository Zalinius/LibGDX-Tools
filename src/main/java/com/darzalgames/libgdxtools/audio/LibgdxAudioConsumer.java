package com.darzalgames.libgdxtools.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.darzalgames.zalaudiolibrary.AudioConstants;
import com.darzalgames.zalaudiolibrary.pipeline.zamples.AudioConsumer;

/**
 * A wrapper for libgdx audio devices
 * Requires GDX to be initialized to construct
 */
public class LibgdxAudioConsumer implements AudioConsumer {

	private final AudioDevice audioDevice;

	public LibgdxAudioConsumer() {
		this(true);
	}

	public LibgdxAudioConsumer(boolean mono) {
		audioDevice = Gdx.audio.newAudioDevice(AudioConstants.SAMPLING_RATE, mono);
	}

	@Override
	public void close() throws Exception {
		audioDevice.dispose();
	}

	@Override
	public void writeSamples(float[] samples) {
		audioDevice.writeSamples(samples, 0, samples.length);
	}

}
