package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.zalaudiolibrary.sfx.SoundEffect;

/**
 * Our very own custom button class that works with keyboard input!
 * This is also the base class for other keyboard *buttons* such as checkboxes and sliders,
 * which allows them all to be put in a navigable menu together and treated the same
 */
public abstract class UniversalButton extends UniversalDoodad implements VisibleInputConsumer {

	private Runnable buttonRunnable;

	protected UniversalButton(Runnable buttonRunnable, InputStrategySwitcher inputStrategySwitcher, ButtonStyle buttonStyle, Consumer<SoundEffect> soundEffectConsumer) {
		super(buttonStyle, inputStrategySwitcher, soundEffectConsumer);
		this.buttonRunnable = buttonRunnable;
	}

	@Override
	protected void justPressed() {
		boolean isTouchable = getTouchable() != Touchable.disabled;
		if (!isDisabled() && isTouchable) {
			buttonRunnable.run();
			requestInteractSound();
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
	 * @param buttonRunnable the runnable for the button
	 */
	public void setButtonRunnable(Runnable buttonRunnable) {
		this.buttonRunnable = buttonRunnable;
	}

}
