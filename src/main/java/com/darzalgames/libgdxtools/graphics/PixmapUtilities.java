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
	 * @param map
	 * @return The color on the specified position of the map, or CLEAR if out of bounds
	 */
	public static Color getPixelColorOnMap(float hitX, float hitY, Pixmap map) {
		int x = (int)Math.floor(hitX);
		int y = map.getHeight() - 1 - (int)Math.floor(hitY); // Actor Y is opposite to Pixmap Y, so we subtract.
		if (x < 0 || x >= map.getWidth() || y < 0 || y >= map.getHeight()) {
			// Juuuuuuust in case, otherwise getPixel() crashes. Honestly, if you're out of bounds of the mask, then you're definitely not in the hexagon.
			return Color.CLEAR;
		}
		int pixel = map.getPixel(x, y);
		return ColorTools.getColorFromPixmapInteger(pixel);
	}


	private PixmapUtilities() {
		throw new IllegalStateException("Utility class");
	}
}
