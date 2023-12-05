package com.darzalgames.libgdxtools.graphics;

import com.badlogic.gdx.graphics.Color;

public class ColorTools {
	public static final int MIN_COLOR_CHANNEL_VALUE = 0;
	public static final int MAX_COLOR_CHANNEL_VALUE = 255;

	/**
	 *
	 * @param red The red color channel value, must be within the range [0, 255]
	 * @param green The green color channel value, must be within the range [0, 255]
	 * @param blue The blue color channel value, must be within the range [0, 255]
	 * @return A color with the specified RGB values, which is completely opaque.
	 */
	public static Color makeColorFromIntComponents(final int red, final int green, final int blue) {
		if(componentsInvalid(red, green, blue)) {
			throw new IllegalArgumentException("Color channel values must be with range ["+MIN_COLOR_CHANNEL_VALUE+", "+MAX_COLOR_CHANNEL_VALUE+"]");
		}

		int packedColorInt = componentsToRGBAIntegerBits(red, green, blue);
		return new Color(packedColorInt);
	}

	private static boolean componentsInvalid(final int red, final int green, final int blue) {
		return red < MIN_COLOR_CHANNEL_VALUE || red > MAX_COLOR_CHANNEL_VALUE || green < MIN_COLOR_CHANNEL_VALUE || green > MAX_COLOR_CHANNEL_VALUE || blue < MIN_COLOR_CHANNEL_VALUE || blue > MAX_COLOR_CHANNEL_VALUE;
	}

	private static int componentsToRGBAIntegerBits(final int red, final int green, final int blue) {
		return red << 24 | green << 16 | blue << 8 | MAX_COLOR_CHANNEL_VALUE;
	}

	protected ColorTools() {
		throw new IllegalStateException("Utility class");
	}
}
