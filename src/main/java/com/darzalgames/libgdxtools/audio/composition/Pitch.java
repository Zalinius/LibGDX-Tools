package com.darzalgames.libgdxtools.audio.composition;

import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;

public class Pitch implements Comparable<Pitch>{
	
	private final float frequency;
	private final boolean natural;
	private final String name;
	private static final NavigableSet<Pitch> naturalPitches = new TreeSet<>();
	
	public static final Pitch NONE = new Pitch(0f, "NONE");
	
	public static final Pitch C1 = new Pitch( 32.70f, "C1");
	public static final Pitch D1 = new Pitch( 36.71f, "D1");
	public static final Pitch E1 = new Pitch( 41.20f, "E1");
	public static final Pitch F1 = new Pitch( 43.65f, "F1");
	public static final Pitch G1 = new Pitch( 49.00f, "G1");
	public static final Pitch A1 = new Pitch( 55.00f, "A1");
	public static final Pitch B1 = new Pitch( 61.74f, "B1");

	public static final Pitch C2 = new Pitch( 65.41f, "C2");
	public static final Pitch D2 = new Pitch( 73.42f, "D2");
	public static final Pitch E2 = new Pitch( 82.41f, "E2");
	public static final Pitch F2 = new Pitch( 87.31f, "F2");
	public static final Pitch G2 = new Pitch( 98.00f, "G2");
	public static final Pitch A2 = new Pitch(110.00f, "A2");
	public static final Pitch B2 = new Pitch(123.47f, "B2");

	public static final Pitch C3 = new Pitch(130.81f, "C3");
	public static final Pitch D3 = new Pitch(146.83f, "D3");
	public static final Pitch E3 = new Pitch(164.81f, "E3");
	public static final Pitch F3 = new Pitch(174.61f, "F3");
	public static final Pitch G3 = new Pitch(196.00f, "G3");
	public static final Pitch A3 = new Pitch(220.00f, "A3");
	public static final Pitch B3 = new Pitch(246.94f, "B3");

	public static final Pitch C4 = new Pitch(261.63f, "C4");
	public static final Pitch D4 = new Pitch(293.66f, "D4");
	public static final Pitch E4 = new Pitch(329.63f, "E4");
	public static final Pitch F4 = new Pitch(349.23f, "F4");
	public static final Pitch G4 = new Pitch(392.00f, "G4");
	public static final Pitch A4 = new Pitch(440.00f, "A4");
	public static final Pitch B4 = new Pitch(493.88f, "B4");

	public static final Pitch C5 = new Pitch(523.25f, "C5");
	public static final Pitch D5 = new Pitch(587.33f, "D5");
	public static final Pitch E5 = new Pitch(659.25f, "E5");
	public static final Pitch F5 = new Pitch(698.46f, "F5");
	public static final Pitch G5 = new Pitch(783.99f, "G5");
	public static final Pitch A5 = new Pitch(880.00f, "A5");
	public static final Pitch B5 = new Pitch(987.77f, "B5");

	public static final Pitch C6 = new Pitch(1046.50f, "C6");	
	public static final Pitch D6 = new Pitch(1174.66f, "D6");
	public static final Pitch E6 = new Pitch(1318.51f, "E6");
	public static final Pitch F6 = new Pitch(1396.91f, "F6");
	public static final Pitch G6 = new Pitch(1567.98f, "G6");
	public static final Pitch A6 = new Pitch(1760.00f, "A6");
	public static final Pitch B6 = new Pitch(1975.53f, "B6");
	
	private Pitch(float frequency, String name) {
		this(frequency, name, true);
	}
	private Pitch(float frequency, String name, boolean natural) {
		this.frequency = frequency;
		this.name = name;
		this.natural = natural;
		if(natural) {
			naturalPitches.add(this);			
		}
	}
	
	public static Pitch makePitch(float frequency) {
		return new Pitch(frequency, "Artificial Pitch", false);
	}
	
	public Pitch up() {
		if(this == NONE) {
			return NONE;
		}
		
		Pitch higher = naturalPitches.higher(this);
		
		if(higher == null) {
			return this;
		}
		else {
			return higher;
		}
	}
	
	public Pitch down() {
		if(this == NONE) {
			return NONE;
		}
		
		Pitch lower = naturalPitches.lower(this);
		
		if(lower == NONE || lower == null) {
			return this;
		}
		else {
			return lower;
		}
	}
	
	public Pitch octaveUp() {
		if(this.compareTo(B5) > 0) {
			return this;
		}
		else {
			return this.up().up().up().up().up().up().up();
		}
	}
	
	public Pitch octaveDown() {
		if(this.compareTo(C2) < 0) {
			return this;
		}
		else {
			return this.down().down().down().down().down().down().down();
		}
	}
	
	
	public float getFrequency() {
		return frequency;
	}
	
	public boolean isNatural() {
		return natural;
	}
		
	
	@Override
	public int hashCode() {
		return Objects.hash(frequency);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pitch other = (Pitch) obj;
		return Float.floatToIntBits(frequency) == Float.floatToIntBits(other.frequency);
	}
	@Override
	public int compareTo(Pitch other) {
		return Float.compare(this.frequency, other.frequency);
	}
	
	@Override
	public String toString() {
		return name + "(" + frequency + "hz)";
	}

}
