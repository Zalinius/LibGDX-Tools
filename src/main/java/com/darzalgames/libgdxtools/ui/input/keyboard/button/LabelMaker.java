package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.darzalcommon.functional.TriFunction;
import com.darzalgames.libgdxtools.MainGame;
import com.darzalgames.libgdxtools.i18n.TextSupplier;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantForeverAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantSequenceAction;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.keyboard.InputSensitiveLabel;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.stylemanager.StyleManager;
import com.darzalgames.libgdxtools.ui.input.keyboard.stage.KeyboardStage;

public class LabelMaker {

	private static Runnable quitGameRunnable;
	private static StyleManager styleManager;
	private static TriFunction<TextButton, Image, Runnable, KeyboardButton> privateKeyboardButtonConstructor;
	private static NinePatchDrawable UIBorderedNine;
	
	protected LabelMaker() {}
	
	public static void setPrivateKeyboardButtonConstructor(
			TriFunction<TextButton, Image, Runnable, KeyboardButton> privateKeyboardButtonConstructor) {
		LabelMaker.privateKeyboardButtonConstructor = privateKeyboardButtonConstructor;
	}

	protected static void initialize(StyleManager styleManager, NinePatchDrawable UIBorderedNine) {
		LabelMaker.styleManager = styleManager;
		LabelMaker.UIBorderedNine = UIBorderedNine;
		quitGameRunnable = Gdx.app::exit;
		KeyboardButton.setUpForLabelMaker();
	}

	public static Label getLabel(final String text) {
		return getLabel(text, styleManager.getDefaultLableStyle());
	}

	public static Label getFlavorTextLabel(final String text) {
		return getLabel(text, styleManager.getFlavorTextLableStyle());
	}

	public static Label getWarningLabel(final String text) {
		return getLabel(text, styleManager.getWarningLableStyle());
	}

	public static Label getLabelWithBackground(final String text) {
		return getLabel(text, styleManager.getLabelWithBackgroundStyle());
	}

	public static Label getInputSensitiveLabelWithBackground(final Supplier<String> textSupplier) {
		Label label = new InputSensitiveLabel(textSupplier, styleManager.getDefaultLableStyle());
		label.setWrap(true);
		return label;
	}

	protected static Label getLabel(final String text, LabelStyle labelStyle) {
		Label label = new Label(text, labelStyle);
		label.setWrap(true);
		return label;
	}

	public static KeyboardButton getListableLabel(final String text) {
		// a bit of hack so that a label-like button can be stored in a list of buttons but not be touchable
		TextButton textButton = new TextButton(text, styleManager.getSneakyLableButtonStyle());
		textButton.setName(text);
		KeyboardButton listableButton = new KeyboardButton(textButton, null, Runnables.nullRunnable());
		listableButton.setTouchable(Touchable.disabled);
		return listableButton;
	}

	public static KeyboardButton getSpacer() {
		KeyboardButton spacer = getListableLabel("");
		spacer.getView().setName("spacer");
		return spacer;
	}

	public static boolean isSpacer(KeyboardButton button) {
		return !button.getView().isTouchable() && button.getButtonText().isBlank();
	}


	public static KeyboardButton getButton(final String text, final Runnable runnable) {
		return makeButton(text, null, runnable);
	}
	
	public static KeyboardButton getButton(final Image image, final Runnable runnable) {
		return makeButton("", image, runnable);
	}
	
	public static KeyboardButton getButton(final String text, Image image, final Runnable runnable) {
		return makeButton(text, image, runnable);
	}
	
	private static KeyboardButton makeButton(final String text, Image image, final Runnable runnable) {
		TextButton textButton = makeLibGDXTextButton(text);
		makeBackgroundFlashing(textButton, styleManager.getTextButtonStyle(), styleManager.getFlashedTextButtonStyle());
		return new KeyboardButton(textButton, image, runnable);
	}
	
	/**
	 * Make a button in a particular style, these are generally exceptional buttons (in Quest Giver this includes the play button, scenario map pips, etc)
	 */
	protected static KeyboardButton makeButton(final String text, final Runnable runnable, TextButtonStyle textButtonStyle) {
		TextButton textButton = new TextButton(text, textButtonStyle);
		return new KeyboardButton(textButton, null, runnable);
	}

	/**
	 * Make a LIBGDX textbutton, only to be used inside nested buttons (e.g. checkboxes & sliders)
	 * @param text
	 * @return
	 */
	private static TextButton makeLibGDXTextButton(final String text) {
		return new TextButton(text, styleManager.getTextButtonStyle());
	}


	public static KeyboardButton getBlankButton(Drawable closed, Drawable hovered, Drawable open, final Runnable runnable) {
		TextButtonStyle textButtonStyle = new TextButtonStyle(closed, hovered, open, new BitmapFont());
		textButtonStyle.over = hovered;
		TextButton textButton = new TextButton("", textButtonStyle);
		return new KeyboardButton(textButton, runnable);
	}

	/*
	 * If ever this isn't behaving as you'd expect, make sure your actor is in a group the size of the
	 * screen or straight on the stage, not nested in some container that's smaller than the screen
	 * since it will be drawn in the parent's coordinate system.
	 */
	public static void makeActorCentered(final Actor actor) {
		actor.setPosition(MainGame.getWidth() / 2f - actor.getWidth() / 2f,
				MainGame.getHeight() / 2f - actor.getHeight() / 2f);
	}

	public static KeyboardSelectBox getSelectBox(String boxLabel, Collection<String> entries, Consumer<String> consumer) {
		TextButton textButton = new TextButton(boxLabel + ":  ", styleManager.getTextButtonStyle()); 
		makeBackgroundFlashing(textButton, styleManager.getTextButtonStyle(), styleManager.getFlashedTextButtonStyle());
		return new KeyboardSelectBox(entries, textButton, consumer);
	}

	public static NinePatchDrawable getUIBorderedNine() {
		return UIBorderedNine;
	}

	public static KeyboardSlider getSlider(String sliderLabel, Consumer<Float> consumer) {
		KeyboardButton textButton = getButton(sliderLabel, Runnables.nullRunnable());
		return new KeyboardSlider(textButton.getView(), styleManager.getSliderStyle(), consumer);
	}

	public static KeyboardCheckbox getCheckbox(String uncheckedLabel, String checkedLabel, Consumer<Boolean> consumer) {
		KeyboardButton textButton = getButton("", Runnables.nullRunnable());
		return new KeyboardCheckbox(textButton.getView(), uncheckedLabel, checkedLabel, consumer, styleManager.getCheckboxStyle());
	}

	public static KeyboardButton getInGamesSettingsButton(Runnable onclick) {
		TextButton textButton = new TextButton("", styleManager.getSettingsButtonStyle()); 
		return new MouseOnlyButton(textButton, onclick);
	}

	private static final String QUIT_GAME_KEY = "quit_game";

	public static KeyboardButton getQuitGameButton() {
		return getButton(TextSupplier.getLine(QUIT_GAME_KEY), quitGameRunnable);
	}

	public static KeyboardButton getQuitGameButtonWithWarning(Runnable runnable) {
		Runnable quitWithConfirmation = () -> {
			runnable.run();
			new ConfirmationMenu("menu_warning", 
					QUIT_GAME_KEY, 
					quitGameRunnable::run);
		};
		return getButton(TextSupplier.getLine(QUIT_GAME_KEY), quitWithConfirmation);
	}

	protected static void makeBackgroundFlashing(Button button, ButtonStyle mainButtonStyle, ButtonStyle flashedButtonStyle) {
		button.addListener(new ClickListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				float flashTime = 1f / 2.5f;
				if (KeyboardStage.isHoverEvent(pointer) && button.isTouchable()) {
					button.setStyle(mainButtonStyle);
					if (shouldButtonFlash(button)) {
						button.clearActions();
						Action flashAfterDelay = getChangeStyleAfterDelayAction(button, flashedButtonStyle, flashTime);
						Action showAfterDelay = getChangeStyleAfterDelayAction(button, mainButtonStyle, flashTime);
						button.addAction(new InstantForeverAction(new InstantSequenceAction(
								flashAfterDelay,
								showAfterDelay
								)));

					}
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				if (KeyboardStage.isHoverEvent(pointer)) {
					button.clearActions();
				}
			}
		});
	}

	private static boolean shouldButtonFlash(Button button) {
		return KeyboardStage.isInTouchableBranch(button)
				&& !button.isDisabled()
				&& MainGame.getInputStrategyManager().shouldFlashButtons();
	}

	private static Action getChangeStyleAfterDelayAction(Button button, ButtonStyle buttonStyle, float delay) {
		RunnableAction change = Actions.run(() -> {
			if (shouldButtonFlash(button)) {
				button.setStyle(buttonStyle);
			}
		});
		DelayAction changeAfterDelay = Actions.delay(delay);
		changeAfterDelay.setAction(change);

		return changeAfterDelay;
	}
}
