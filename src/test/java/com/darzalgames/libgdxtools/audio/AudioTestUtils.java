package com.darzalgames.libgdxtools.audio;

import com.badlogic.gdx.audio.AudioDevice;
import com.darzalgames.libgdxtools.audio.amplitude.Envelope;
import com.darzalgames.libgdxtools.audio.composition.Instrument;
import com.darzalgames.libgdxtools.audio.synth.Synth;
import com.darzalgames.libgdxtools.preferences.InMemoryPreferences;
import com.darzalgames.libgdxtools.preferences.PreferenceManager;
import com.darzalgames.libgdxtools.preferences.SoundPreference;

public class AudioTestUtils {
	
	public static Instrument testInstrument() {
		Synth synth = Synth.sine;
		Envelope envelope = (duration, time) -> 1f;
		return new Instrument(synth, envelope);
	}
	
	public static AudioDevice audioDeviceSpy(float[] internalBuffer) {
		return new AudioDevice() {
			
			private int internalIndex = 0;
			
			
			@Override
			public void writeSamples(float[] samples, int offset, int numSamples) {
				for (int i = 0; i < samples.length; i++) {
					internalBuffer[i + internalIndex] = samples[i];
				}
				internalIndex += samples.length;
			}
			
			@Override
			public void writeSamples(short[] samples, int offset, int numSamples) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setVolume(float volume) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void resume() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void pause() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isMono() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int getLatency() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void dispose() {
			}
		};
	}
	
	public static SoundPreference soundPreferenceDummy() {
		PreferenceManager inMemoryPreferenceManager = new PreferenceManager(new InMemoryPreferences());
		return inMemoryPreferenceManager.sound();
	}
}
