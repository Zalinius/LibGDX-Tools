package com.zalinius.libgdxtools.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class StyleManager {
	public static LabelStyle style;
	public static LabelStyle titleStyle;
	public static LabelStyle greenBackgroundStyle;
	public static LabelStyle menuLabelStyle;
	public static TextButtonStyle textButtonStyle;
	public static CheckBoxStyle musicButtonStyle;
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

		greenBackgroundStyle = new LabelStyle();
		greenBackgroundStyle.font = font;
		greenBackgroundStyle.background = new DrawableNinePatch(Assets.get(Assets.exampleTexture));

		style = new LabelStyle();
		style.font = font;

		DrawableNinePatch button = new DrawableNinePatch(Assets.get(Assets.exampleTexture));
		textButtonStyle = new TextButtonStyle(button, button, button, font);
		textButtonStyle.over = new DrawableNinePatch(Assets.get(Assets.exampleTexture));
		textButtonStyle.disabled = new DrawableNinePatch(Assets.get(Assets.exampleTexture));

		// title font
		FreeTypeFontGenerator titleGenerator = new FreeTypeFontGenerator(Assets.exampleFontPath);
		FreeTypeFontParameter titleParameter = new FreeTypeFontParameter();
		titleParameter.size = 150;
		titleParameter.color = Color.WHITE;
		BitmapFont titleFont = titleGenerator.generateFont(parameter);
		titleGenerator.dispose();
		titleStyle = new LabelStyle();
		titleStyle.font = titleFont;

		Image soundoff = new Image(Assets.get(Assets.exampleTexture));
		Image soundon = new Image(Assets.get(Assets.exampleTexture));
		musicButtonStyle = new CheckBoxStyle(soundon.getDrawable(),
				soundoff.getDrawable(), font, Color.WHITE);
	}

	public static void dispose() {
		style.font.dispose();
	}
}
