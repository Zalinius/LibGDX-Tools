package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputObserver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class InputSensitiveLabel extends Label implements InputObserver {
	
	private final Supplier<String> labelTextSupplier;
	private final InputStrategySwitcher inputStrategySwitcher;

	/**
	 * Creates a label that changes it's text when the input strategy changes
	 * @param labelTextSupplier supplies a string for what the label should say based on the current input method (using info from the inputStrategySwitcher, perhaps)
	 * @param style
	 * @param inputStrategySwitcher
	 */
	protected InputSensitiveLabel(Supplier<String> labelTextSupplier, LabelStyle style, InputStrategySwitcher inputStrategySwitcher) {
		super(labelTextSupplier.get(), style);
		this.labelTextSupplier = labelTextSupplier;
		this.inputStrategySwitcher = inputStrategySwitcher;
		inputStrategySwitcher.register(this);
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategySwitcher) {
		this.setText(labelTextSupplier.get());
	}
	
	@Override
	public boolean remove() {
		inputStrategySwitcher.unregister(this);
		return super.remove();
	}
	
	@Override
	public void clear() {
		inputStrategySwitcher.unregister(this);
		super.clear();
	}
	
	@Override
	protected void setParent(Group parent) {
		if (parent == null) {
			inputStrategySwitcher.unregister(this);
		}
		super.setParent(parent);
	}

	@Override
	public boolean shouldBeUnregistered() {
		return this.getStage() == null;
	}

}
