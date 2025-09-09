package com.darzalgames.libgdxtools.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.darzalgames.libgdxtools.graphics.PixmapUtilities;

public class CustomHitbox {

	private final Pixmap maskMap;

	/**
	 * @param maskTexture The texture (or related mask texture) that represents the visual clickable actor.
	 */
	public CustomHitbox(Texture maskTexture) {
		maskMap = PixmapUtilities.convertTextureToPixelMap(maskTexture);
	}

	/**
	 * NOTE: the map texture needs to be the same proportions as the actor for this!
	 * @param hitX The x value provided in Actor's hit()
	 * @param hitY The y value provided in Actor's hit()
	 * @return Whether or not this position is a "hit" on the custom hitbox
	 */
	public boolean isHit(float hitX, float hitY, float scaledWidth, float scaledHeight) {
		float scaledHitX = (hitX * maskMap.getWidth()) / scaledWidth;
		float scaledHitY = (hitY * maskMap.getHeight()) / scaledHeight;
		Color pixelColor = PixmapUtilities.getPixelColorOnMap(scaledHitX, scaledHitY, maskMap);
		return pixelColor.a <= 0;
	}

}
