package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darzalgames.darzalcommon.functional.Runnables;
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

	public Label getLabel(final String text) {
		return getLabel(text, skinManager.getDefaultLableStyle());
	}

	public Label getFlavorTextLabel(final String text) {
		return getLabel(text, skinManager.getFlavorTextLableStyle());
	}

	public Label getWarningLabel(final String text) {
		return getLabel(text, skinManager.getWarningLableStyle());
	}

	public Label getLabelWithBackground(final String text) {
		return getLabel(text, skinManager.getLabelWithBackgroundStyle());
	}

	/**
	 * Makes a label which changes its text based on the current input mode
	 * @param textSupplier
	 * @return
	 */
	public Label getInputSensitiveLabelWithBackground(final Supplier<String> textSupplier) {
		Label label = new InputSensitiveLabel(textSupplier, skinManager.getLabelWithBackgroundStyle(), inputStrategySwitcher) {
			@Override
			public void act(float delta) {
				super.act(delta);
				this.setStyle(this.getStyle());
				this.invalidateHierarchy();
				this.layout();
			}
		};
		label.setWrap(true);
		return label;
	}

	protected Label getLabel(final String text, LabelStyle labelStyle) {
		Label label = new Label(text, labelStyle) {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				this.setStyle(this.getStyle());
				this.invalidateHierarchy();
				this.layout();
				super.draw(batch, parentAlpha);
			}
		};
		label.setWrap(true);
		return label;
	}

	/**
	 * Makes a label which can be listed among other buttons, but isn't interactable
	 * @param text
	 * @return
	 */
	public UniversalButton getListableLabel(final String text) {
		// a bit of hack so that a label-like button can be stored in a list of buttons but not be interactable
		TextButton textButton = makeLibGDXTextButton(text, skinManager.getSneakyLableButtonStyle());
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
	public UniversalButton getSpacer() {
		UniversalButton spacer = getListableLabel("");
		spacer.getView().setName("spacer");
		return spacer;
	}

	public boolean isSpacer(UniversalButton button) {
		return button.getView().isDisabled() && button.isBlank();
	}


	public UniversalButton getButton(final String text, final Runnable runnable) {
		return makeButton(text, null, runnable);
	}

	public UniversalButton getButton(final Image image, final Runnable runnable) {
		return makeButton("", image, runnable);
	}

	public UniversalButton getButton(final String text, Image image, final Runnable runnable) {
		return makeButton(text, image, runnable);
	}

	private UniversalButton makeButton(final String text, Image image, final Runnable runnable) {
		TextButton textButton = makeLibGDXTextButton(text);
		makeBackgroundFlashing(textButton, skinManager.getTextButtonStyle(), skinManager.getFlashedTextButtonStyle());
		return new UniversalButton(textButton, image, runnable, inputStrategySwitcher, soundInteractListener);
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

	public UniversalSelectBox getSelectBox(String boxLabel, Collection<String> entries, Consumer<String> consumer) {
		TextButton textButton = makeLibGDXTextButton(boxLabel + ":  ", skinManager.getTextButtonStyle()); 
		makeBackgroundFlashing(textButton, skinManager.getTextButtonStyle(), skinManager.getFlashedTextButtonStyle());
		UniversalSelectBox keyboardSelectBox =  new UniversalSelectBox(entries, textButton, inputStrategySwitcher, soundInteractListener);
		keyboardSelectBox.setAction(consumer);
		return keyboardSelectBox;
	}

	public BaseDrawable getUIBorderedNine() {
		return skinManager.getUINinePatch();
	}

	public UniversalSlider getSlider(String sliderLabel, Consumer<Float> consumer) {
		UniversalButton textButton = getButton(sliderLabel, Runnables.nullRunnable());
		return new UniversalSlider(textButton.getView(), skinManager.getSliderStyle(), consumer, inputStrategySwitcher, soundInteractListener);
	}

	public UniversalCheckbox getCheckbox(String uncheckedLabel, String checkedLabel, Consumer<Boolean> consumer) {
		UniversalButton textButton = getButton("", Runnables.nullRunnable());
		return new UniversalCheckbox(textButton.getView(), uncheckedLabel, checkedLabel, consumer, skinManager.getCheckboxStyle(), inputStrategySwitcher, soundInteractListener);
	}

	public UniversalButton getSettingsButton(Consumer<Boolean> togglePauseScreenVisibility) {
		TextButton textButton = new TextButton("", skinManager.getSettingsButtonStyle()){
			@Override public String toString() { return "pause button"; }}; 
			return new MouseOnlyButton(textButton, 
					() -> togglePauseScreenVisibility.accept(!isPaused.get()),
					inputStrategySwitcher, soundInteractListener);
	}

	/**
	 * @param buttonText
	 * @return A quit button, with a default English text label if not otherwise to find
	 */
	public UniversalButton getQuitGameButton(String buttonText) {
		return getButton(buttonText, quitGameRunnable);
	}

	/**
	 * @return A quit button, with a default English text label if not otherwise to find
	 */
	public UniversalButton getQuitGameButton() {
		return getQuitGameButton(getQuitButtonString());
	}

	private String getQuitButtonString() {
		String text;
		try {
			text = TextSupplier.getLine(QUIT_GAME_KEY);
		} catch (NullPointerException e) {
			// if there's no internationalization bundle
			text = "Quit";
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
		TextButton textButton = makeLibGDXTextButton(TextSupplier.getLine("window_mode_label") + ":  ", skinManager.getTextButtonStyle()); 
		makeBackgroundFlashing(textButton, skinManager.getTextButtonStyle(), skinManager.getFlashedTextButtonStyle());
		return new WindowResizerSelectBox(textButton, inputStrategySwitcher, soundInteractListener);

	}

	private float computeDelay() {
		return 1f/flashesPerSecondSupplier.get();
	}
}
