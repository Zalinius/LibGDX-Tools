package com.darzalgames.libgdxtools.hexagon;

import com.badlogic.gdx.graphics.*;
import com.darzalgames.libgdxtools.graphics.ColorTools;

public class HexagonButtonHitBox {

	private static Pixmap maskMap;

	public static void initialize(Texture hexagonMask) {
		maskMap = convertTextureToPixelMap(hexagonMask);
	}
	
	private static Pixmap convertTextureToPixelMap(Texture texture) {
		TextureData textureData = texture.getTextureData(); 
		if (!textureData.isPrepared()) {
			textureData.prepare();
		}
		return textureData.consumePixmap();
	}

	public static boolean isPixelTransparentOnMask(float hitX, float hitY) {
		int x = (int)Math.floor(hitX);
		int y = maskMap.getHeight() - 1 - (int)Math.floor(hitY); // Actor Y is opposite to Pixmap Y, so we subtract.
		if (x < 0 || x >= maskMap.getWidth() || y < 0 || y >= maskMap.getHeight()) {
			// Juuuuuuust in case, otherwise getPixel() crashes. Honestly, if you're out of bounds of the mask, then you're definitely not in the hexagon.
			return true;
		}
		Color pixel = ColorTools.getColorFromPixmapInteger(maskMap.getPixel(x, y));
		return pixel.a <= 0;
	}

	private HexagonButtonHitBox() {
		throw new IllegalStateException("Utility class");
	}

}
