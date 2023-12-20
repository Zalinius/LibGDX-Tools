package com.darzalgames.libgdxtools.ui.input.keyboard.button.stylemanager;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public abstract class StyleManager {
	
	protected final Skin skin;

	protected static final String defaultLabel = "defaultLabelStyle";
	protected static final String flavorTextLabel = "flavorTextLabelStyle";
	protected static final String warningLabel = "warningLabelStyle";
	protected static final String labelWithBackground = "labelWithBackgroundStyle";
	
	protected static final String slider = "sliderStyle";
	
	protected static final String checkbox = "checkboxStyle";

	protected static final String textButton = "textButtonStyle";
	protected static final String flashedTextButton = "flashedTextButtonStyle";
	protected static final String sneakyLabelButton = "sneakyLabelButtonStyle";
	protected static final String blankButton = "blankButtonStyle";
	protected static final String settingsButton = "settingsButtonStyle";
	
	protected StyleManager(Skin skin) {
		super();
		this.skin = skin;
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
	

	public LabelStyle getDefaultLableStyle() {
		return getLabelStyle(defaultLabel);
	}
	public LabelStyle getFlavorTextLableStyle() {
		return getLabelStyle(flavorTextLabel);
	}
	public LabelStyle getWarningLableStyle() {
		return getLabelStyle(warningLabel);
	}
	public LabelStyle getLabelWithBackgroundStyle() {
		return getLabelStyle(labelWithBackground);
	}
	public SliderStyle getSliderStyle() {
		return getSliderStyle(slider);
	}
	public CheckBoxStyle getCheckboxStyle() {
		return getCheckboxStyle(checkbox);
	}
	public TextButtonStyle getTextButtonStyle() {
		return getTextButtonStyle(textButton);
	}
	public TextButtonStyle getFlashedTextButtonStyle() {
		return getTextButtonStyle(flashedTextButton);
	}
	public TextButtonStyle getSneakyLableButtonStyle() {
		return getTextButtonStyle(sneakyLabelButton);
	}
	public TextButtonStyle getBlankButtonStyle() {
		return getTextButtonStyle(blankButton);
	}
	public TextButtonStyle getSettingsButtonStyle() {
		return getTextButtonStyle(settingsButton);
	}
	
}
