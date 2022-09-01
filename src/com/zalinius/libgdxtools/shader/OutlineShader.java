package com.zalinius.libgdxtools.shader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

public class OutlineShader extends ShaderProgram{
	
	private static String COLOR_UNIFORM_LOCATION = "u_outlineColor";
	private static String OUTLINE_QUALITY_UNIFORM_LOCATION = "u_outlineQuality";
	private static String OUTLINE_THICKNESS_UNIFORM_LOCATION = "u_outlineThickness";

	private static String TEXTURE_SIZE_UNIFORM_LOCATION = "u_textureSize";

	public OutlineShader(FileHandle vertexShader, FileHandle fragmentShader) {
		super(vertexShader, fragmentShader);
	}
	
	public void setForOption() {
		setQuality(8);
		setThickness(10);
		setColor(Color.CHARTREUSE);
	}
	
	public void setForChosen() {
		setQuality(8);
		setThickness(10);
		setColor(Color.WHITE);
	}
	
	public void setColor(Color color) {
		setUniformf(COLOR_UNIFORM_LOCATION, color);
	}
	
	/**
	 * @param quality number of samples per quadrant (e.g. quality 8 means 32 samples, or radial samples 11.25 degrees)
	 */
	public void setQuality(int quality) {
		setUniformf(OUTLINE_QUALITY_UNIFORM_LOCATION, quality);
	}
	
	/**
	 * @param thickness every 4 thickness is roughly 1 pixel. Don't ask me why
	 */
	public void setThickness(int thickness) {
		setUniformf(OUTLINE_THICKNESS_UNIFORM_LOCATION, thickness);
	}
	
	
	public void setTextureSize(Vector2 textureSize) {
		setUniformf(TEXTURE_SIZE_UNIFORM_LOCATION, textureSize);
	}
}
