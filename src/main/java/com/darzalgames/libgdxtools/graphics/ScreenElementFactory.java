package com.darzalgames.libgdxtools.graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public abstract class ScreenElementFactory {
	protected SkinManager skinManager;
	public static final float DEFAULT_FONT_SCALE = 5/16f; 
	public static final float TITLE_FONT_SCALE = DEFAULT_FONT_SCALE * 1.5f; 
	public static final float COMPACT_FONT_SCALE = DEFAULT_FONT_SCALE * 0.76f; 
	public static final float MINI_FONT_SCALE = DEFAULT_FONT_SCALE * 0.66f; 
	public static final float TEEENSY_FONT_SCALE = DEFAULT_FONT_SCALE * 0.55f;

	public ScreenElementFactory(final SkinManager skinManager) {
		this.skinManager = skinManager;
	}

	public TextButton makeRegularButton(final String text, final Runnable runnable) {
		return makeButton(text, runnable, DEFAULT_FONT_SCALE, skinManager.defaultStyle);
	}
	public Label makeTitleLabel(final String text) {
		Label label = makeLabel(text, skinManager.titleLabelStyle);
		label.setFontScale(TITLE_FONT_SCALE*2);
		return label;
	}

	public Label makeRegularLabel(final String text) {
		return makeLabel(text, skinManager.defaultStyle);
	}

	protected Label makeLabel(final String text, final String style) {
		Label label = new Label(text, skinManager.skin, style);
		label.setFontScale(DEFAULT_FONT_SCALE);
		return label;
	}

	private TextButton makeButton(final String text, final Runnable runnable, final float fontScale, final String styleTag) {
		TextButton button = new TextButton(text, skinManager.skin, styleTag);
		button.getLabel().setFontScale(fontScale);
		button.addListener(new ChangeListener() {
			@Override
			public void changed (final ChangeEvent event, final Actor actor) {
				runnable.run();
			}
		});
		return button;
	}

	public CheckBox makeCheckBox(final boolean isChecked) {
		CheckBox button = new CheckBox("", skinManager.skin);
		if (isChecked) {
			button.setProgrammaticChangeEvents(false);
			button.toggle();
			button.setProgrammaticChangeEvents(true);
		}
		return button;
	}

	public void addEnterAndExitListener(final Actor actor, final Runnable onEnter, final Runnable onExit) {
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

	protected Image makeRepeatingBackground(final Texture backgroundTex, final float widthMultiplier, final float heightMultiplier) {
		backgroundTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		TextureRegion imgTextureRegion = new TextureRegion(backgroundTex);
		imgTextureRegion.setRegion(0, 0, backgroundTex.getWidth() * widthMultiplier, backgroundTex.getHeight() * heightMultiplier);
		return new Image(imgTextureRegion);
	}

	public abstract Actor makeBackground();

}
