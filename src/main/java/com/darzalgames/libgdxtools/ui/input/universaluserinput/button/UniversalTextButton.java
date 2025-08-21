package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;

/**
 * A UniversalButton which you know for sure has a label
 */
public class UniversalTextButton extends UniversalButton {

	protected final UniversalLabel label;

	public UniversalTextButton(UniversalLabel label, Runnable buttonRunnable, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener, TextButtonStyle textButtonStyle) {
		super(buttonRunnable, inputStrategySwitcher, soundInteractListener, textButtonStyle);
		this.label = label;
		label.setTouchable(Touchable.disabled);
		add(label);
	}

	@Override
	public void resizeUI() {
		label.resizeUI();
		getCell(label).pad(UserInterfaceSizer.getMinimumPercentage(0.0025f));
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
		return label.textSupplier.get();
	}

}
