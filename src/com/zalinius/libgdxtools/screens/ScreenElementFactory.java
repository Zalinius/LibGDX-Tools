package com.zalinius.libgdxtools.screens;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.zalinius.libgdxtools.graphics.SkinManager;
import com.zalinius.libgdxtools.tools.Assets;

public class ScreenElementFactory {

	private ScreenElementFactory() {
		throw new IllegalStateException("Utility class");
	}

	public static TextButton makeRegularButton(final String text, final Runnable runnable) {
		return makeButton(text, runnable, SkinManager.DEFAULT_FONT_SCALE);
	}
	public static TextButton makeTitleButton(final String text, final Runnable runnable) {
		return makeButton(text, runnable, SkinManager.TITLE_FONT_SCALE);
	}

	private static TextButton makeButton(final String text, final Runnable runnable, final float fontScale) {
		TextButton button = new TextButton(text, SkinManager.skin);
		button.getLabel().setFontScale(fontScale);
		button.addListener(new ChangeListener() {
			@Override
			public void changed (final ChangeEvent event, final Actor actor) {
				runnable.run();
			}
		});
		return button;
	}

	public static void addEnterAndExitListener(final Actor actor, final Runnable onEnter, final Runnable onExit) {
		actor.addListener(new InputListener() {
			@Override
			public void enter(final InputEvent event, final float x, final float y, final int pointer, final Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				onEnter.run();
			}

			@Override
			public void exit(final InputEvent event, final float x, final float y, final int pointer, final Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				onExit.run();
			}

		});
	}

	public static Actor makeBackground(final AssetDescriptor<Texture> tex) {
		Texture backgroundTex = Assets.get(tex);
		backgroundTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		TextureRegion imgTextureRegion = new TextureRegion(backgroundTex);
		imgTextureRegion.setRegion(0, 0, backgroundTex.getWidth() * 10, backgroundTex.getHeight() * 10);
		Image backgroundActor = new Image(imgTextureRegion);
		backgroundActor.scaleBy(-0.75f);
		backgroundActor.setColor(Color.LIGHT_GRAY);
		return backgroundActor;
	}

}
