package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputStrategyObserver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;


/**
 * A {@link UniversalButton} which will only be visible and intractable in mouse mode
 */
public class MouseOnlyButton extends UniversalButton implements InputStrategyObserver {

	public MouseOnlyButton(BasicButton textButton, Supplier<String> textSupplier, Runnable runnable, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(textButton, textSupplier, runnable, inputStrategySwitcher, soundInteractListener);
		inputStrategySwitcher.register(this);
		setVisibilityBasedOnCurrentInputStrategy(inputStrategySwitcher);
	}

	public MouseOnlyButton(BasicButton textButton, Supplier<String> textSupplier, Image image, Runnable runnable, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(textButton, textSupplier, image, runnable, inputStrategySwitcher, soundInteractListener);
		inputStrategySwitcher.register(this);
		setVisibilityBasedOnCurrentInputStrategy(inputStrategySwitcher);
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategySwitcher) {
		setVisibilityBasedOnCurrentInputStrategy(inputStrategySwitcher);
	}
	
	private void setVisibilityBasedOnCurrentInputStrategy(InputStrategySwitcher inputStrategySwitcher) {
		this.getView().setVisible(inputStrategySwitcher.isMouseMode());
	}

	@Override
	public boolean shouldBeUnregistered() {
		return this.getView().getStage() == null;
	}
	
}
