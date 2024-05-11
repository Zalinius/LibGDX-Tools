package com.darzalgames.libgdxtools.audio.engine.samples;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.MathUtils;
import com.darzalgames.libgdxtools.audio.engine.AudioConstants;
import com.darzalgames.libgdxtools.audio.engine.sound.SimpleSound;
import com.darzalgames.libgdxtools.audio.engine.sound.TimedSimpleSound;

public class SampleMakerImpl implements SampleMaker {
	
	private Map<String, Float> phaseMap;
	
	public SampleMakerImpl() {
		phaseMap = new HashMap<>();
	}

	@Override
	public float[] makeSamples(List<TimedSimpleSound> simpleSounds, float volume, final int sampleCount, final float sampleDuration, final float samplingStartTime) {
		
		float[] sampleBuffer = new float[sampleCount];
		

		for (Iterator<TimedSimpleSound> it = simpleSounds.iterator(); it.hasNext();) {
			TimedSimpleSound timedSimpleSound = it.next();
			SimpleSound simpleSound = timedSimpleSound.simpleSound;
			
			float phaseAtMinus1 = phaseMap.getOrDefault(simpleSound.getId(), 0f);
			float alpha = phaseAtMinus1;
			float beta = computeBeta(samplingStartTime, timedSimpleSound);
			float phi = alpha - beta;
			
			for (int i = 0; i < sampleBuffer.length; i++) {
				float t = samplingStartTime - timedSimpleSound.startingTime + i * AudioConstants.SAMPLE_DURATION;
				
				float amplitude = simpleSound.computeAmplitude(t);
				float frequency = simpleSound.computeFrequency(t);
				
				//This is the wave phase, on interval [0,1[
				float waveProgress = frequency * t + phi;
				float moduloedWaveProgress = waveProgress - MathUtils.floor(waveProgress);
				float waveValue = simpleSound.getTimbre().f(moduloedWaveProgress);
				
				sampleBuffer[i] += volume * amplitude * waveValue;
				
				if(i == sampleCount - 1) {
					phaseMap.put(simpleSound.getId(), moduloedWaveProgress);			
				}
			}
		}
		
		return sampleBuffer;
	}
	
	private static float computeBeta(float samplingStartTime, TimedSimpleSound timedSimpleSound) {
		SimpleSound simpleSound = timedSimpleSound.simpleSound;
		float t = samplingStartTime - timedSimpleSound.startingTime + 0 * AudioConstants.SAMPLE_DURATION;
		
		float frequency = simpleSound.computeFrequency(t);
		
		//This is the wave phase, on interval [0,1[
		float waveProgress = frequency * t;
		float moduloedWaveProgress = waveProgress - MathUtils.floor(waveProgress);
		return moduloedWaveProgress;
	}
	
	public static float computeCurrentTimeForSimpleSoundForSampleIndex(final float currentAbsoluteTime, final float absoluteSimpleSoundStartTime, int sampleIndex, float sampleDuration) {
		return (currentAbsoluteTime - absoluteSimpleSoundStartTime) + sampleIndex * sampleDuration;
	}
	
}
