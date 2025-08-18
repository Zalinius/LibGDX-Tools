package com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.Styles;
import com.github.tommyettinger.textra.Styles.CheckBoxStyle;
import com.github.tommyettinger.textra.Styles.LabelStyle;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;

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
	}

	/**
	 * @return the default (very basic) skin
	 */
	public static Skin getDefaultSkin() {
		Skin skin = new Skin();

		int size = 4;

		NinePatchDrawable darkGrayNinePatch = new NinePatchDrawable(new NinePatch(ColorTools.getColoredTexture(Color.DARK_GRAY, size), 1, 1, 1, 1));
		darkGrayNinePatch.setMinWidth(size);
		darkGrayNinePatch.setMinHeight(size);
		skin.add(UI_BORDERED_NINE, darkGrayNinePatch);
		skin.add(CONFIRMATION_MENU_BACKGROUND, new NinePatchDrawable(new NinePatch(ColorTools.getColoredTexture(Color.PINK, size), 1, 1, 1, 1)));

		Font defaultFont = new Font();
		skin.add(DEFAULT_LABEL, defaultFont);

		skin.add(DEFAULT_LABEL, new LabelStyle(defaultFont, Color.BLACK));
		skin.add(FLAVOR_TEXT_LABEL, new LabelStyle(defaultFont, Color.CHARTREUSE));
		skin.add(WARNING_LABEL, new LabelStyle(defaultFont, Color.FIREBRICK));
		LabelStyle labelWithBackgroundStyle = new LabelStyle(defaultFont, Color.BLACK);
		labelWithBackgroundStyle.background = skin.get(UI_BORDERED_NINE, NinePatchDrawable.class);
		skin.add(LABEL_WITH_BACKGROUND, labelWithBackgroundStyle);

		SliderStyle sliderStyle = new SliderStyle(new Image(ColorTools.getColoredTexture(Color.GOLDENROD, size)).getDrawable(), darkGrayNinePatch);
		sliderStyle.knobDown = darkGrayNinePatch;
		skin.add(SLIDER, sliderStyle);

		CheckBoxStyle checkboxStyle = new Styles.CheckBoxStyle(new Image(ColorTools.getColoredTexture(Color.DARK_GRAY, size)).getDrawable(), new Image(ColorTools.getColoredTexture(Color.PURPLE, size)).getDrawable(), defaultFont, Color.BLACK);
		skin.add(CHECKBOX, checkboxStyle);

		Drawable buttonNOTHighlighted = new Image(ColorTools.getColoredTexture(Color.WHITE, size)).getDrawable();
		Drawable buttonHighlighted = new Image(ColorTools.getColoredTexture(Color.GRAY, size)).getDrawable();
		TextButtonStyle textButtonStyle = new TextButtonStyle(buttonNOTHighlighted, null, null, defaultFont);
		textButtonStyle.over = buttonHighlighted;
		textButtonStyle.fontColor = Color.LIGHT_GRAY;
		textButtonStyle.overFontColor = Color.WHITE;
		textButtonStyle.focused = buttonHighlighted;
		textButtonStyle.disabledFontColor = Color.FIREBRICK;
		skin.add(TEXT_BUTTON, textButtonStyle);
		TextButtonStyle flashedTextButtonStyle = new TextButtonStyle(buttonHighlighted, null, null, defaultFont);
		flashedTextButtonStyle.over = buttonNOTHighlighted;
		textButtonStyle.fontColor = Color.LIGHT_GRAY;
		flashedTextButtonStyle.overFontColor = Color.WHITE;
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
		if (!skin.has(style, LabelStyle.class)) {
			Label.LabelStyle sceneStyle = skin.get(style, Label.LabelStyle.class);
			String fontName = sceneStyle.font.getData().name;
			fontName = fontName.substring(0, fontName.length() - 3);
			skin.add(style, new LabelStyle(skin.get(fontName, Font.class), sceneStyle.fontColor, sceneStyle.background));
		}
		return skin.get(style, LabelStyle.class);
	}
	protected TextButtonStyle getTextButtonStyle(String style) {
		if (!skin.has(style, TextButtonStyle.class)) {
			TextButton.TextButtonStyle sceneStyle = skin.get(style, TextButton.TextButtonStyle.class);
			String fontName = sceneStyle.font.getData().name;
			fontName = fontName.substring(0, fontName.length() - 3);
			TextButtonStyle textraStyle = new TextButtonStyle(sceneStyle.up, sceneStyle.down, sceneStyle.checked, skin.get(fontName, Font.class));
			textraStyle.fontColor = sceneStyle.fontColor;
			textraStyle.overFontColor = sceneStyle.overFontColor;
			skin.add(style, textraStyle);
		}

		return skin.get(style, TextButtonStyle.class);
	}
	protected CheckBoxStyle getCheckboxStyle(String style) {
		if (!skin.has(style, CheckBoxStyle.class)) {
			CheckBox.CheckBoxStyle sceneStyle = skin.get(style, CheckBox.CheckBoxStyle.class);
			String fontName = sceneStyle.font.getData().name;
			fontName = fontName.substring(0, fontName.length() - 3);
			skin.add(style, new CheckBoxStyle(sceneStyle.checkboxOff, sceneStyle.checkboxOn, skin.get(fontName, Font.class), sceneStyle.fontColor));
		}
		return skin.get(style, CheckBoxStyle.class);
	}
	protected SliderStyle getSliderStyle(String style) {
		return skin.get(style, SliderStyle.class);
	}

	public BaseDrawable getUINinePatch() {
		return skin.get(UI_BORDERED_NINE, NinePatchDrawable.class);
	}
	public BaseDrawable getConfirmationMenuBackground() {
		return skin.get(CONFIRMATION_MENU_BACKGROUND, NinePatchDrawable.class);
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
