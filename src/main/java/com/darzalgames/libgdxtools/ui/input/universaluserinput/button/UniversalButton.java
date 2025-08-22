package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UniversalDoodad;

/**
 * Our very own custom button class that works with keyboard input!
 * This is also the base class for other keyboard *buttons* such as checkboxes and sliders,
 * which allows them all to be put in a navigable menu together and treated the same
 */
public abstract class UniversalButton extends UniversalDoodad implements VisibleInputConsumer {

	private Runnable buttonRunnable;
	private boolean doesSoundOnInteract = true;
	private final Runnable soundInteractListener;

	protected UniversalButton(Runnable buttonRunnable, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener, ButtonStyle buttonStyle) {
		super(buttonStyle, true, inputStrategySwitcher);
		this.buttonRunnable = buttonRunnable;
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
	 * Set what to do when the button is pressed
	 * @param buttonRunnable
	 */
	public void setButtonRunnable(Runnable buttonRunnable) {
		this.buttonRunnable = buttonRunnable;
	}

	/**
	 * Set whether or not this button should make a sound when interacted with
	 * @param doesSoundOnInteract
	 */
	public void setDoesSoundOnInteract(boolean doesSoundOnInteract) {
		this.doesSoundOnInteract = doesSoundOnInteract;
	}

}
