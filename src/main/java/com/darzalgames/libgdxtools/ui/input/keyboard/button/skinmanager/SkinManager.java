package com.darzalgames.libgdxtools.ui.input.keyboard.button.skinmanager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;

public abstract class SkinManager {

	protected final Skin skin;

	protected static final String DEFAULT_LABEL = "defaultLabelStyle";
	protected static final String FLAVOR_TEXT_LABEL = "flavorTextLabelStyle";
	protected static final String WARNING_LABEL = "warningLabelStyle";
	protected static final String LABEL_WITH_BACKGROUND = "labelWithBackgroundStyle";

	protected static final String SLIDER = "sliderStyle";

	protected static final String CHECKBOX = "checkboxStyle";

	protected static final String TEXT_BUTTON = "textButtonStyle";
	protected static final String FLASHED_TEXT_BUTTON = "flashedTextButtonStyle";
	protected static final String SNEAKY_LABEL_BUTTON = "sneakyLabelButtonStyle";
	protected static final String BLANK_BUTTON = "blankButtonStyle";
	protected static final String SETTINGS_BUTTON = "settingsButtonStyle";

	protected static final String UI_BORDERED_NINE = "uiBorderedNine";
	protected static final String CONFIRMATION_MENU_BACKGROUND = "confirmationMenuBackground";

	protected SkinManager(Skin skin) {
		super();
		this.skin = skin;
		ConfirmationMenu.setConfirmationBackground(skin.get(CONFIRMATION_MENU_BACKGROUND, Texture.class));
	}
	protected LabelStyle getLabelStyle(String style) {
		return skin.get(style, LabelStyle.class);
	}
	protected TextButtonStyle getTextButtonStyle(String style) {
		return skin.get(style, TextButtonStyle.class);
	}
	protected CheckBoxStyle getCheckboxStyle(String style) {
		return skin.get(style, CheckBoxStyle.class);
	}
	protected SliderStyle getSliderStyle(String style) {
		return skin.get(style, SliderStyle.class);
	}

	public NinePatchDrawable getUINinePatch() {
		return skin.get(UI_BORDERED_NINE, NinePatchDrawable.class);
	}
	public LabelStyle getDefaultLableStyle() {
		return getLabelStyle(DEFAULT_LABEL);
	}
	public LabelStyle getFlavorTextLableStyle() {
		return getLabelStyle(FLAVOR_TEXT_LABEL);
	}
	public LabelStyle getWarningLableStyle() {
		return getLabelStyle(WARNING_LABEL);
	}
	public LabelStyle getLabelWithBackgroundStyle() {
		return getLabelStyle(LABEL_WITH_BACKGROUND);
	}
	public SliderStyle getSliderStyle() {
		return getSliderStyle(SLIDER);
	}
	public CheckBoxStyle getCheckboxStyle() {
		return getCheckboxStyle(CHECKBOX);
	}
	public TextButtonStyle getTextButtonStyle() {
		return getTextButtonStyle(TEXT_BUTTON);
	}
	public TextButtonStyle getFlashedTextButtonStyle() {
		return getTextButtonStyle(FLASHED_TEXT_BUTTON);
	}
	public TextButtonStyle getSneakyLableButtonStyle() {
		return getTextButtonStyle(SNEAKY_LABEL_BUTTON);
	}
	public TextButtonStyle getBlankButtonStyle() {
		return getTextButtonStyle(BLANK_BUTTON);
	}
	public TextButtonStyle getSettingsButtonStyle() {
		return getTextButtonStyle(SETTINGS_BUTTON);
	}

}
