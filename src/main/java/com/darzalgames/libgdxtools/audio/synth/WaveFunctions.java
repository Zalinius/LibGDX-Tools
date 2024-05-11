package com.darzalgames.libgdxtools.audio.synth;

import java.util.Random;
import java.util.function.UnaryOperator;

import com.badlogic.gdx.math.MathUtils;

// 
// When possible/sensible, f(0) = 0

/**
 * A bunch of wave functions that satisfy:
 * - Domain [0,1[
 * - Range [-1, 1]
 * - Period/Frequency of 1 (when relevant)
 */
public class WaveFunctions {


	public static UnaryOperator<Float> getSquareWaveFunction() {
		return x -> {
			return (x < 0.5f) ? 1f : -1f;
		};
	}

	public static UnaryOperator<Float> getPulseWaveFunction(float modulation) {
		return x -> {
			return (x < modulation) ? 1f : -1f;
		};
	}

	public static UnaryOperator<Float> getSinWaveFunction() {
		return x -> {
			return MathUtils.sin(MathUtils.PI2 * x);
		};
	}

	public static UnaryOperator<Float> getSincWaveFunction() {
		return x -> {
			if (x == 0.5f) {
				return 1f;
			} else {
				float xPrime = 4f * MathUtils.PI * (x - 0.5f);
				return MathUtils.sin(xPrime) / xPrime;
			}
		};
	}

	public static UnaryOperator<Float> getTriangleWaveFunction() {
		return x -> {
			if (x <= .25f) {
				return 4 * x;
			} else if (x <= .75f) {
				return -4 * x + 2f;
			} else {
				return 4 * x - 4f;
			}
		};
	}

	public static UnaryOperator<Float> getSawtoothWaveFunction() {
		return x -> {

			if (x <= .5f) {
				return 2 * x;
			} else {
				return 2 * x - 2f;
			}
		};
	}

	public static UnaryOperator<Float> bandLimitedSawtoothWaveFunction(int harmonics) {
		if (harmonics < 1) {
			throw new IllegalArgumentException("Harmonics must be at least 1: " + harmonics);
		}

		return x -> {

			float y = 0f;
			float maxAmplitude = 0f;

			for (int i = 1; i <= harmonics; i++) {
				y += MathUtils.sin(i * MathUtils.PI2 * x) / i;
				maxAmplitude += 1f / i;
			}

			return y / maxAmplitude;

		};
	}

	public static UnaryOperator<Float> getNoiseFunction() {
		return x -> {
			Random rand = new Random();
			return rand.nextFloat(-1f, 1f);
		};
	}

	public static class Brownian implements UnaryOperator<Float> {
		private final Random rand;
		private float lastOut;
		private float continuity;

		public Brownian(float continuity) {
			this.rand = new Random();
			this.lastOut = 0f;
			this.continuity = continuity;
		}

		@Override
		public Float apply(Float t) {
			float white = rand.nextFloat(-1, 1);
			float output = (continuity*lastOut + ((1-continuity) * white));
			lastOut = (float) output;
			return lastOut;

		}
	}

	public static UnaryOperator<Float> getNullWaveFunction() {
		return x -> 0f;
	}

	public static UnaryOperator<Float> getSinPowerWaveFunction(int power) {
		UnaryOperator<Float> sin = getSinWaveFunction();
		return x -> {
			float sinValue = sin.apply(x);

			if (sinValue > 0f) {
				return (float) Math.pow(sinValue, power);
			} else {
				return (float) -Math.pow(Math.abs(sinValue), power);
			}

		};

	}
	
	/**
	 * Creates a synth with an overtone at double frequency, of a specified amplitude
	 */
	public static UnaryOperator<Float> getOvertoneFunction(Synth synth, float overtoneRatio) {
		return x -> {
			float value = synth.f(x);
			float overtone = synth.f((2f * x)%1f);
			
			return (value + overtoneRatio*overtone) / (1+overtoneRatio);
		};

	}

}
