package com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;

/**
 * A class which holds a LibGDX {@link Skin} and provides convenient named accessors.
 */
public class SkinManager {

	protected final Skin skin;
	
	// NinePatch
	protected static final String CONFIRMATION_MENU_BACKGROUND = "confirmationMenuBackground";
	protected static final String UI_BORDERED_NINE = "uiBorderedNine";

	// LabelStyle
	protected static final String DEFAULT_LABEL = "default";
	protected static final String FLAVOR_TEXT_LABEL = "flavorTextLabelStyle";
	protected static final String WARNING_LABEL = "warningLabelStyle";
	protected static final String LABEL_WITH_BACKGROUND = "labelWithBackgroundStyle";

	// SliderStyle
	protected static final String SLIDER = "default-horizontal";

	// CheckboxStyle
	protected static final String CHECKBOX = "default";

	// TextButtonStyle
	protected static final String TEXT_BUTTON = "default";
	protected static final String FLASHED_TEXT_BUTTON = "flashedTextButtonStyle";
	protected static final String SNEAKY_LABEL_BUTTON = "sneakyLabelButtonStyle";
	protected static final String BLANK_BUTTON = "blankButtonStyle";
	protected static final String SETTINGS_BUTTON = "settingsButtonStyle";

	/**
	 * @param skin The skin set up by the base class
	 */
	public SkinManager(Skin skin) {
		super();
		this.skin = skin;
		ConfirmationMenu.setConfirmationBackground(skin.get(CONFIRMATION_MENU_BACKGROUND, NinePatch.class));
	}
	
	/**
	 * @return the default (very basic) skin
	 */
	public static Skin getDefaultSkin() {
		Skin skin = new Skin();
		
		int size = 4;
		
		NinePatchDrawable darkGrayNinePatch = new NinePatchDrawable(new NinePatch(ColorTools.getColoredTexture(Color.DARK_GRAY, size), 1, 1, 1, 1));
		skin.add(UI_BORDERED_NINE, darkGrayNinePatch);
		skin.add(CONFIRMATION_MENU_BACKGROUND, new NinePatch(ColorTools.getColoredTexture(Color.PINK, size), 1, 1, 1, 1));

		BitmapFont defaultFont = new BitmapFont();
		skin.add("default", defaultFont);
		
		skin.add(DEFAULT_LABEL, new LabelStyle(defaultFont, Color.BLACK));
		skin.add(FLAVOR_TEXT_LABEL, new LabelStyle(defaultFont, Color.CHARTREUSE));
		skin.add(WARNING_LABEL, new LabelStyle(defaultFont, Color.FIREBRICK));
		LabelStyle labelWithBackgroundStyle = new LabelStyle(defaultFont, Color.BLACK);
		labelWithBackgroundStyle.background = skin.get(UI_BORDERED_NINE, NinePatchDrawable.class);
		skin.add(LABEL_WITH_BACKGROUND, labelWithBackgroundStyle);

		SliderStyle sliderStyle = new SliderStyle(new Image(ColorTools.getColoredTexture(Color.GOLDENROD, size)).getDrawable(), skin.get(UI_BORDERED_NINE, NinePatchDrawable.class));
		sliderStyle.knobDown = darkGrayNinePatch;
		skin.add(SLIDER, sliderStyle);

		CheckBoxStyle checkboxStyle = new CheckBoxStyle();
		checkboxStyle.font = defaultFont;
		checkboxStyle.fontColor = Color.BLACK;
		checkboxStyle.checkboxOn = skin.get(UI_BORDERED_NINE, NinePatchDrawable.class);
		checkboxStyle.checkboxOff = darkGrayNinePatch;
		skin.add(CHECKBOX, checkboxStyle);
		
		Drawable buttonNOTHighlighted = new Image(ColorTools.getColoredTexture(Color.WHITE, size)).getDrawable();
		Drawable buttonHighlighted = new Image(ColorTools.getColoredTexture(Color.GRAY, size)).getDrawable();
		TextButtonStyle textButtonStyle = new TextButtonStyle(buttonNOTHighlighted, null, null, defaultFont);
		textButtonStyle.over = buttonHighlighted;
		textButtonStyle.focused = buttonHighlighted;
		textButtonStyle.disabledFontColor = Color.FIREBRICK;
		skin.add(TEXT_BUTTON, textButtonStyle);
		TextButtonStyle flashedTextButtonStyle = new TextButtonStyle(buttonHighlighted, null, null, defaultFont);
		flashedTextButtonStyle.over = buttonNOTHighlighted;
		flashedTextButtonStyle.focused = buttonNOTHighlighted;
		skin.add(FLASHED_TEXT_BUTTON, flashedTextButtonStyle);
		TextButtonStyle sneakyLabelButtonStyle = new TextButtonStyle(new Image(ColorTools.getColoredTexture(Color.CLEAR, size)).getDrawable(), null, null, defaultFont);
		skin.add(SNEAKY_LABEL_BUTTON, sneakyLabelButtonStyle);
		TextButtonStyle blankButtonStyle = new TextButtonStyle(null, null, null, defaultFont);
		blankButtonStyle.focused = null;
		skin.add(BLANK_BUTTON, blankButtonStyle);
		TextButtonStyle settingsButtonStyle = new TextButtonStyle(new Image(ColorTools.getColoredTexture(Color.PURPLE, size)).getDrawable(), null, null, defaultFont);
		Texture settingsHighlightedIcon = ColorTools.getColoredTexture(Color.PINK, size);
		settingsButtonStyle.over = new TextureRegionDrawable(settingsHighlightedIcon);
		settingsButtonStyle.focused = new TextureRegionDrawable(settingsHighlightedIcon);
		skin.add(SETTINGS_BUTTON, settingsButtonStyle);
		
		return skin;
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

	public BaseDrawable getUINinePatch() {
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
