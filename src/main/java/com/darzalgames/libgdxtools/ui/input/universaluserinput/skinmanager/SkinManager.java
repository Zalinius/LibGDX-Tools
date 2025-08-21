package com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
	protected static final String DEFAULT = "default";
	protected static final String FLAVOR_TEXT_LABEL = "flavorTextLabelStyle";
	protected static final String WARNING_LABEL = "warningLabelStyle";
	protected static final String LABEL_WITH_BACKGROUND = "labelWithBackgroundStyle";

	// SliderStyle
	protected static final String SLIDER_DEFAULT = "default-horizontal";

	// CheckboxStyle

	// TextButtonStyle
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
		skin.add(DEFAULT, defaultFont);

		skin.add(DEFAULT, new LabelStyle(defaultFont, Color.BLACK));
		skin.add(FLAVOR_TEXT_LABEL, new LabelStyle(defaultFont, Color.CHARTREUSE));
		skin.add(WARNING_LABEL, new LabelStyle(defaultFont, Color.FIREBRICK));
		LabelStyle labelWithBackgroundStyle = new LabelStyle(defaultFont, Color.BLACK);
		labelWithBackgroundStyle.background = skin.get(UI_BORDERED_NINE, NinePatchDrawable.class);
		skin.add(LABEL_WITH_BACKGROUND, labelWithBackgroundStyle);

		SliderStyle sliderStyle = new SliderStyle(new Image(ColorTools.getColoredTexture(Color.GOLDENROD, size)).getDrawable(), new Image(ColorTools.getColoredTexture(Color.GRAY, size)).getDrawable());
		sliderStyle.knobDown = darkGrayNinePatch;
		sliderStyle.knobOver = darkGrayNinePatch;
		skin.add(SLIDER_DEFAULT, sliderStyle);

		CheckBoxStyle checkboxStyle = new Styles.CheckBoxStyle(new Image(ColorTools.getColoredTexture(Color.DARK_GRAY, size)).getDrawable(), new Image(ColorTools.getColoredTexture(Color.PURPLE, size)).getDrawable(), defaultFont, Color.BLACK);
		skin.add(DEFAULT, checkboxStyle);

		Drawable buttonNOTHighlighted = new Image(ColorTools.getColoredTexture(Color.WHITE, size)).getDrawable();
		Drawable buttonHighlighted = new Image(ColorTools.getColoredTexture(Color.GRAY, size)).getDrawable();
		TextButtonStyle textButtonStyle = new TextButtonStyle(buttonNOTHighlighted, buttonHighlighted, buttonHighlighted, defaultFont);
		textButtonStyle.over = buttonHighlighted;
		textButtonStyle.fontColor = Color.LIGHT_GRAY;
		textButtonStyle.overFontColor = Color.WHITE;
		textButtonStyle.focused = buttonHighlighted;
		textButtonStyle.disabledFontColor = Color.FIREBRICK;
		skin.add(DEFAULT, textButtonStyle);

		Drawable blank = new Image(ColorTools.getColoredTexture(Color.CLEAR, size)).getDrawable();
		TextButtonStyle blankButtonStyle = new TextButtonStyle(blank, blank, blank, defaultFont);
		blankButtonStyle.focused = null;
		skin.add(BLANK_BUTTON, blankButtonStyle);
		TextureRegionDrawable settingsHighlightedIcon = new TextureRegionDrawable(ColorTools.getColoredTexture(Color.PINK, size));
		TextButtonStyle settingsButtonStyle = new TextButtonStyle(new Image(ColorTools.getColoredTexture(Color.PURPLE, size)).getDrawable(), settingsHighlightedIcon, settingsHighlightedIcon, defaultFont);
		settingsButtonStyle.over = settingsHighlightedIcon;
		settingsButtonStyle.focused = settingsHighlightedIcon;
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
	public BaseDrawable getConfirmationMenuBackground() {
		return skin.get(CONFIRMATION_MENU_BACKGROUND, NinePatchDrawable.class);
	}
	public LabelStyle getDefaultLableStyle() {
		return getLabelStyle(DEFAULT);
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
		return getSliderStyle(SLIDER_DEFAULT);
	}
	public CheckBoxStyle getCheckboxStyle() {
		return getCheckboxStyle(DEFAULT);
	}
	public TextButtonStyle getTextButtonStyle() {
		return getTextButtonStyle(DEFAULT);
	}
	public TextButtonStyle getBlankButtonStyle() {
		return getTextButtonStyle(BLANK_BUTTON);
	}
	public TextButtonStyle getSettingsButtonStyle() {
		return getTextButtonStyle(SETTINGS_BUTTON);
	}

}
