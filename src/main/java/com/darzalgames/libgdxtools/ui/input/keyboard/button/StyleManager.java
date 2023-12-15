package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public abstract class StyleManager {

	protected BitmapFont currentFont;
	protected LabelStyle labelStyle;
	protected LabelStyle flavorTextLabelStyle;
	protected LabelStyle warningLabelStyle;
	protected LabelStyle labelWithBackgroundStyle;
	protected LabelStyle labelWithLightBackgroundStyle;
	protected TextButtonStyle textButtonStyle;
	protected TextButtonStyle flashedTextButtonStyle;
	protected TextButtonStyle sneakyLabelButtonStyle;
	protected TextButtonStyle settingsButtonStyle;
	protected TextButtonStyle fastForwardButtonStyle;
	protected SliderStyle sliderStyle;
	protected CheckBoxStyle checkboxStyle;
	protected NinePatchDrawable borderedNine;

	abstract NinePatchDrawable getUIBorderedNine();
}
