package com.zalinius.libgdxtools.graphics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.zalinius.libgdxtools.tools.Assets;

// Investigate this tool for making custom skins easily
// https://ray3k.wordpress.com/software/skin-composer-for-libgdx/

public class SkinManager {
	public static Skin skin;
	public static float DEFAULT_FONT_SCALE, TITLE_FONT_SCALE, COMPACT_FONT_SCALE, MINI_FONT_SCALE, TEEENSY_FONT_SCALE;

	private SkinManager() {
		throw new IllegalStateException("Utility class");
	}

	public static void create() {
		DEFAULT_FONT_SCALE = 5/16f;
		TITLE_FONT_SCALE = DEFAULT_FONT_SCALE * 1.5f;
		COMPACT_FONT_SCALE = DEFAULT_FONT_SCALE * 0.76f;
		MINI_FONT_SCALE = DEFAULT_FONT_SCALE * 0.66f;
		TEEENSY_FONT_SCALE = DEFAULT_FONT_SCALE * 0.55f;

		skin = new Skin(Assets.skinPath);
	}

	public static void dispose() {
		//must dispose of fonts
		skin.dispose();
	}
}
