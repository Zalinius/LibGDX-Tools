package com.zalinius.libgdxtools.tools;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.zalinius.libgdxtools.shader.OutlineShader;
import com.zalinius.libgdxtools.shader.ShaderFactory;

public abstract class MultiImage extends Image{

	private final List<AtlasRegion> atlasRegions;

	public MultiImage(final List<AtlasRegion> atlasRegions) {
		this.atlasRegions = atlasRegions;
	}

	public abstract int currentImage();

	@Override
	public void draw(final Batch batch, final float parentAlpha) {
		int index = currentImage();
		if (index < atlasRegions.size()) {
			AtlasRegion currentTexture = atlasRegions.get(currentImage());

			OutlineShader outlineShader = ShaderFactory.getOutlineShader();
			batch.setShader(outlineShader);

			outlineShader.setForChosen();

			outlineShader.setTextureSize(new Vector2(currentTexture.getTexture().getWidth(), currentTexture.getTexture().getHeight()));

			Color color = getColor();
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

			float x = getX();
			float y = getY();
			float originX = getOriginX();
			float originY = getOriginY();
			float width = getWidth();
			float height = getHeight();
			float scaleX = getScaleX();
			float scaleY = getScaleY();
			float rotation = getRotation();

			batch.draw(currentTexture, x, y, originX, originY, width, height, scaleX, scaleY, rotation);

			unsetShader(batch);
		}
	}

	private void unsetShader(final Batch batch) {
		batch.setShader(null);
	}

}
