package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class UniversalSlider extends UniversalButton {

	private final Slider slider;
	private float previousValue;

	public UniversalSlider(TextButton textButton, SliderStyle sliderStyle, Consumer<Float> consumer, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		this(textButton, null, sliderStyle, consumer, inputStrategySwitcher, soundInteractListener);
	}

	public UniversalSlider(TextButton textButton, Image image, SliderStyle sliderStyle, Consumer<Float> consumer, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(textButton, image, Runnables.nullRunnable(), inputStrategySwitcher, soundInteractListener);
		slider = new Slider(0, 1, 0.1f, false, sliderStyle) {
			@Override
			public float getPrefWidth() {
				return Math.max(super.getPrefWidth(), this.getBackgroundDrawable().getMinWidth());
			}
		};
		slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				consumer.accept(slider.getValue());
			}
		});

		textButton.setWidth(textButton.getWidth() + slider.getPrefWidth());
		textButton.add(slider);
		textButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				UniversalSlider.this.setFocused(true);
				float currentValue = slider.getValue();
				if (currentValue != previousValue) { //The slider fires a ChangeEvent on touchUp (mouse mode) which we want to ignore
					requestInteractSound();
				}
				previousValue = currentValue;
			}
		});
	}
	@Override
	public void setFocused(boolean isFocused) {
		super.setFocused(isFocused);
		InputListener inputListener = (InputListener) slider.getListeners().get(0);
		if (isFocused) {
			inputListener.enter(null, 0, 0, -1, slider);	
		} else {
			inputListener.exit(null, 0, 0, -1, slider);
		}
		
	}
	public void setSliderStyle(SliderStyle sliderStyle) {
		slider.setStyle(sliderStyle);
	}

	@Override
	public void consumeKeyInput(Input input) {
		if (input == Input.LEFT || input == Input.RIGHT) {
			float value = slider.getValue() + (input == Input.LEFT ? -1 : 1)*slider.getStepSize();
			slider.setValue(value);
		}
	}

	/**
	 * Set the position of the slider, optionally triggering the sound effect for doing so
	 * @param newPosition A value within the bar's min/max range, it's clamped regardless
	 * @param withSoundEffect Whether or not to play the interaction sound
	 */
	public void setSliderPosition(float newPosition, boolean withSoundEffect) {
		previousValue = slider.getValue();
		this.setDoesSoundOnInteract(withSoundEffect);
		slider.setValue(newPosition);
		this.setDoesSoundOnInteract(true);
	}

}
