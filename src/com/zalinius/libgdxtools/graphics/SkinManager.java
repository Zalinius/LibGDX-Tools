package com.zalinius.libgdxtools.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

// Use this tool for making custom skins easily
// https://ray3k.wordpress.com/software/skin-composer-for-libgdx/

public class SkinManager {
	public static Skin skin;
	public static String defaultStyle = "default";
	public static String titleLabelStyle = "title";
	public static String greenBackgroundLabelStyle = "green-borderless";
	public static float DEFAULT_FONT_SCALE, TITLE_FONT_SCALE, COMPACT_FONT_SCALE, MINI_FONT_SCALE, TEEENSY_FONT_SCALE;
	private static FreeTypeFontGenerator generator, titleGenerator;

	private SkinManager() {
		throw new IllegalStateException("Utility class");
	}

	public static void create(final FileHandle skinPath, final FileHandle atlasPath, final FileHandle fontPath, final FileHandle titleFontPath) {
		DEFAULT_FONT_SCALE = 5/16f;
		TITLE_FONT_SCALE = DEFAULT_FONT_SCALE * 1.5f;
		COMPACT_FONT_SCALE = DEFAULT_FONT_SCALE * 0.76f;
		MINI_FONT_SCALE = DEFAULT_FONT_SCALE * 0.66f;
		TEEENSY_FONT_SCALE = DEFAULT_FONT_SCALE * 0.55f;

		// main game font
		generator = new FreeTypeFontGenerator(fontPath);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 150;
		parameter.color = Color.WHITE;
		parameter.spaceX = 4;

		parameter.minFilter = Texture.TextureFilter.Nearest;
		parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;

		BitmapFont mainFont = generator.generateFont(parameter);

		// title font
		titleGenerator = new FreeTypeFontGenerator(titleFontPath);
		FreeTypeFontParameter titleParameter = new FreeTypeFontParameter();
		titleParameter.size = 150;
		titleParameter.color = Color.WHITE;
		titleParameter.mono = false;
		titleParameter.incremental = true;
		BitmapFont titleFont = titleGenerator.generateFont(parameter);

		skin = new Skin();

		skin.add("mainFont", mainFont);
		skin.add("titleFont", titleFont);
		skin.addRegions(new TextureAtlas(atlasPath));
		skin.load(skinPath);

	}

	public static void dispose() {
		titleGenerator.dispose();
		generator.dispose();
		skin.dispose();
	}
}
