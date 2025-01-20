package com.darzalgames.libgdxtools.hexagon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.darzalgames.libgdxtools.graphics.PixmapUtilities;

public class CustomHitbox {
	
	private Pixmap maskMap;

	/**
	 * @param maskTexture The texture (or related mask texture) that represents the visual clickable actor. NOTE: this needs to be the same size as the actor!
	 */
	public CustomHitbox(Texture maskTexture) {
		maskMap = PixmapUtilities.convertTextureToPixelMap(maskTexture);
	}

	/**
	 * @param hitX The x value provided in Actor's hit()
	 * @param hitY The y value provided in Actor's hit()
	 * @return Whether or not this position is a "hit" on the custom hitbox
	 */
	public boolean isHit(float hitX, float hitY) {
		Color pixelColor = PixmapUtilities.getPixelColorOnMap(hitX, hitY, maskMap);
		return pixelColor.a <= 0;
	}

}
