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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerSelectBox;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantForeverAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantSequenceAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStage;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager.SkinManager;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.TypingLabel;

/**
 * The ONLY place where one should be making UI elements (buttons, labels, etc)
 */
public class UserInterfaceFactory {

	protected static Runnable quitGameRunnable;
	private static SkinManager skinManager;
	protected static InputStrategySwitcher inputStrategySwitcher;
	private static Supplier<Float> flashesPerSecondSupplier;
	protected static Runnable soundInteractListener;
	private static Supplier<Boolean> isPaused;

	protected UserInterfaceFactory() {}

	public static void initialize(SkinManager skinManager, InputStrategySwitcher inputStrategySwitcher, Supplier<Float> flashesPerSecondSupplier, Runnable soundInteractListener,
			Supplier<Boolean> isPaused) {
		UserInterfaceFactory.skinManager = skinManager;
		UserInterfaceFactory.inputStrategySwitcher = inputStrategySwitcher;
		quitGameRunnable = Gdx.app::exit;
		UserInterfaceFactory.flashesPerSecondSupplier = flashesPerSecondSupplier;
		UserInterfaceFactory.soundInteractListener = soundInteractListener;
		UserInterfaceFactory.isPaused = isPaused;
	}

	public static Label getLabel(final String text) {
		return getLabel(text, skinManager.getDefaultLableStyle());
	}

	public static Label getFlavorTextLabel(final String text) {
		return getLabel(text, skinManager.getFlavorTextLableStyle());
	}

	public static Label getWarningLabel(final String text) {
		return getLabel(text, skinManager.getWarningLableStyle());
	}

	public static Label getLabelWithBackground(final String text) {
		return getLabel(text, skinManager.getLabelWithBackgroundStyle());
	}


	public static TypingLabel getTypingLabel(final String text) {
		Font font = new Font(skinManager.getDefaultLableStyle().font, Font.DistanceFieldType.STANDARD, 0, 0, 0, 0, false);
		TypingLabel label = new TypingLabel(text, font);
		label.setWrap(true);
		
		return label;
	}

	/**
	 * Makes a label which changes its text based on the current input mode
	 * @param textSupplier
	 * @return
	 */
	public static Label getInputSensitiveLabelWithBackground(final Supplier<String> textSupplier) {
		Label label = new InputSensitiveLabel(textSupplier, skinManager.getLabelWithBackgroundStyle(), inputStrategySwitcher);
		label.setWrap(true);
		return label;
	}
	public static Label getFlashingArrowLabel() {
		return getFlashingTextLabel(">");
	}
	public static Label getFlashingContinueArrowLabel() {
		return getFlashingTextLabel("\\/"); // TODO This arrow is stupid
	}

	private static Label getFlashingTextLabel(final String text) {
		Label textLabel = getLabel(text);
		textLabel.setWrap(false);
		RunnableActionBest flash = new RunnableActionBest(() -> textLabel.setVisible(false));
		RunnableActionBest show = new RunnableActionBest(() -> textLabel.setVisible(true));
		float flashTime = 0.9f; // TODO synchronize with music
		DelayAction flashAfterDelay = Actions.delay(flashTime);
		flashAfterDelay.setAction(flash);
		DelayAction showAfterDelay = Actions.delay(flashTime);
		showAfterDelay.setAction(show);
		textLabel.addAction(new InstantForeverAction(new InstantSequenceAction(
				flashAfterDelay,
				showAfterDelay
				)));
		return textLabel;
	}

	protected static Label getLabel(final String text, LabelStyle labelStyle) {
		Label label = new Label(text, labelStyle);
		label.setWrap(true);
		return label;
	}

	/**
	 * Makes a label which can be listed among other buttons, but isn't interactable
	 * @param text
	 * @return
	 */
	public static UniversalButton getListableLabel(final String text) {
		// a bit of hack so that a label-like button can be stored in a list of buttons but not be interactable
		TextButton textButton = new TextButton(text, skinManager.getSneakyLableButtonStyle());
		textButton.setName(text);
		UniversalButton listableButton = new UniversalButton(textButton, null, Runnables.nullRunnable(), inputStrategySwitcher, soundInteractListener);
		listableButton.setDisabled(true);
		return listableButton;
	}

	/**
	 * Makes a spacer which can be listed among other buttons, but isn't interactable and which will
	 * expand out to fill any available space in the menu
	 * @return
	 */
	public static UniversalButton getSpacer() {
		UniversalButton spacer = getListableLabel("");
		spacer.getView().setName("spacer");
		return spacer;
	}

	public static boolean isSpacer(UniversalButton button) {
		return button.getView().isDisabled() && button.isBlank();
	}

	public static UniversalButton getFastForwardButton(Runnable runnable) {
		return getButton(">>", runnable);
	}

	public static UniversalButton getButton(final String text, final Runnable runnable) {
		return makeButton(text, null, runnable);
	}

	public static UniversalButton getButton(final Image image, final Runnable runnable) {
		return makeButton("", image, runnable);
	}

	public static UniversalButton getButton(final String text, Image image, final Runnable runnable) {
		return makeButton(text, image, runnable);
	}

	private static UniversalButton makeButton(final String text, Image image, final Runnable runnable) {
		TextButton textButton = makeLibGDXTextButton(text);
		makeBackgroundFlashing(textButton, skinManager.getTextButtonStyle(), skinManager.getFlashedTextButtonStyle());
		return new UniversalButton(textButton, image, runnable, inputStrategySwitcher, soundInteractListener);
	}

	/**
	 * Make a button in a particular style, these are generally exceptional buttons (in Quest Giver this includes the play button, scenario map pips, etc)
	 */
	protected static UniversalButton makeButton(final String text, final Runnable runnable, TextButtonStyle textButtonStyle) {
		TextButton textButton = new TextButton(text, textButtonStyle);
		return new UniversalButton(textButton, null, runnable, inputStrategySwitcher, soundInteractListener);
	}

	/**
	 * Make a LIBGDX textbutton, only to be used inside nested buttons (e.g. checkboxes and sliders)
	 * @param text
	 * @return
	 */
	private static TextButton makeLibGDXTextButton(final String text) {
		return new TextButton(text, skinManager.getTextButtonStyle());
	}

	/*
	 * If ever this isn't behaving as you'd expect, make sure your actor is in a group the size of the
	 * screen or straight on the stage, not nested in some container that's smaller than the screen
	 * since it will be drawn in the parent's coordinate system.
	 */
	public static void makeActorCentered(final Actor actor) {
		actor.setPosition(GameInfo.getWidth() / 2f - actor.getWidth() / 2f,
				GameInfo.getHeight() / 2f - actor.getHeight() / 2f);
	}

	public static UniversalSelectBox getSelectBox(String boxLabel, Collection<String> entries, Consumer<String> consumer) {
		TextButton textButton = new TextButton(boxLabel + ":  ", skinManager.getTextButtonStyle()); 
		makeBackgroundFlashing(textButton, skinManager.getTextButtonStyle(), skinManager.getFlashedTextButtonStyle());
		UniversalSelectBox keyboardSelectBox =  new UniversalSelectBox(entries, textButton, inputStrategySwitcher, soundInteractListener);
		keyboardSelectBox.setAction(consumer);
		return keyboardSelectBox;
	}

	public static NinePatchDrawable getUIBorderedNine() {
		return skinManager.getUINinePatch();
	}

	public static UniversalSlider getSlider(String sliderLabel, Consumer<Float> consumer) {
		UniversalButton textButton = getButton(sliderLabel, Runnables.nullRunnable());
		return new UniversalSlider(textButton.getView(), skinManager.getSliderStyle(), consumer, inputStrategySwitcher, soundInteractListener);
	}

	public static UniversalCheckbox getCheckbox(String uncheckedLabel, String checkedLabel, Consumer<Boolean> consumer) {
		UniversalButton textButton = getButton("", Runnables.nullRunnable());
		return new UniversalCheckbox(textButton.getView(), uncheckedLabel, checkedLabel, consumer, skinManager.getCheckboxStyle(), inputStrategySwitcher, soundInteractListener);
	}

	public static UniversalButton getSettingsButton(Consumer<Boolean> togglePauseScreenVisibility) {
		TextButton textButton = new TextButton("", skinManager.getSettingsButtonStyle()){
			@Override public String toString() { return "pause button"; }}; 
		return new MouseOnlyButton(textButton, 
				() -> togglePauseScreenVisibility.accept(!isPaused.get()),
				inputStrategySwitcher, soundInteractListener);
	}

	private static final String QUIT_GAME_KEY = "quit_game";

	/**
	 * @param buttonText
	 * @return A quit button, with a default English text label if not otherwise to find
	 */
	public static UniversalButton getQuitGameButton(String buttonText) {
		return getButton(buttonText, quitGameRunnable);
	}

	/**
	 * @return A quit button, with a default English text label if not otherwise to find
	 */
	public static UniversalButton getQuitGameButton() {
		return getQuitGameButton(getQuitButtonString());
	}

	private static String getQuitButtonString() {
		String text;
		try {
			text = TextSupplier.getLine(QUIT_GAME_KEY);
		} catch (NullPointerException e) {
			// if there's no internationalization bundle
			text = "Quit";
		}
		return text;
	}

	public static UniversalButton getQuitGameButtonWithWarning() {
		Runnable quitWithConfirmation = () -> {
			new ConfirmationMenu(
					"menu_warning", 
					QUIT_GAME_KEY, 
					quitGameRunnable::run);
		};
		return getButton(getQuitButtonString(), quitWithConfirmation);
	}

	protected static void makeBackgroundFlashing(Button button, ButtonStyle mainButtonStyle, ButtonStyle flashedButtonStyle) {
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
	protected static void makeSliderFlashing(UniversalSlider keyboardSlider, SliderStyle mainStyle, SliderStyle flashedStyle) {
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

	private static boolean shouldButtonFlash(Button button) {
		return UniversalInputStage.isInTouchableBranch(button)
				&& !button.isDisabled()
				&& inputStrategySwitcher.shouldFlashButtons();
	}

	private static Action getChangeButtonStyleAfterDelayAction(Button button, ButtonStyle buttonStyle) {
		RunnableAction change = Actions.run(() -> {
			if (shouldButtonFlash(button)) {
				button.setStyle(buttonStyle);
			}
		});
		DelayAction changeAfterDelay = Actions.delay(computeDelay());
		changeAfterDelay.setAction(change);

		return changeAfterDelay;
	}

	private static Action getChangeSliderStyleAfterDelayAction(UniversalSlider keyboardSlider, SliderStyle sliderStyle) {
		RunnableAction change = Actions.run(() -> {
			if (shouldButtonFlash(keyboardSlider.getView())) {
				keyboardSlider.setSliderStyle(sliderStyle);
			}
		});
		DelayAction changeAfterDelay = Actions.delay(computeDelay());
		changeAfterDelay.setAction(change);

		return changeAfterDelay;
	}

	public static WindowResizerSelectBox getWindowModeTextSelectBox() {
		Supplier<TextButton> supplier = () -> {
			TextButton textButton = new TextButton(TextSupplier.getLine("window_mode_label") + ":  ", skinManager.getTextButtonStyle()); 
			makeBackgroundFlashing(textButton, skinManager.getTextButtonStyle(), skinManager.getFlashedTextButtonStyle());
			return textButton;
		};
		return new WindowResizerSelectBox(supplier, inputStrategySwitcher);
	}
	
	private static float computeDelay() {
		return 1f/flashesPerSecondSupplier.get();
	}
}
