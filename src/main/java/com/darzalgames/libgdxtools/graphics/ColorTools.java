package com.darzalgames.libgdxtools.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

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

	/**
	 * Get a square colored texture with width and height both being size "size"
	 * @param color
	 * @param size
	 * @return
	 */
	public static Texture getColoredTexture(Color color, int size) {
		return getColoredTexture(color, size, size);
	}

	/**
	 * Get a colored texture with the specified width and height
	 * @param color
	 * @param width
	 * @param height
	 * @return
	 */
	public static Texture getColoredTexture(Color color, int width, int height) {
		Pixmap coloredMap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		coloredMap.setColor(color);
		coloredMap.fillRectangle(0, 0, width, height);
		Texture coloredTexture = new Texture(coloredMap);
		coloredMap.dispose();
		return coloredTexture;
	}
	
	public static Texture getDefaultCursor() {
		Pixmap coloredMap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
		coloredMap.setColor(Color.WHITE);
		coloredMap.fillRectangle(2, 2, 1, 1);
		coloredMap.fillRectangle(3, 3, 2, 2);
		coloredMap.fillRectangle(4, 4, 3, 3);
		coloredMap.setColor(Color.BLACK);
		coloredMap.drawLine(0, 0, 8, 4);
		coloredMap.drawLine(0, 0, 4, 8);
		coloredMap.drawLine(4, 8, 8, 4);
		coloredMap.drawLine(6, 6, 9, 9);
		Texture coloredTexture = new Texture(coloredMap);
		coloredMap.dispose();
		return coloredTexture;
	}
}
