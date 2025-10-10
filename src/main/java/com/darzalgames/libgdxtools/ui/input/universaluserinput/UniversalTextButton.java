package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

/**
 * A UniversalButton which you know for sure has a label
 */
public class UniversalTextButton extends UniversalButton {

	protected final UniversalLabel label;

	public UniversalTextButton(UniversalLabel label, Runnable buttonRunnable, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener, ButtonStyle buttonStyle) {
		super(buttonRunnable, inputStrategySwitcher, soundInteractListener, buttonStyle);
		this.label = label;
		label.setTouchable(Touchable.disabled);
		add(label);
	}

	@Override
	public void resizeUI() {
		label.resizeUI();
		getCell(label).pad(UserInterfaceSizer.getMinimumPercentage(0.0025f));
		pack();
		super.resizeUI();
	}

	@Override
	public boolean isBlank() {
		return label.isBlank();
	}

	@Override
	public void setAlignment(Alignment alignment) {
		label.setAlignment(alignment);
		getCell(label).align(alignment.getAlignment());
		align(alignment.getAlignment());
	}

	public String getText() {
		return label.storedText;
	}

	@Override
	public void colorOtherComponentsBasedOnFocus(Color color) {
		label.setColor(color);
	}

}
