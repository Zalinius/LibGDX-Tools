package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.darzalgames.darzalcommon.functional.Suppliers;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerSelectBox;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.MultipleStage;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.SelectBoxContentManager;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.SelectBoxContentManager.SelectBoxButtonInfo;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UniversalDoodad;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager.SkinManager;
import com.github.tommyettinger.textra.Styles.LabelStyle;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;

/**
 * The ONLY place where one should be making UI elements (buttons, labels, etc)
 */
public class UserInterfaceFactory {

	private final Runnable quitGameRunnable;
	private final SkinManager skinManager;
	private final InputStrategySwitcher inputStrategySwitcher;
	private final Supplier<Float> flashesPerSecondSupplier;
	private final Runnable soundInteractRunnable;

	private static final String QUIT_GAME_KEY = "quit_game";

	public UserInterfaceFactory(SkinManager skinManager, InputStrategySwitcher inputStrategySwitcher, Supplier<Float> flashesPerSecondSupplier, Runnable soundInteractRunnable) {
		this.skinManager = skinManager;
		this.inputStrategySwitcher = inputStrategySwitcher;
		this.flashesPerSecondSupplier = flashesPerSecondSupplier;
		this.soundInteractRunnable = soundInteractRunnable;
		quitGameRunnable = Gdx.app::exit;
	}

	public UniversalLabel getLabel(final Supplier<String> textSupplier) {
		return getLabel(textSupplier, skinManager.getDefaultLableStyle(), skinManager.getBlankButtonStyle());
	}

	public UniversalLabel getFlavorTextLabel(final Supplier<String> textSupplier) {
		return getLabel(textSupplier, skinManager.getFlavorTextLableStyle(), skinManager.getBlankButtonStyle());
	}

	public UniversalLabel getWarningLabel(final Supplier<String> textSupplier) {
		return getLabel(textSupplier, skinManager.getWarningLableStyle(), skinManager.getBlankButtonStyle());
	}

	public UniversalLabel getLabelWithBackground(final Supplier<String> textSupplier) {
		return getLabel(textSupplier, skinManager.getLabelWithBackgroundStyle(), skinManager.getBlankButtonStyle());
	}

	/**
	 * Makes a label which changes its text based on the current input mode
	 * @param textSupplier supplier for the desired localized and input-sensitive text
	 * @return a UniversalLabel with a background, whose text changes when the input strategy changes
	 */
	public UniversalLabel getInputSensitiveLabelWithBackground(Supplier<String> textSupplier) {
		return new UniversalInputSensitiveLabel(textSupplier, skinManager.getLabelWithBackgroundStyle(), skinManager.getBlankButtonStyle(), inputStrategySwitcher);
	}

	protected UniversalLabel getLabel(final Supplier<String> textSupplier, LabelStyle labelStyle, TextButtonStyle buttonStyle) {
		return new UniversalLabel(textSupplier, labelStyle, buttonStyle);
	}


	/**
	 * Makes a label which can be listed among other buttons, but isn't interactable
	 * @param textSupplier supplier for the desired localized text
	 * @return a disabled UniversalButton with the specified label text
	 */
	//	public UniversalButton getListableLabel(Supplier<String> textSupplier) {
	//		UniversalLabel label = new UniversalLabel(textSupplier, skinManager.)
	//
	//				// a bit of hack so that a label-like button can be stored in a list of buttons but not be interactable
	//				BasicButton textButton = makeLibGDXTextButton(textSupplier.get(), skinManager.getSneakyLableButtonStyle());
	//		textButton.setName(textSupplier.get());
	//		UniversalButton listableButton = new UniversalButton(textButton, textSupplier, Runnables.nullRunnable(), inputStrategySwitcher, soundInteractListener);
	//		listableButton.setDisabled(true);
	//		return listableButton;
	//	}

	/**
	 * Makes a spacer which can be listed among other buttons, but isn't interactable and which will
	 * expand out to fill any available space in the menu
	 * @return a blank UniversalButton
	 */
	public UniversalDoodad getSpacer() {
		UniversalLabel spacer = getLabel(Suppliers.emptyString());
		spacer.getView().setName("spacer");
		spacer.setDisabled(true);
		return spacer;
	}


	public UniversalButton getImageButton(final Image image, final Runnable runnable) {
		UniversalButton button = new UniversalButton(runnable, inputStrategySwitcher, soundInteractRunnable, skinManager.getTextButtonStyle()) {
			@Override
			public boolean isBlank() {
				return image != null;
			}
			@Override
			public void setAlignment(Alignment alignment) {
				getCell(image).align(alignment.getAlignment());
			}
		};
		button.add(image);
		return button;
	}

	//	public UniversalButton getButton(Supplier<String> textKey, Image image, final Runnable runnable) {
	//		return makeButton(textKey, image, runnable);
	//	}

	private UniversalButton makeButton(Supplier<String> textSupplier, Image image, final Runnable runnable) {
		UniversalLabel label = new UniversalLabel(textSupplier, skinManager.getDefaultLableStyle(), skinManager.getBlankButtonStyle());
		//		makeBackgroundFlashing(textButton, skinManager.getTextButtonStyle(), skinManager.getFlashedTextButtonStyle());
		UniversalButton button = new UniversalButton(runnable, inputStrategySwitcher, soundInteractRunnable, skinManager.getTextButtonStyle()) {
			@Override
			public boolean isBlank() {
				return label.isBlank();
			}
			@Override
			public void setAlignment(Alignment alignment) {
				label.setAlignment(alignment);
			}
		};
		button.add(image);
		button.add(label);
		return button;
	}

	public UniversalTextButton makeTextButton(Supplier<String> textSupplier, final Runnable runnable) {
		UniversalLabel label = new UniversalLabel(textSupplier, skinManager.getDefaultLableStyle(), skinManager.getBlankButtonStyle());
		return new UniversalTextButton(label, runnable, inputStrategySwitcher, soundInteractRunnable, skinManager.getTextButtonStyle());
	}

	/**
	 * Make a button in a particular style, these are generally exceptional buttons (in Quest Giver this includes the play button, scenario map pips, etc)
	 */
	protected UniversalButton makeButton(final String text, final Runnable runnable, TextButtonStyle textButtonStyle) {
		return new UniversalButton(runnable, inputStrategySwitcher, soundInteractRunnable, textButtonStyle) {

			@Override
			public boolean isBlank() {
				return text.isBlank();
			}

			@Override
			public void setAlignment(Alignment alignment) {
				// TODO Auto-generated method stub

			}

		};
	}


	public UniversalSelectBox getSelectBox(SelectBoxContentManager contentManager) {
		Supplier<String> boxLabel = contentManager.getBoxLabelSupplier();
		List<SelectBoxButtonInfo> entries = contentManager.getOptionButtons();
		//		BasicButton textButton = makeLibGDXTextButton(boxLabel.get(), skinManager.getTextButtonStyle());
		//		makeBackgroundFlashing(textButton, skinManager.getTextButtonStyle(), skinManager.getFlashedTextButtonStyle());
		UniversalSelectBox selectBox = new UniversalSelectBox(boxLabel, inputStrategySwitcher, soundInteractRunnable, skinManager.getTextButtonStyle());
		List<UniversalTextButton> entriesButtons = entries.stream().map(buttonInfo -> makeTextButton(buttonInfo.buttonTextSupplier(), () -> {
			buttonInfo.buttonPressRunnable().run();
			selectBox.setSelected(buttonInfo.buttonTextSupplier().get());
			selectBox.closeSelectBox();
		})).toList();
		selectBox.setEntryButtons(entriesButtons);
		selectBox.setSelected(contentManager.getCurrentSelectedDisplayName().get());
		return selectBox;
	}

	public BaseDrawable getDefaultBackgroundDrawable() {
		return skinManager.getUINinePatch();
	}

	public BaseDrawable getConfirmationMenuBackground() {
		return skinManager.getConfirmationMenuBackground();
	}

	public UniversalSlider getSlider(Supplier<String> textSupplier, Consumer<Float> consumer) {
		UniversalLabel label = new UniversalLabel(textSupplier, skinManager.getDefaultLableStyle(), skinManager.getBlankButtonStyle());
		return new UniversalSlider(label, skinManager.getSliderStyle(), skinManager.getBlankButtonStyle(), consumer, inputStrategySwitcher, soundInteractRunnable, 0.01f);
	}

	public UniversalCheckbox getCheckbox(Supplier<String> uncheckedLabel, Supplier<String> checkedLabel, Consumer<Boolean> consumer) {
		return new UniversalCheckbox(uncheckedLabel, checkedLabel, consumer, skinManager.getCheckboxStyle(), skinManager.getTextButtonStyle(), inputStrategySwitcher, soundInteractRunnable);
	}

	public UniversalButton getOptionsButton(Consumer<Boolean> toggleOptionsScreenVisibility) {
		return new UniversalButton(() -> toggleOptionsScreenVisibility.accept(true), inputStrategySwitcher, soundInteractRunnable, skinManager.getSettingsButtonStyle()) {
			@Override public String toString() { return "options button"; }
			@Override public boolean isBlank() { return false; }
			@Override public void setAlignment(Alignment alignment) { /* nothing? */ }
		};
	}

	/**
	 * @param buttonText
	 * @return A quit button, with a default English text label if not otherwise to find
	 */
	public UniversalTextButton getQuitGameButton(Supplier<String> buttonText) {
		return makeTextButton(buttonText, quitGameRunnable);
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
		return makeTextButton(getQuitButtonString(), quitWithConfirmation);
	}

	//	protected void makeBackgroundFlashing(BasicButton button, ButtonStyle mainButtonStyle, ButtonStyle flashedButtonStyle) {
	//		button.addListener(new ClickListener() {
	//			@Override
	//			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
	//				super.enter(event, x, y, pointer, fromActor);
	//				if (StageBest.isHoverEvent(pointer) && button.isTouchable()) {
	//					button.setStyle(mainButtonStyle);
	//					if (shouldButtonFlash(button)) {
	//						button.clearActions();
	//						button.addAction(new InstantForeverAction(new InstantSequenceAction(
	//								getChangeButtonStyleAfterDelayAction(button, flashedButtonStyle),
	//								getChangeButtonStyleAfterDelayAction(button, mainButtonStyle)
	//								)));
	//
	//					}
	//				}
	//			}
	//
	//			@Override
	//			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
	//				super.exit(event, x, y, pointer, toActor);
	//				if (StageBest.isHoverEvent(pointer)) {
	//					button.clearActions();
	//					button.setStyle(mainButtonStyle);
	//				}
	//			}
	//		});
	//	}
	//
	//	protected void makeSliderFlashing(UniversalSlider keyboardSlider, SliderStyle mainStyle, SliderStyle flashedStyle) {
	//		BasicButton button = keyboardSlider.getButton();
	//		button.addListener(new ClickListener() {
	//			@Override
	//			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
	//				super.enter(event, x, y, pointer, fromActor);
	//				if (StageBest.isHoverEvent(pointer) && button.isTouchable()) {
	//					keyboardSlider.setSliderStyle(flashedStyle);
	//					if (shouldButtonFlash(button)) {
	//						button.clearActions();
	//						button.addAction(new InstantForeverAction(new InstantSequenceAction(
	//								getChangeSliderStyleAfterDelayAction(keyboardSlider, flashedStyle),
	//								getChangeSliderStyleAfterDelayAction(keyboardSlider, mainStyle)
	//								)));
	//
	//					}
	//				}
	//			}
	//
	//			@Override
	//			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
	//				super.exit(event, x, y, pointer, toActor);
	//				if (StageBest.isHoverEvent(pointer)) {
	//					button.clearActions();
	//					keyboardSlider.setSliderStyle(mainStyle);
	//				}
	//			}
	//		});
	//	}
	//
	//	private boolean shouldButtonFlash(BasicButton button) {
	//		return StageBest.isInTouchableBranch(button.getView())
	//				&& !button.isDisabled()
	//				&& inputStrategySwitcher.shouldFlashButtons();
	//	}
	//
	//	private Action getChangeButtonStyleAfterDelayAction(BasicButton button, ButtonStyle buttonStyle) {
	//		return new DelayRunnableAction(computeDelay(),
	//				() -> {
	//					if (shouldButtonFlash(button)) {
	//						button.setStyle(buttonStyle);
	//					}
	//				});
	//	}
	//
	//	private Action getChangeSliderStyleAfterDelayAction(UniversalSlider keyboardSlider, SliderStyle sliderStyle) {
	//		return new DelayRunnableAction(computeDelay(), () -> {
	//			if (shouldButtonFlash(keyboardSlider.getButton())) {
	//				keyboardSlider.setSliderStyle(sliderStyle);
	//			}
	//		});
	//	}

	public WindowResizerSelectBox getWindowModeTextSelectBox() {
		Supplier<String> textSupplier = () -> TextSupplier.getLine("window_mode_label");
		//		BasicButton textButton = makeLibGDXTextButton(textSupplier.get(), skinManager.getTextButtonStyle());
		//		makeBackgroundFlashing(textButton, skinManager.getTextButtonStyle(), skinManager.getFlashedTextButtonStyle());
		return new WindowResizerSelectBox(textSupplier, inputStrategySwitcher, soundInteractRunnable, skinManager.getTextButtonStyle());
	}

	private float computeDelay() {
		return 1f / flashesPerSecondSupplier.get();
	}

	protected InputStrategySwitcher getInputStrategySwitcher() {
		return inputStrategySwitcher;
	}

	protected Runnable getSoundInteractListener() {
		return soundInteractRunnable;
	}

}
