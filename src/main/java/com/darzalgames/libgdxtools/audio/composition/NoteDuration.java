package com.darzalgames.libgdxtools.audio.composition;

public class NoteDuration {
	
	public static final NoteDuration SIXTEENTH = new NoteDuration(0.25f, 1);
	public static final NoteDuration EIGHTH = new NoteDuration(0.5f, 2);
	public static final NoteDuration EIGHTH_DOT = new NoteDuration(0.75f, 3);
	public static final NoteDuration QUARTER = new NoteDuration(1.0f, 4);
	public static final NoteDuration QUARTER_DOT = new NoteDuration(1.5f, 6);
	public static final NoteDuration HALF = new NoteDuration(2.0f, 8);
	public static final NoteDuration HALF_DOT = new NoteDuration(3.0f, 12);
	public static final NoteDuration WHOLE = new NoteDuration(4.0f, 16);
	public static final NoteDuration WHOLE_DOT = new NoteDuration(6.0f, 24);

	
	public final float duration;
	public final int durationInSixteenths;

	public NoteDuration(float duration, int durationInSixteenths) {
		this.duration = duration;
		this.durationInSixteenths = durationInSixteenths;
	}

}
