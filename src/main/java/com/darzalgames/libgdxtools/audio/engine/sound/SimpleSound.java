package com.darzalgames.libgdxtools.audio.engine.sound;

import java.util.function.UnaryOperator;

import com.darzalgames.libgdxtools.audio.amplitude.Envelope;
import com.darzalgames.libgdxtools.audio.synth.Synth;

/**
 * A simple sound is made up of:
 * - A Synth, which is really just a function f(x), an abstract wave function, that gives a wave position for an x between 0 and 1
 * - A fixed duration in seconds
 * - An overall amplitude or volume, which is a constant usually between 0 and 1 
 * - A Frequency Function, which returns a specific frequency for a specific time t. Generally, the function should be continuous
 * - An amplitude envelope, which modulates the amplitude
 */
public class SimpleSound {

	private final Synth timbre;
    private	final float duration; // in seconds
    private	final float amplitude;
    private	final UnaryOperator<Float> frequencyFunction;
	private	final Envelope envelope;
	private final String id;
	
	public SimpleSound(Synth timbre, float duration, float amplitude, float frequency, Envelope envelope, String id) {
		this(timbre, duration, amplitude, t -> frequency, envelope, id);
	}
	
	public SimpleSound(Synth timbre, float duration, float amplitude, UnaryOperator<Float> frequencyFunction, Envelope envelope, String id) {
		this.timbre = timbre;
		this.duration = duration;
		this.amplitude = amplitude;
		this.frequencyFunction = frequencyFunction;
		this.envelope = envelope;		
		this.id = id;
	}
	
	public float computeAmplitude(float time) {
		return amplitude * envelope.getEnvelope(duration, time);
	}
	public float computeFrequency(float time) {
		return frequencyFunction.apply(time);
	}

	
	public Synth getTimbre() {
		return timbre;
	}
	public float getDuration() {
		return duration;
	}
	public float getAmplitude() {
		return amplitude;
	}
	public UnaryOperator<Float> getFrequencyFunction() {
		return frequencyFunction;
	}
	public Envelope getEnvelope() {
		return envelope;
	}

	public String getId() {
		return id;
	}
	

}
