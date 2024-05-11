package com.darzalgames.libgdxtools.audio.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PeakingTracker {
	
	private Map<Set<String>, Float> peakingInstances;
	
	public PeakingTracker() {
		this.peakingInstances = new HashMap<>();
	}
	
	public void checkForPeaking(float[] samples, Set<String> ids) {
		float peakingMax = 0;
		for (int i = 0; i < samples.length; i++) {
			float absoluteAmplitude = Math.abs(samples[i]);
			if(absoluteAmplitude > 1f) {
				peakingMax = Math.max(peakingMax, absoluteAmplitude);
			}
		}
		
		if(peakingMax > 0) {
			peakingInstances.put(ids, Math.max(peakingMax, peakingInstances.getOrDefault(ids, 0f)));
		}
	}
	
	
	public boolean hasPeaked() {
		return !peakingInstances.isEmpty();
	}
	
	public Map<Set<String>, Float> getPeakingInstances() {
		return peakingInstances;
	}

}
