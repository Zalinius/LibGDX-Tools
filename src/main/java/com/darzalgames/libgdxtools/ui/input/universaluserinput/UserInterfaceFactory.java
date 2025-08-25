package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerSelectBox;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.MultipleStage;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
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

	private static final String QUIT_GAME_KEY = "quit_game";

	public UserInterfaceFactory(SkinManager skinManager, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractRunnable) {
		this.skinManager = skinManager;
		this.inputStrategySwitcher = inputStrategySwitcher;
		this.soundInteractRunnable = soundInteractRunnable;
		quitGameRunnable = Gdx.app::exit;
	}


	protected abstract void addGameSpecificHighlightListener(Group button);

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
			@Override public void setAlignment(Alignment alignment) { }
			@Override public boolean isBlank() { return true; }
			@Override public void colorOtherComponentsBasedOnFocus(Color color)  { /* not needed */ }
		};
		spacer.setName("spacer");
		spacer.setDisabled(true);
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
		return makeTextButtonWithStyle(textSupplier, runnable, skinManager.getDefaultButtonStyle(), skinManager.getDefaultLableStyle());
	}

	protected UniversalTextButton makeTextButtonWithStyle(Supplier<String> textSupplier, final Runnable runnable, ButtonStyle style, LabelStyle labelStyle) {
		UniversalLabel label = new UniversalLabel(textSupplier, labelStyle);
		UniversalTextButton button = new UniversalTextButton(label, runnable, inputStrategySwitcher, soundInteractRunnable, style);
		addGameSpecificHighlightListener(button);
		return button;
	}



	public UniversalSelectBox getSelectBox(SelectBoxContentManager contentManager) {
		String boxLabel = contentManager.getBoxLabelKey();
		List<SelectBoxButtonInfo> entries = contentManager.getOptionButtons();
		//		BasicButton textButton = makeLibGDXTextButton(boxLabel.get(), skinManager.getButtonStyle());
		//		makeBackgroundFlashing(textButton, skinManager.getButtonStyle(), skinManager.getFlashedButtonStyle());
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
		return new UniversalButton(() -> toggleOptionsScreenVisibility.accept(true), inputStrategySwitcher, soundInteractRunnable, skinManager.getSettingsButtonStyle()) {
			@Override public String toString() { return "options button"; }
			@Override public boolean isBlank() { return false; }
			@Override public void setAlignment(Alignment alignment) { /* nothing? */ }
			@Override public void colorOtherComponentsBasedOnFocus(Color color)  { /* not needed */ }
		};
	}

	/**
	 * @param buttonText
	 * @return A quit button, with a default English text label if not otherwise to find
	 */
	public UniversalTextButton getQuitGameButton(Supplier<String> buttonText) {
		UniversalTextButton button = makeTextButton(buttonText, quitGameRunnable);
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
		UniversalTextButton button = makeTextButton(getQuitButtonString(), quitWithConfirmation);
		button.setColor(Color.SALMON);
		return button;
	}

	public WindowResizerSelectBox getWindowModeTextSelectBox() {
		String textKey = "window_mode_label";
		WindowResizerSelectBox button = new WindowResizerSelectBox(textKey, inputStrategySwitcher, soundInteractRunnable, skinManager.getDefaultButtonStyle());
		addGameSpecificHighlightListener(button);
		return button;
	}

	protected InputStrategySwitcher getInputStrategySwitcher() {
		return inputStrategySwitcher;
	}

	protected Runnable getSoundInteractListener() {
		return soundInteractRunnable;
	}

}
