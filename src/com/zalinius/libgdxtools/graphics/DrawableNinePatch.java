package com.zalinius.libgdxtools.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class DrawableNinePatch implements Drawable {

	private final NinePatch ninePatch;

	public DrawableNinePatch(final NinePatch ninePatch) {
		this.ninePatch = ninePatch;
	}

	//Default-sized NinePatch (100px off each side)
	public DrawableNinePatch(final Texture tex) {
		int borderWidth = 100;
		this.ninePatch = new NinePatch(tex, borderWidth, borderWidth, borderWidth, borderWidth);

		//Draw the corner patches smaller, since the textures are bigger than the rendered cards
		ninePatch.setLeftWidth(borderWidth * 0.3f);
		ninePatch.setRightWidth(borderWidth * 0.3f);
		ninePatch.setTopHeight(borderWidth * 0.3f);
		ninePatch.setBottomHeight(borderWidth * 0.3f);
	}

	@Override
	public void draw(final Batch batch, final float x, final float y, final float width, final float height) {
		ninePatch.draw(batch, x, y, width, height);
	}

	@Override
	public float getLeftWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLeftWidth(final float leftWidth) {
		ninePatch.setLeftWidth(leftWidth);
	}

	@Override
	public float getRightWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRightWidth(final float rightWidth) {
		ninePatch.setRightWidth(rightWidth);
	}

	@Override
	public float getTopHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTopHeight(final float topHeight) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getBottomHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBottomHeight(final float bottomHeight) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getMinWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMinWidth(final float minWidth) {
		// TODO Auto-generated method stub

	}

	@Override
	public float getMinHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMinHeight(final float minHeight) {
		// TODO Auto-generated method stub

	}

}
