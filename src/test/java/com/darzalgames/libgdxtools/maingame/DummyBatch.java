package com.darzalgames.libgdxtools.maingame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;

/**
 * A dummy implementation of LibGDX's {@link Batch Batch} interface with no functionality and which makes no OpenGL calls.
 * Useful to instantiate a Stage for testing.
 */
public class DummyBatch implements Batch {

	@Override
	public void dispose() {
		// not needed for testin
	}

	@Override
	public void begin() {
		// not needed for testing
	}

	@Override
	public void end() {
		// not needed for testing
	}

	@Override
	public void setColor(Color tint) {
		// not needed for testing
	}

	@Override
	public void setColor(float r, float g, float b, float a) {
		// not needed for testing
	}

	@Override
	public Color getColor() {
		// not needed for testing
		return null;
	}

	@Override
	public void setPackedColor(float packedColor) {
		// not needed for testing
	}

	@Override
	public float getPackedColor() {
		// not needed for testing
		return 0;
	}

	@Override
	public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
		// not needed for testing
	}

	@Override
	public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
		// not needed for testing

	}

	@Override
	public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
		// not needed for testing

	}

	@Override
	public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
		// not needed for testing

	}

	@Override
	public void draw(Texture texture, float x, float y) {
		// not needed for testing

	}

	@Override
	public void draw(Texture texture, float x, float y, float width, float height) {
		// not needed for testing

	}

	@Override
	public void draw(Texture texture, float[] spriteVertices, int offset, int count) {
		// not needed for testing

	}

	@Override
	public void draw(TextureRegion region, float x, float y) {
		// not needed for testing

	}

	@Override
	public void draw(TextureRegion region, float x, float y, float width, float height) {
		// not needed for testing

	}

	@Override
	public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
		// not needed for testing

	}

	@Override
	public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {
		// not needed for testing

	}

	@Override
	public void draw(TextureRegion region, float width, float height, Affine2 transform) {
		// not needed for testing

	}

	@Override
	public void flush() {
		// not needed for testing

	}

	@Override
	public void disableBlending() {
		// not needed for testing

	}

	@Override
	public void enableBlending() {
		// not needed for testing

	}

	@Override
	public void setBlendFunction(int srcFunc, int dstFunc) {
		// not needed for testing

	}

	@Override
	public void setBlendFunctionSeparate(int srcFuncColor, int dstFuncColor, int srcFuncAlpha, int dstFuncAlpha) {
		// not needed for testing

	}

	@Override
	public int getBlendSrcFunc() {
		// not needed for testing
		return 0;
	}

	@Override
	public int getBlendDstFunc() {
		// not needed for testing
		return 0;
	}

	@Override
	public int getBlendSrcFuncAlpha() {
		// not needed for testing
		return 0;
	}

	@Override
	public int getBlendDstFuncAlpha() {
		// not needed for testing
		return 0;
	}

	@Override
	public Matrix4 getProjectionMatrix() {
		// not needed for testing
		return null;
	}

	@Override
	public Matrix4 getTransformMatrix() {
		// not needed for testing
		return null;
	}

	@Override
	public void setProjectionMatrix(Matrix4 projection) {
		// not needed for testing

	}

	@Override
	public void setTransformMatrix(Matrix4 transform) {
		// not needed for testing

	}

	@Override
	public void setShader(ShaderProgram shader) {
		// not needed for testing

	}

	@Override
	public ShaderProgram getShader() {
		// not needed for testing
		return null;
	}

	@Override
	public boolean isBlendingEnabled() {
		// not needed for testing
		return false;
	}

	@Override
	public boolean isDrawing() {
		// not needed for testing
		return false;
	}

}
