package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;

/**
 * A UniversalButton which you know for sure has a label
 */
public class UniversalTextButton extends UniversalButton {

	private final UniversalLabel label;

	public UniversalTextButton(UniversalLabel label, Runnable buttonRunnable, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener, TextButtonStyle textButtonStyle) {
		super(buttonRunnable, inputStrategySwitcher, soundInteractListener, textButtonStyle);
		this.label = label;
		add(label);
	}

	@Override
	public void resizeUI() {
		label.resizeUI();
	}

	@Override
	public boolean isBlank() {
		return label.isBlank();
	}

	@Override
	public void setAlignment(Alignment alignment) {
		label.setAlignment(alignment);
	}

	public String getText() {
		return label.textSupplier.get();
	}

}
