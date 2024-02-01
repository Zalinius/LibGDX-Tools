package com.darzalgames.libgdxtools.ui.input.keyboard.button.skinmanager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;

/**
 * @author DarZal
 * A class which holds a LibGDX {@link Skin} and provides convenient named accessors.
 *  
 */
public class SkinManager {

	protected final Skin skin;
	
	protected static final String UI_BORDERED_NINE = "uiBorderedNine";
	protected static final String CONFIRMATION_MENU_BACKGROUND = "confirmationMenuBackground";

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

	/**
	 * @param skin The skin set up by the base class
	 */
	protected SkinManager(Skin skin) {
		super();
		this.skin = skin;
		ConfirmationMenu.setConfirmationBackground(skin.get(CONFIRMATION_MENU_BACKGROUND, Texture.class));
	}
	
	/**
	 * Start with the default (very basic) skin
	 */
	public SkinManager() {
		this(getDefaultSkin());
	}

	private static Skin getDefaultSkin() {
		Skin skin = new Skin();
		
		NinePatchDrawable darkGrayNinePatch = new NinePatchDrawable(new NinePatch(getColoredTexture(Color.DARK_GRAY), 1, 1, 1, 1));
		skin.add(UI_BORDERED_NINE, darkGrayNinePatch);
		skin.add(CONFIRMATION_MENU_BACKGROUND, getColoredTexture(Color.PINK));

		BitmapFont defaultFont = new BitmapFont();
		skin.add("default", defaultFont);
		
		skin.add(DEFAULT_LABEL, new LabelStyle(defaultFont, Color.BLACK));
		skin.add(FLAVOR_TEXT_LABEL, new LabelStyle(defaultFont, Color.DARK_GRAY));
		skin.add(WARNING_LABEL, new LabelStyle(defaultFont, Color.FIREBRICK));
		LabelStyle labelWithBackgroundStyle = new LabelStyle(defaultFont, Color.BLACK);
		labelWithBackgroundStyle.background = skin.get(UI_BORDERED_NINE, NinePatchDrawable.class);
		skin.add(LABEL_WITH_BACKGROUND, labelWithBackgroundStyle);

		SliderStyle sliderStyle = new SliderStyle(new Image(getColoredTexture(Color.GOLDENROD)).getDrawable(), skin.get(UI_BORDERED_NINE, NinePatchDrawable.class));
		sliderStyle.knobDown = darkGrayNinePatch;
		skin.add(SLIDER, sliderStyle);

		CheckBoxStyle checkboxStyle = new CheckBoxStyle();
		checkboxStyle.font = defaultFont;
		checkboxStyle.fontColor = Color.BLACK;
		checkboxStyle.checkboxOn = skin.get(UI_BORDERED_NINE, NinePatchDrawable.class);
		checkboxStyle.checkboxOff = darkGrayNinePatch;
		skin.add(CHECKBOX, checkboxStyle);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle(new Image(getColoredTexture(Color.LIGHT_GRAY)).getDrawable(), null, null, defaultFont);
//		textButtonStyle.fontColor = fontColor;
//		textButtonStyle.over = buttonHighlight;
//		textButtonStyle.focused = buttonHighlight;
		textButtonStyle.disabledFontColor = Color.DARK_GRAY;
		skin.add(TEXT_BUTTON, textButtonStyle);
		TextButtonStyle flashedTextButtonStyle = new TextButtonStyle(new Image(getColoredTexture(Color.GRAY)).getDrawable(), null, null, defaultFont);
//		flashedTextButtonStyle.over = buttonNOTHighlighted;
//		flashedTextButtonStyle.focused = buttonNOTHighlighted;
		skin.add(FLASHED_TEXT_BUTTON, flashedTextButtonStyle);
		TextButtonStyle sneakyLabelButtonStyle = textButtonStyle;
//		sneakyLabelButtonStyle.fontColor = fontColor;
		skin.add(SNEAKY_LABEL_BUTTON, sneakyLabelButtonStyle);
		TextButtonStyle blankButtonStyle = new TextButtonStyle(null, null, null, defaultFont);
		blankButtonStyle.focused = null;
		skin.add(BLANK_BUTTON, blankButtonStyle);
		TextButtonStyle settingsButtonStyle = new TextButtonStyle(new Image(getColoredTexture(Color.PURPLE)).getDrawable(), null, null, defaultFont);
//		settingsButtonStyle.over = new TextureRegionDrawable(Assets.get(Assets.settingsHighlightedIcon));
//		settingsButtonStyle.focused = new TextureRegionDrawable(Assets.get(Assets.settingsHighlightedIcon));
		skin.add(SETTINGS_BUTTON, settingsButtonStyle);
		
		return skin;
	}
	
	private static Texture getColoredTexture(Color color) {
		int size = 10;
		Pixmap coloredMap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
		coloredMap.setColor(color);
		coloredMap.fillRectangle(0, 0, size, size);
		Texture coloredTexture = new Texture(coloredMap);
		coloredMap.dispose();
		return coloredTexture;
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
