package com.zalinius.libgdxtools.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.zalinius.libgdxtools.tools.Assets;

public class ShaderFactory {

	private ShaderFactory() {}

	private static OutlineShader outlineShader;
	private static OutlineShader dottedOutlineShader;

	public static void create() {
		outlineShader = makeOutlineShader();
		dottedOutlineShader = makeDottedOutlineShader();
	}

	public static void dispose() {
		outlineShader.dispose();
		dottedOutlineShader.dispose();
	}

	public static OutlineShader getOutlineShader() {
		return outlineShader;
	}

	public static OutlineShader getDottedOutlineShader() {
		return dottedOutlineShader;
	}

	private static OutlineShader makeOutlineShader() {
		return makeShader(Assets.outlineVertexShader, Assets.outlineFragmentShader);
	}
	private static OutlineShader makeDottedOutlineShader() {
		return makeShader(Assets.outlineDottedVertexShader, Assets.outlineDottedFragmentShader);
	}

	private static OutlineShader makeShader(final FileHandle vertexShaderFile, final FileHandle fragmentShaderFile) {
		ShaderProgram.pedantic = false;
		OutlineShader shaderProgram = new OutlineShader(vertexShaderFile, fragmentShaderFile);
		if(!shaderProgram.isCompiled()) {
			Gdx.app.error("Shader", "Shader compilation failed:");
			Gdx.app.error("Shader", shaderProgram.getLog());
			return null;
		}
		return shaderProgram;
	}


}
