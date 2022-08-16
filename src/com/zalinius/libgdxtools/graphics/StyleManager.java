package com.zalinius.libgdxtools.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.zalinius.libgdxtools.tools.Assets;

// Investigate this tool for making custom skins easily
// https://ray3k.wordpress.com/software/skin-composer-for-libgdx/

public class StyleManager {
	public static Skin skin;
	public static float DEFAULT_FONT_SCALE, TITLE_FONT_SCALE, COMPACT_FONT_SCALE, MINI_FONT_SCALE, TEEENSY_FONT_SCALE;

	private StyleManager() {
		throw new IllegalStateException("Utility class");
	}

	public static void create() {
		DEFAULT_FONT_SCALE = 5/16f;
		TITLE_FONT_SCALE = DEFAULT_FONT_SCALE * 1.5f;
		COMPACT_FONT_SCALE = DEFAULT_FONT_SCALE * 0.76f;
		MINI_FONT_SCALE = DEFAULT_FONT_SCALE * 0.66f;
		TEEENSY_FONT_SCALE = DEFAULT_FONT_SCALE * 0.55f;

		skin = new Skin(Assets.skinPath);

		// main game font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Assets.exampleFontPath);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 150;
		parameter.color = Color.WHITE;
		parameter.spaceX = 4;

		parameter.minFilter = Texture.TextureFilter.Nearest;
		parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
		generator.scaleForPixelHeight(720);

		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();


		// title font
		FreeTypeFontGenerator titleGenerator = new FreeTypeFontGenerator(Assets.exampleFontPath);
		FreeTypeFontParameter titleParameter = new FreeTypeFontParameter();
		titleParameter.size = 150;
		titleParameter.color = Color.WHITE;
		BitmapFont titleFont = titleGenerator.generateFont(parameter);
		titleGenerator.dispose();

	}

	public static void dispose() {
		//must dispose of the fonts as well, not just the generator
	}
}
