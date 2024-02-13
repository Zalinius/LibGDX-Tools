package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.darzalgames.libgdxtools.ui.input.InputObserver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public class InputSensitiveLabel extends Label implements InputObserver {
	
	private final Supplier<String> labelTextSupplier;
	private final InputStrategyManager inputStrategyManager;

	/**
	 * Creates a label that changes it's text when the input strategy changes
	 * @param textSupplier supplies a string for what the lebel should say based on the current input method (e.g. using an argument like inputStrategyManager.getRosterButtonInputHint()))
	 * @param style
	 */
	protected InputSensitiveLabel(Supplier<String> labelTextSupplier, LabelStyle style, InputStrategyManager inputStrategyManager) {
		super(labelTextSupplier.get(), style);
		this.labelTextSupplier = labelTextSupplier;
		this.inputStrategyManager = inputStrategyManager;
		inputStrategyManager.register(this);
	}

	@Override
	public void inputStrategyChanged(InputStrategyManager inputStrategyManager) {
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
