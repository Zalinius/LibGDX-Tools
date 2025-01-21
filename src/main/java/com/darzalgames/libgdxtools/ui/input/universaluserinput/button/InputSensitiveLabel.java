package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.inputpriority.InputObserver;

public class InputSensitiveLabel extends Label implements InputObserver {
	
	private final Supplier<String> labelTextSupplier;
	private final InputStrategySwitcher inputStrategyManager;

	/**
	 * Creates a label that changes it's text when the input strategy changes
	 * @param labelTextSupplier supplies a string for what the label should say based on the current input method (using info from the inputStrategyManager, perhaps)
	 * @param style
	 * @param inputStrategyManager
	 */
	protected InputSensitiveLabel(Supplier<String> labelTextSupplier, LabelStyle style, InputStrategySwitcher inputStrategyManager) {
		super(labelTextSupplier.get(), style);
		this.labelTextSupplier = labelTextSupplier;
		this.inputStrategyManager = inputStrategyManager;
		inputStrategyManager.register(this);
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategyManager) {
		this.setText(labelTextSupplier.get());
	}
	
	@Override
	public boolean remove() {
		inputStrategyManager.unregister(this);
		return super.remove();
	}
	
	@Override
	public void clear() {
		inputStrategyManager.unregister(this);
		super.clear();
	}
	
	@Override
	protected void setParent(Group parent) {
		if (parent == null) {
			inputStrategyManager.unregister(this);
		}
		super.setParent(parent);
	}

	@Override
	public boolean shouldBeUnregistered() {
		return this.getStage() == null;
	}

}
