package com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class DoodadBackgroundImage extends Image {

	@Override
	public void draw(Batch batch, float parentAlpha) {
		validate();

		float x = getX();
		float y = getY();
		float scaleX = getScaleX();
		float scaleY = getScaleY();
		getDrawable().draw(batch, x + getImageX(), y + getImageY(), getImageWidth() * scaleX, getImageHeight() * scaleY);

//		Color color = getColor();
//		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
//
//		if (drawable instanceof TransformDrawable) {
//			float rotation = getRotation();
//			if (scaleX != 1 || scaleY != 1 || rotation != 0) {
//				((TransformDrawable) drawable).draw(
//						batch, x + imageX, y + imageY, getOriginX() - imageX, getOriginY() - imageY,
//						imageWidth, imageHeight, scaleX, scaleY, rotation
//				);
//				return;
//			}
//		}
//		if (drawable != null) {
//		}
	}

	@Override
	public void setScale(float scaleX, float scaleY) {
		if (scaleX == 0 || scaleY == 0) {
			System.out.println();
		}
		super.setScale(scaleX, scaleY);
	}

}
