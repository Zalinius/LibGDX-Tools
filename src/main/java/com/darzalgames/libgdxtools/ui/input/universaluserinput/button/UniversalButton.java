package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Pools;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UniversalDoodad;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;

/**
 * Our very own custom button class that works with keyboard input!
 * This is also the base class for other keyboard *buttons* such as checkboxes and sliders,
 * which allows them all to be put in a navigable menu together and treated the same
 */
public abstract class UniversalButton extends UniversalDoodad implements VisibleInputConsumer {

	// TODO use enter and exit events to handle styling
	// TODO homebrew my own system for toggling or checked and focused
	// TODO Some sort of factory to make buttons with different components?


	private Runnable buttonRunnable;
	private boolean doesSoundOnInteract = true;
	private final InputStrategySwitcher inputStrategySwitcher;
	private final Runnable soundInteractListener;

	protected UniversalButton(Runnable buttonRunnable, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener, TextButtonStyle textButtonStyle) {
		super(textButtonStyle, true);
		this.buttonRunnable = buttonRunnable;
		this.inputStrategySwitcher = inputStrategySwitcher;
		this.soundInteractListener = soundInteractListener;
	}

	@Override
	protected void justPressed() {
		boolean isTouchable = getTouchable() != Touchable.disabled;
		if (isAClickableDoodad() && !isDisabled() && isTouchable) {
			buttonRunnable.run();
			setFocused(false);
			requestInteractSound();
		}
	}

	/**
	 * This will play the interaction sound immediately if this button has {@link UniversalButton#doesSoundOnInteract} set to true (generally the default)
	 */
	protected void requestInteractSound() {
		if (doesSoundOnInteract) {
			soundInteractListener.run();
		}
	}

	@Override
	public void consumeKeyInput(Input input) {
		if (input == Input.ACCEPT) {
			justPressed();
		}
	}

	/**
	 * Sets this button un/focused, generating a mimicked LibGDX mouse enter/exit event
	 * @param isFocused
	 */
	@Override
	public void setFocused(boolean isFocused) {
		InputEvent event = Pools.obtain(InputEvent.class);
		if (!isFocused) {
			event.setType(InputEvent.Type.exit);
		}
		else if (inputStrategySwitcher.shouldFlashButtons()) {
			event.setType(InputEvent.Type.enter);
		}
		else {
			event.setType(null); // Since the events are pooled I think they can come with a type?! (the type of the last event it was used for?)
		}

		if (event.getType() != null) {
			event.setStage(getStage());
			Vector2 localToStageCoordinates = localToStageCoordinates(new Vector2(0, 0));
			event.setStageX(localToStageCoordinates.x);
			event.setStageY(localToStageCoordinates.y);
			event.setPointer(-1);
			fire(event);
			Pools.free(event);
		}
	}

	//	/**
	//	 * @return Whether or not the button is blank
	//	 */
	//	@Override
	//	public boolean isBlank() {
	//		return textSupplier.get().isBlank() && image == null;
	//	}
	//
	//	/**
	//	 * Useful for trying to navigate to a particular button in a menu based on its text
	//	 * (e.g. defaulting to the current setting in a drop-down menu via string matching)
	//	 * @param value
	//	 * @return Whether or not this button has text that matches the supplied value
	//	 */
	//	public boolean doesTextMatch(String value) {
	//		return textSupplier.get().equalsIgnoreCase(value);
	//	}

	/**
	 * Set what to do when the button is pressed
	 * @param buttonRunnable
	 */
	public void setButtonRunnable(Runnable buttonRunnable) {
		this.buttonRunnable = buttonRunnable;
	}



	/**
	 * Update both the button's text and image in one go
	 * @param textSupplier
	 * @param image
	 */
	//	public void updateLabels(Supplier<String> textSupplier, final Image image) {
	//		this.textSupplier = textSupplier;
	//		this.image.setDrawable(image.getDrawable());
	//	}

	/**
	 * Set whether or not this button should make a sound when interacted with
	 * @param doesSoundOnInteract
	 */
	public void setDoesSoundOnInteract(boolean doesSoundOnInteract) {
		this.doesSoundOnInteract = doesSoundOnInteract;
	}

	@Override
	public void gainFocus() {
		setFocused(true);
	}

	@Override
	public void loseFocus() {
		setFocused(false);
	}

	@Override
	public void focusCurrent() {
		setFocused(true);
	}

	@Override
	public void clearSelected() {
		setFocused(false);
	}

	@Override
	public void selectDefault() { /*A basic button doesn't have any nested components to select*/ }

	@Override
	public void resizeUI() {
		setStyle(getStyle());
		setSize(getPrefWidth(), getPrefHeight());
	}

}
