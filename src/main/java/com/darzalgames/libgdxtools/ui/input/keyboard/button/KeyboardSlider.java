package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.darzalgames.libgdxtools.ui.input.Input;

public class KeyboardSlider extends KeyboardButton {

	private final Slider slider;

	public KeyboardSlider(TextButton textButton, SliderStyle sliderStyle, Consumer<Float> consumer) {
		super(textButton);
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
			}
		});
	}

	
	@Override
	public void consumeKeyInput(Input input) {
		if (input == Input.LEFT || input == Input.RIGHT) {
			float value = slider.getValue() + (input == Input.LEFT ? -1 : 1)*slider.getStepSize();
			slider.setValue(value);
		}
	}
	
	public void setSliderPosition(float newPosition) {
		slider.setValue(newPosition);
	}

}
