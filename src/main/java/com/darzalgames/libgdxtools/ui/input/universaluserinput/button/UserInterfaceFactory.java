package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.darzalcommon.functional.Suppliers;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerSelectBox;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantForeverAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantSequenceAction;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStage;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager.SkinManager;

/**
 * The ONLY place where one should be making UI elements (buttons, labels, etc)
 */
public class UserInterfaceFactory {

	protected Runnable quitGameRunnable;
	protected SkinManager skinManager;
	protected InputStrategySwitcher inputStrategySwitcher;
	private Supplier<Float> flashesPerSecondSupplier;
	protected Runnable soundInteractListener;
	private Supplier<Boolean> isPaused;

	private final String QUIT_GAME_KEY = "quit_game";

	public UserInterfaceFactory(SkinManager skinManager, InputStrategySwitcher inputStrategySwitcher, Supplier<Float> flashesPerSecondSupplier, Runnable soundInteractListener,
			Supplier<Boolean> isPaused) {
		this.skinManager = skinManager;
		this.inputStrategySwitcher = inputStrategySwitcher;
		this.flashesPerSecondSupplier = flashesPerSecondSupplier;
		this.soundInteractListener = soundInteractListener;
		this.isPaused = isPaused;
		this.quitGameRunnable = Gdx.app::exit;
	}

	public UniversalLabel getLabel(final Supplier<String> textSupplier) {
		return getLabel(textSupplier, skinManager.getDefaultLableStyle());
	}

	public UniversalLabel getFlavorTextLabel(final Supplier<String> textSupplier) {
		return getLabel(textSupplier, skinManager.getFlavorTextLableStyle());
	}

	public UniversalLabel getWarningLabel(final Supplier<String> textSupplier) {
		return getLabel(textSupplier, skinManager.getWarningLableStyle());
	}

	public UniversalLabel getLabelWithBackground(final Supplier<String> textSupplier) {
		return getLabel(textSupplier, skinManager.getLabelWithBackgroundStyle());
	}

	/**
	 * Makes a label which changes its text based on the current input mode
	 * @param textSupplier
	 * @return
	 */
	public UniversalLabel getInputSensitiveLabelWithBackground(Supplier<String> textSupplier) {
		return new UniversalInputSensitiveLabel(textSupplier, skinManager.getLabelWithBackgroundStyle(), inputStrategySwitcher);
	}

	protected UniversalLabel getLabel(final Supplier<String> textSupplier, LabelStyle labelStyle) {
		return new UniversalLabel(textSupplier, labelStyle);
	}

	/**
	 * Makes a label which can be listed among other buttons, but isn't interactable
	 * @param textSupplier
	 * @return
	 */
	public UniversalButton getListableLabel(Supplier<String> textSupplier) {
		// a bit of hack so that a label-like button can be stored in a list of buttons but not be interactable
		TextButton textButton = makeLibGDXTextButton(textSupplier.get(), skinManager.getSneakyLableButtonStyle());
		textButton.setName(textSupplier.get());
		UniversalButton listableButton = new UniversalButton(textButton, textSupplier, Runnables.nullRunnable(), inputStrategySwitcher, soundInteractListener);
		listableButton.setDisabled(true);
		return listableButton;
	}

	/**
	 * Makes a spacer which can be listed among other buttons, but isn't interactable and which will
	 * expand out to fill any available space in the menu
	 * @return
	 */
	public UniversalButton getSpacer() {
		UniversalButton spacer = getListableLabel(Suppliers.emptyString());
		spacer.getView().setName("spacer");
		return spacer;
	}

	public boolean isSpacer(UniversalButton button) {
		return button.getView().isDisabled() && button.isBlank();
	}


	public UniversalButton getButton(Supplier<String> textKey, final Runnable runnable) {
		return makeButton(textKey, null, runnable);
	}

	public UniversalButton getButton(final Image image, final Runnable runnable) {
		return makeButton(Suppliers.emptyString(), image, runnable);
	}

	public UniversalButton getButton(Supplier<String> textKey, Image image, final Runnable runnable) {
		return makeButton(textKey, image, runnable);
	}

	private UniversalButton makeButton(Supplier<String> textKey, Image image, final Runnable runnable) {
		TextButton textButton = makeLibGDXTextButton(textKey.get());
		makeBackgroundFlashing(textButton, skinManager.getTextButtonStyle(), skinManager.getFlashedTextButtonStyle());
		return new UniversalButton(textButton, textKey, image, runnable, inputStrategySwitcher, soundInteractListener);
	}

	/**
	 * Make a button in a particular style, these are generally exceptional buttons (in Quest Giver this includes the play button, scenario map pips, etc)
	 */
	protected UniversalButton makeButton(final String text, final Runnable runnable, TextButtonStyle textButtonStyle) {
		TextButton textButton = makeLibGDXTextButton(text, textButtonStyle);
		return new UniversalButton(textButton, null, runnable, inputStrategySwitcher, soundInteractListener);
	}

	private TextButton makeLibGDXTextButton(final String text, TextButtonStyle textButtonStyle) {
		return new TextButton(text, textButtonStyle);
	}
	private TextButton makeLibGDXTextButton(final String text) {
		return makeLibGDXTextButton(text, skinManager.getTextButtonStyle());
	}

	public UniversalSelectBox getSelectBox(Supplier<String> boxLabel, Collection<Supplier<String>> entries, Consumer<String> consumer) {
		TextButton textButton = makeLibGDXTextButton(boxLabel.get(), skinManager.getTextButtonStyle()); 
		makeBackgroundFlashing(textButton, skinManager.getTextButtonStyle(), skinManager.getFlashedTextButtonStyle());
		UniversalSelectBox keyboardSelectBox =  new UniversalSelectBox(entries, textButton, boxLabel, inputStrategySwitcher, soundInteractListener);
		keyboardSelectBox.setAction(consumer);
		return keyboardSelectBox;
	}

	public BaseDrawable getUIBorderedPatch() {
		return skinManager.getUINinePatch();
	}
	public BaseDrawable getConfirmationMenuBackground() {
		return skinManager.getConfirmationMenuBackground();
	}

	public UniversalSlider getSlider(Supplier<String> textKey, Consumer<Float> consumer) {
		UniversalButton textButton = getButton(textKey, Runnables.nullRunnable());
		return new UniversalSlider(textButton.getView(), textKey, skinManager.getSliderStyle(), consumer, inputStrategySwitcher, soundInteractListener);
	}

	public UniversalCheckbox getCheckbox(Supplier<String> uncheckedLabel, Supplier<String> checkedLabel, Consumer<Boolean> consumer) {
		UniversalButton textButton = getButton(Suppliers.emptyString(), Runnables.nullRunnable());
		return new UniversalCheckbox(textButton.getView(), uncheckedLabel, checkedLabel, consumer, skinManager.getCheckboxStyle(), inputStrategySwitcher, soundInteractListener);
	}

	public UniversalButton getSettingsButton(Consumer<Boolean> toggleOptionsScreenVisibility) {
		TextButton textButton = new TextButton("", skinManager.getSettingsButtonStyle()){
			@Override public String toString() { return "options button"; }}; 
			return new MouseOnlyButton(textButton, Suppliers.emptyString(),
					() -> toggleOptionsScreenVisibility.accept(!isPaused.get()),
					inputStrategySwitcher, soundInteractListener);
	}

	/**
	 * @param buttonText
	 * @return A quit button, with a default English text label if not otherwise to find
	 */
	public UniversalButton getQuitGameButton(Supplier<String> buttonText) {
		return getButton(buttonText, quitGameRunnable);
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

	public UniversalButton getQuitGameButtonWithWarning() {
		Runnable quitWithConfirmation = () -> {
			new ConfirmationMenu(
					"menu_warning", 
					QUIT_GAME_KEY, 
					quitGameRunnable::run);
		};
		return getButton(getQuitButtonString(), quitWithConfirmation);
	}

	protected void makeBackgroundFlashing(Button button, ButtonStyle mainButtonStyle, ButtonStyle flashedButtonStyle) {
		button.addListener(new ClickListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				if (UniversalInputStage.isHoverEvent(pointer) && button.isTouchable()) {
					button.setStyle(mainButtonStyle);
					if (shouldButtonFlash(button)) {
						button.clearActions();
						button.addAction(new InstantForeverAction(new InstantSequenceAction(
								getChangeButtonStyleAfterDelayAction(button, flashedButtonStyle),
								getChangeButtonStyleAfterDelayAction(button, mainButtonStyle)
								)));

					}
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				if (UniversalInputStage.isHoverEvent(pointer)) {
					button.clearActions();
					button.setStyle(mainButtonStyle);
				}
			}
		});
	}
	protected void makeSliderFlashing(UniversalSlider keyboardSlider, SliderStyle mainStyle, SliderStyle flashedStyle) {
		Button button = keyboardSlider.getView(); 
		button.addListener(new ClickListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				if (UniversalInputStage.isHoverEvent(pointer) && button.isTouchable()) {
					keyboardSlider.setSliderStyle(flashedStyle);
					if (shouldButtonFlash(button)) {
						button.clearActions();
						button.addAction(new InstantForeverAction(new InstantSequenceAction(
								getChangeSliderStyleAfterDelayAction(keyboardSlider, flashedStyle),
								getChangeSliderStyleAfterDelayAction(keyboardSlider, mainStyle)
								)));

					}
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				if (UniversalInputStage.isHoverEvent(pointer)) {
					button.clearActions();
					keyboardSlider.setSliderStyle(mainStyle);
				}
			}
		});
	}

	private boolean shouldButtonFlash(Button button) {
		return UniversalInputStage.isInTouchableBranch(button)
				&& !button.isDisabled()
				&& inputStrategySwitcher.shouldFlashButtons();
	}

	private Action getChangeButtonStyleAfterDelayAction(Button button, ButtonStyle buttonStyle) {
		RunnableAction change = Actions.run(() -> {
			if (shouldButtonFlash(button)) {
				button.setStyle(buttonStyle);
			}
		});
		DelayAction changeAfterDelay = Actions.delay(computeDelay());
		changeAfterDelay.setAction(change);

		return changeAfterDelay;
	}

	private Action getChangeSliderStyleAfterDelayAction(UniversalSlider keyboardSlider, SliderStyle sliderStyle) {
		RunnableAction change = Actions.run(() -> {
			if (shouldButtonFlash(keyboardSlider.getView())) {
				keyboardSlider.setSliderStyle(sliderStyle);
			}
		});
		DelayAction changeAfterDelay = Actions.delay(computeDelay());
		changeAfterDelay.setAction(change);

		return changeAfterDelay;
	}

	public WindowResizerSelectBox getWindowModeTextSelectBox() {
		Supplier<String> textSupplier = () -> TextSupplier.getLine("window_mode_label");
		TextButton textButton = makeLibGDXTextButton(textSupplier.get(), skinManager.getTextButtonStyle()); 
		makeBackgroundFlashing(textButton, skinManager.getTextButtonStyle(), skinManager.getFlashedTextButtonStyle());
		return new WindowResizerSelectBox(textButton, textSupplier, inputStrategySwitcher, soundInteractListener);
	}

	private float computeDelay() {
		return 1f/flashesPerSecondSupplier.get();
	}
}
