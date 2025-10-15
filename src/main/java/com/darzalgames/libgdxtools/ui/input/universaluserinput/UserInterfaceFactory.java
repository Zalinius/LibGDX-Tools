package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerSelectBox;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.MultipleStage;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.handler.FallbackGamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.SelectBoxContentManager.SelectBoxButtonInfo;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager.SkinManager;
import com.github.tommyettinger.textra.Styles.LabelStyle;

/**
 * The ONLY place where one should be making UI elements (buttons, labels, etc)
 */
public abstract class UserInterfaceFactory {

	private final Runnable quitGameRunnable;
	private final SkinManager skinManager;
	private final InputStrategySwitcher inputStrategySwitcher;
	private final Runnable soundInteractRunnable;

	private final FallbackGamepadInputHandler sampleGlyphSupplierForSizeReference;

	private static final String QUIT_GAME_KEY = "quit_game";

	public UserInterfaceFactory(SkinManager skinManager, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractRunnable, FallbackGamepadInputHandler sampleGlyphSupplierForSizeReference) {
		this.skinManager = skinManager;
		this.inputStrategySwitcher = inputStrategySwitcher;
		this.soundInteractRunnable = soundInteractRunnable;

		this.sampleGlyphSupplierForSizeReference = sampleGlyphSupplierForSizeReference;
		/*
		 * We use an un-connected copy of the fallback gamepad system to get glyphs as a size reference: the Steam glyphs are higher-res
		 * and since I work with the fallback system while designing the UI, this ensures that the Steam glyphs layout nicely / the same.
		 */
		Controllers.removeListener(sampleGlyphSupplierForSizeReference);

		quitGameRunnable = Gdx.app::exit;
	}

	protected abstract void addGameSpecificHighlightListener(UniversalDoodad button);

	public UniversalLabel getLabel(final Supplier<String> textSupplier) {
		return getLabel(textSupplier, skinManager.getDefaultLableStyle());
	}

	public UniversalLabel getFlavorTextLabel(final Supplier<String> textSupplier) {
		return getLabel(() -> "[/]" + textSupplier.get() + "[/]", skinManager.getFlavorTextLableStyle());
	}

	public UniversalLabel getWarningLabel(final Supplier<String> textSupplier) {
		return getLabel(textSupplier, skinManager.getWarningLableStyle());
	}

	public UniversalLabel getLabelWithBackground(final Supplier<String> textSupplier) {
		return getLabel(textSupplier, skinManager.getLabelWithBackgroundStyle());
	}

	/**
	 * Makes a label which changes its text based on the current input mode
	 * @param textSupplier supplier for the desired localized and input-sensitive text
	 * @return a UniversalLabel with a background, whose text changes when the input strategy changes
	 */
	public UniversalLabel getInputSensitiveLabelWithBackground(Supplier<String> textSupplier) {
		return new UniversalInputSensitiveLabel(textSupplier, skinManager.getLabelWithBackgroundStyle(), inputStrategySwitcher);
	}

	protected UniversalLabel getLabel(final Supplier<String> textSupplier, LabelStyle labelStyle) {
		return new UniversalLabel(textSupplier, labelStyle);
	}

	/**
	 * Makes a spacer which can be listed among other buttons, but isn't interactable and which will
	 * expand out to fill any available space in the menu
	 * @return a blank UniversalButton
	 */
	public UniversalDoodad getSpacer() {
		UniversalButton spacer = new UniversalButton(Runnables.nullRunnable(), inputStrategySwitcher, soundInteractRunnable, skinManager.getBlankButtonStyle()) {
			@Override
			public void setAlignment(Alignment alignment) {}

			@Override
			public boolean isBlank() {
				return true;
			}

			@Override
			public void colorOtherComponentsBasedOnFocus(Color color) { /* not needed */ }

			@Override
			public void setDisabled(boolean disabled) { /* stay disabled */ }

			@Override
			public boolean isDisabled() {
				return true;
			}
		};
		spacer.setName("spacer");
		return spacer;
	}

	protected UniversalButton getImageButton(final Image image, final Runnable runnable, ButtonStyle style) {
		UniversalButton button = new UniversalButton(runnable, inputStrategySwitcher, soundInteractRunnable, style) {
			@Override
			public boolean isBlank() {
				return image != null;
			}

			@Override
			public void setAlignment(Alignment alignment) {
				getCell(image).align(alignment.getAlignment());
			}

			@Override
			public void colorOtherComponentsBasedOnFocus(Color color) {
				image.setColor(color);
			}
		};
		button.add(image);
		addGameSpecificHighlightListener(button);
		return button;
	}

	public UniversalButton getImageButton(final Image image, final Runnable runnable) {
		return getImageButton(image, runnable, skinManager.getDefaultButtonStyle());
	}

	public UniversalTextButton makeTextButton(Supplier<String> textSupplier, final Runnable runnable) {
		return makeTextButton(textSupplier, runnable, Input.NONE);
	}

	public UniversalTextButton makeTextButton(Supplier<String> textSupplier, final Runnable runnable, Input inputForGlyph) {
		return makeTextButtonWithStyle(textSupplier, runnable, skinManager.getDefaultButtonStyle(), skinManager.getDefaultLableStyle(), inputForGlyph);
	}

	protected UniversalTextButton makeTextButtonWithStyle(Supplier<String> textSupplier, final Runnable runnable, ButtonStyle style, LabelStyle labelStyle, Input inputForGlyph) {
		UniversalLabel label = new UniversalLabel(textSupplier, labelStyle);
		UniversalTextButton button = new UniversalTextButton(label, runnable, inputStrategySwitcher, soundInteractRunnable, style);
		addGameSpecificHighlightListener(button);
		if (inputForGlyph != Input.NONE) {
			ControlsGlyph glyph = getControlsGlyphForButton(inputForGlyph, button);
			button.addActor(glyph);
		}
		return button;
	}

	public UniversalSelectBox getSelectBox(SelectBoxContentManager contentManager) {
		String boxLabel = contentManager.getBoxLabelKey();
		List<SelectBoxButtonInfo> entries = contentManager.getOptionButtons();
		UniversalSelectBox selectBox = new UniversalSelectBox(boxLabel, inputStrategySwitcher, soundInteractRunnable, skinManager.getDefaultButtonStyle());
		List<UniversalTextButton> entriesButtons = entries.stream().map(buttonInfo -> makeTextButton(buttonInfo.buttonTextSupplier(), () -> {
			buttonInfo.buttonPressRunnable().run();
			selectBox.setSelected(buttonInfo.buttonTextSupplier().get());
			selectBox.closeSelectBox();
		})).toList();
		selectBox.setEntryButtons(entriesButtons);
		selectBox.setSelected(contentManager.getCurrentSelectedDisplayName().get());
		addGameSpecificHighlightListener(selectBox);
		return selectBox;
	}

	public BaseDrawable getCompactBackgroundDrawable() {
		return skinManager.getCompactUINinePatch();
	}

	public BaseDrawable getDefaultBackgroundDrawable() {
		return skinManager.getUINinePatch();
	}

	public BaseDrawable getConfirmationMenuBackground() {
		return skinManager.getConfirmationMenuBackground();
	}

	public UniversalSlider getSlider(Supplier<String> textSupplier, Consumer<Float> consumer) {
		UniversalLabel label = new UniversalLabel(textSupplier, skinManager.getDefaultLableStyle());
		UniversalSlider button = new UniversalSlider(label, skinManager.getSliderStyle(), skinManager.getBlankButtonStyle(), consumer, inputStrategySwitcher, soundInteractRunnable, 0.05f);
		addGameSpecificHighlightListener(button);
		return button;
	}

	public UniversalCheckbox getCheckbox(Supplier<String> uncheckedLabel, Supplier<String> checkedLabel, Consumer<Boolean> consumer) {
		UniversalCheckbox button = new UniversalCheckbox(uncheckedLabel, checkedLabel, consumer, skinManager.getCheckboxStyle(), skinManager.getBlankButtonStyle(), inputStrategySwitcher, soundInteractRunnable);
		addGameSpecificHighlightListener(button);
		return button;
	}

	public UniversalButton getOptionsButton(Consumer<Boolean> toggleOptionsScreenVisibility) {
		UniversalButton button = new UniversalButton(() -> toggleOptionsScreenVisibility.accept(true), inputStrategySwitcher, soundInteractRunnable, skinManager.getSettingsButtonStyle()) {
			@Override
			public String toString() {
				return "options button";
			}

			@Override
			public boolean isBlank() {
				return false;
			}

			@Override
			public void setAlignment(Alignment alignment) { /* nothing? */ }

			@Override
			public void colorOtherComponentsBasedOnFocus(Color color) { /* not needed */ }
		};
		button.setSize(button.getStyle().up.getMinWidth(), button.getStyle().up.getMinHeight());
		ControlsGlyph glyph = getControlsGlyphForButton(Input.PAUSE, button);
		glyph.setAlignment(Alignment.BOTTOM_RIGHT);
		button.addActor(glyph);
		return button;
	}

	public UniversalButton makeBackButton(Runnable runnable) {
		return makeTextButtonWithStyle(() -> TextSupplier.getLine("back_message"), runnable, skinManager.getBackButtonStyle(), skinManager.getDefaultLableStyle(), Input.BACK);
	}

	/**
	 * @param buttonText The text on the quit button
	 * @return A quit button, with a default English text label if not otherwise to find
	 */
	public UniversalTextButton getQuitGameButton(Supplier<String> buttonText) {
		UniversalTextButton button = makeTextButton(buttonText, quitGameRunnable, Input.NONE);
		button.setColor(Color.SALMON);
		return button;
	}

	/**
	 * @return A quit button, with a default English text label if not otherwise to find
	 */
	public UniversalButton getQuitGameButton() {
		return getQuitGameButton(getQuitButtonString());
	}

	private Supplier<String> getQuitButtonString() {
		Supplier<String> text;
		try {
			text = () -> TextSupplier.getLine(QUIT_GAME_KEY);
		} catch (NullPointerException e) {
			// if there's no internationalization bundle
			text = () -> "Quit";
		}
		return text;
	}

	public UniversalTextButton getQuitGameButtonWithWarning() {
		Runnable quitWithConfirmation = () -> new ConfirmationMenu("menu_warning", QUIT_GAME_KEY, quitGameRunnable::run, MultipleStage.OPTIONS_STAGE_NAME);
		UniversalTextButton button = makeTextButton(getQuitButtonString(), quitWithConfirmation, Input.NONE);
		button.setColor(Color.SALMON);
		return button;
	}

	public WindowResizerSelectBox getWindowModeTextSelectBox() {
		String textKey = "window_mode_label";
		WindowResizerSelectBox button = new WindowResizerSelectBox(textKey, inputStrategySwitcher, soundInteractRunnable, skinManager.getDefaultButtonStyle());
		addGameSpecificHighlightListener(button);
		return button;
	}

	public ControlsGlyph getControlsGlyphForButton(Input input, UniversalButton button) {
		return getControlsGlyph(input, () -> !button.isDisabled());
	}

	public ControlsGlyph getControlsGlyph(Input input, Supplier<Boolean> parentIsEnabled) {
		Texture texture = sampleGlyphSupplierForSizeReference.getGlyphForInput(input);
		if (texture == null) {
			Gdx.app.error("GlyphFactory", "Missing glyph setup for: " + input);
			texture = new Texture(30, 30, Format.RGBA8888);
		}
		return new ControlsGlyph(input, inputStrategySwitcher, texture, parentIsEnabled);
	}

	protected InputStrategySwitcher getInputStrategySwitcher() {
		return inputStrategySwitcher;
	}

	protected Runnable getSoundInteractListener() {
		return soundInteractRunnable;
	}

}
