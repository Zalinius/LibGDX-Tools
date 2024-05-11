package com.darzalgames.libgdxtools.audio.synth;

import java.util.function.UnaryOperator;

/**
 * A mathematical wave function, with domain [0,1] and range (typically) [-1,1]
 */
public class Synth {

	private final UnaryOperator<Float> waveFunction;

	public Synth(UnaryOperator<Float> waveFunction) {
		this.waveFunction = waveFunction;
	}

	public float f(float x) {
		if (x < 0f || x > 1f) {
			throw new IllegalArgumentException("Argument must be in range [0,1] : " + x);
		}

		return waveFunction.apply(x);
	}

	public static final Synth sine = new Synth(WaveFunctions.getSinWaveFunction());
	public static final Synth sinc = new Synth(WaveFunctions.getSincWaveFunction()); //TODO Seems to produce a clicking sound
	public static final Synth square = new Synth(WaveFunctions.getSquareWaveFunction());
	public static final Synth saw = new Synth(WaveFunctions.getSawtoothWaveFunction());
	public static final Synth triangle = new Synth(WaveFunctions.getTriangleWaveFunction());

	public static Synth bandLimitedSawTooth(int harmonics) {
		return new Synth(WaveFunctions.bandLimitedSawtoothWaveFunction(harmonics));
	}

	public static Synth pulse(float modulation) {
		return new Synth(WaveFunctions.getPulseWaveFunction(modulation));
	}

	public static Synth sinePower(int power) {
		return new Synth(WaveFunctions.getSinPowerWaveFunction(power));
	}

	public static Synth zero = new Synth(x -> 0f);
	public static Synth noise = new Synth(WaveFunctions.getNoiseFunction());
	public static Synth brownianNoise(float continuity) {
		return new Synth(new WaveFunctions.Brownian(continuity));
	}

	public static Synth combineSynths(Synth... synths) {
		
		return new Synth(x -> {
		
			float y = 0;
		
			for (int i = 0; i < synths.length; i++) {
				y += synths[i].f(x);  
			}
			return y / synths.length;
		});
	}
	
	public static Synth overtone(Synth synth, float overtoneRatio) {
		return new Synth(WaveFunctions.getOvertoneFunction(synth, overtoneRatio));
	}

}
