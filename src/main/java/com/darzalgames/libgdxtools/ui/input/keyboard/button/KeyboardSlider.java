package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public class KeyboardSlider extends KeyboardButton {

	private final Slider slider;
	private float previousValue;

	public KeyboardSlider(TextButton textButton, SliderStyle sliderStyle, Consumer<Float> consumer, InputStrategyManager inputStrategyManager) {
		this(textButton, null, sliderStyle, consumer, inputStrategyManager);
	}

	public KeyboardSlider(TextButton textButton, Image image, SliderStyle sliderStyle, Consumer<Float> consumer, InputStrategyManager inputStrategyManager) {
		super(textButton, image, Runnables.nullRunnable(), inputStrategyManager);
		slider = new Slider(0, 1, 0.1f, false, sliderStyle);
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
				KeyboardSlider.this.setFocused(true);
				float currentValue = slider.getValue();
				if (currentValue != previousValue) { //The slider fires a ChangeEvent on touchUp (mouse mode) which we want to ignore
					requestInteractSound();
				}
				previousValue = currentValue;
			}
		});
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
