package com.darzalgames.libgdxtools.graphics;

import com.badlogic.gdx.graphics.*;

public class PixmapUtilities {

	public static Pixmap convertTextureToPixelMap(Texture texture) {
		TextureData textureData = texture.getTextureData();
		if (!textureData.isPrepared()) {
			textureData.prepare();
		}
		return textureData.consumePixmap();
	}

	/**
	 * @param hitX The x value provided in Actor's hit()
	 * @param hitY The y value provided in Actor's hit()
	 * @param map the pixmap to analyze
	 * @return The color on the specified position of the map, or CLEAR if out of bounds
	 */
	public static Color getPixelColorOnMap(float hitX, float hitY, Pixmap map) {
		int x = (int)Math.floor(hitX);
		int y = map.getHeight() - 1 - (int)Math.floor(hitY); // Actor Y is opposite to Pixmap Y, so we subtract.
		boolean isOutOfBounds = x < 0 || x >= map.getWidth() || y < 0 || y >= map.getHeight();
		if (isOutOfBounds) {
			// We check out of bounds just in case, otherwise getPixel() crashes. If you're out of bounds, there's no correct color data anyway.
			return Color.CLEAR;
		}
		int pixel = map.getPixel(x, y);
		return new Color(pixel);
	}


	private PixmapUtilities() {
		throw new IllegalStateException("Utility class");
	}
}
