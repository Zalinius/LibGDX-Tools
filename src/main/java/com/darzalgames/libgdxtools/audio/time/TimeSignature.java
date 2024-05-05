package com.darzalgames.libgdxtools.audio.time;

import java.util.function.IntFunction;

public interface TimeSignature {

	public static final TimeSignature TIME_3_4 = makeSimpleTimeSignature(3);
	public static final TimeSignature TIME_4_4 = makeSimpleTimeSignature(4);
	public static final TimeSignature TIME_5_4 = makeSimpleTimeSignature(5);
	
	
	/**
	 * @param beatNumber The current absolute beat
	 * @return Which measure the beat corresponds to
	 */
	public int getMeasure(int beatNumber);
	/**
	 * @param beatNumber The current absolute beat
	 * @return What the number of the beat is in the current measure
	 */
	public int getBeatInMeasure(int beatNumber);
	
	/**
	 * @param beatsPerMeasure How many beats are in a measure for this time signature. Must be positive.
	 * @return A simple time signature (one with 4 in the denominator) with the specified number of beats per measure
	 */
	public static TimeSignature makeSimpleTimeSignature(int beatsPerMeasure) {
		if(beatsPerMeasure < 1) {
			throw new IllegalArgumentException("Time signatures require a positive beatsPerMeasure");
		}
		
		return makeTimeSignature(b -> b/beatsPerMeasure, b -> b% beatsPerMeasure);
	}
	
	private static TimeSignature makeTimeSignature(IntFunction<Integer> measureFunction, IntFunction<Integer> beatPerMeasureFunction) {
		return new TimeSignature() {			
			@Override
			public int getMeasure(int beatNumber) {
				return measureFunction.apply(beatNumber);
			}
			@Override
			public int getBeatInMeasure(int beatNumber) {
				return beatPerMeasureFunction.apply(beatNumber);
			}
		};
	}

}
