package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;

public class InputConsumerForTesting implements InputConsumer {

	@Override
	public void consumeKeyInput(Input input) {/* not needed */}

	@Override
	public void setTouchable(Touchable isTouchable) {/* not needed */}

	@Override
	public void focusCurrent() {/* not needed */}

	@Override
	public void clearSelected() {/* not needed */}

	@Override
	public void selectDefault() {/* not needed */}

	@Override
	public void loseFocus() {/* not needed */}

	@Override
	public String toString() {
		return "Testing input consumer";
	}

	@Override
	public void resizeUI() {/* not needed */}

	@Override
	public boolean isDisabled() {
		return false;
	}

	@Override
	public boolean isBlank() {
		return false;
	}

	@Override
	public void setAlignment(Alignment alignment) {/* not needed */}

	@Override
	public void setFocused(boolean focused) {/* not needed */}

	@Override
	public void setDisabled(boolean disabled) {/* not needed */}

}
