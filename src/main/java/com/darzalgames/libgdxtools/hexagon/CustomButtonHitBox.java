package com.darzalgames.libgdxtools.hexagon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.darzalgames.libgdxtools.graphics.PixmapUtilities;

public class CustomButtonHitBox {

	private static Pixmap maskMap;

	/**
	 * @param hexagonMask The texture (or related mask) that represents the visual clickable button. NOTE: THESE NEED TO BE THE SAME SIZE!
	 */
	public static void initialize(Texture hexagonMask) {
		maskMap = PixmapUtilities.convertTextureToPixelMap(hexagonMask);
	}

	/**
	 * @param hitX The x value provided in Actor's hit()
	 * @param hitY The y value provided in Actor's hit()
	 * @return Whether or not this hit position is on the button
	 */
	public static boolean isHit(float hitX, float hitY) {
		Color pixelColor = PixmapUtilities.getPixelColorOnMap(hitX, hitY, maskMap);
		return pixelColor.a <= 0;
	}

	private CustomButtonHitBox() {
		throw new IllegalStateException("Utility class");
	}

}
