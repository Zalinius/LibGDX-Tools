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
	}

	@Override
	public void setScale(float scaleX, float scaleY) {
		if (scaleX == 0 || scaleY == 0) {
			System.out.println();
		}
		super.setScale(scaleX, scaleY);
	}

}
