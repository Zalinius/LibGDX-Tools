package com.darzalgames.libgdxtools.hexagon;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.darzalgames.libgdxtools.graphics.PixmapUtilities;

public class CustomHitBox {
	
	private static Map<String, Pixmap> hitboxMasks = new HashMap<>();

	/**
	 * Register a custom hitbox, to be called later with {@link CustomHitBox#isHit}
	 * @param maskKey A String representing this mask, to be shared by several objects
	 * @param maskTexture The texture (or related mask texture) that represents the visual clickable actor. NOTE: this needs to be the same size as the actor!
	 */
	public static void registerMask(String maskKey, Texture maskTexture) {
		hitboxMasks.put(maskKey, PixmapUtilities.convertTextureToPixelMap(maskTexture));
	}

	/**
	 * @param maskKey A String representing this mask, must have been previously registered with {@link CustomHitBox#registerMask}.
	 * @param hitX The x value provided in Actor's hit()
	 * @param hitY The y value provided in Actor's hit()
	 * @return Whether or not this position is a "hit" on the custom hitbox
	 */
	public static boolean isHit(String maskKey, float hitX, float hitY) {
		Pixmap maskMap =  hitboxMasks.get(maskKey);
		if (maskMap == null) {
			throw new IllegalArgumentException("No mask registered for key: " + maskKey);
		}
		Color pixelColor = PixmapUtilities.getPixelColorOnMap(hitX, hitY, maskMap);
		return pixelColor.a <= 0;
	}

	private CustomHitBox() {
		throw new IllegalStateException("Utility class");
	}

}
