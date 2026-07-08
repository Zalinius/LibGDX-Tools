package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.function.Consumer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.zalaudiolibrary.sfx.SoundEffect;

/**
 * A UniversalButton which you know for sure has a label
 */
public class UniversalTextButton extends UniversalButton {

	protected final UniversalLabel label;

	public UniversalTextButton(UniversalLabel label, Runnable buttonRunnable, InputStrategySwitcher inputStrategySwitcher, ButtonStyle buttonStyle,
			Consumer<SoundEffect> soundEffectConsumer, SoundEffect soundEffect, ControlsGlyph controlsGlyph) {
		super(buttonRunnable, inputStrategySwitcher, buttonStyle, soundEffectConsumer, soundEffect, controlsGlyph);
		this.label = label;
		label.setTouchable(Touchable.disabled);
		doodadContents.add(label).growX();
	}

	@Override
	public void resizeUI() {
		label.resizeUI();
		doodadContents.getCell(label).pad(UserInterfaceSizer.getMinimumPercentage(0.0015f));
		doodadContents.invalidateHierarchy();
		if (label.wrap) {
			setHeight(label.getHeight());
		} else {
			super.resizeUI();
		}
	}

	@Override
	public boolean isBlank() {
		return label.isBlank();
	}

	@Override
	public void setAlignment(Alignment alignment) {
		label.setAlignment(alignment);
		doodadContents.getCell(label).align(alignment.getAlignment());
		doodadContents.align(alignment.getAlignment());
	}

	public String getText() {
		return label.storedText;
	}

	@Override
	public void colorOtherComponentsBasedOnFocus(Color color) {
		label.setColor(color);
	}

}
