package com.darzalgames.libgdxtools.hexagon.twodee;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

public class BlankHexagonController implements VisibleInputConsumer {
	@Override
	public Actor getView() {
		return new Actor();
	}

	@Override
	public void consumeKeyInput(Input input) { /* Not needed for testing */ }

	@Override
	public void setTouchable(Touchable isTouchable) { /* Not needed for testing */ }

	@Override
	public void focusCurrent() { /* Not needed for testing */ }

	@Override
	public void clearSelected() { /* Not needed for testing */ }

	@Override
	public void selectDefault() { /* Not needed for testing */ }

	@Override
	public void resizeUI() { /* Not needed for testing */ }

	@Override
	public boolean isDisabled() {
		return false;
	}

	@Override
	public boolean isBlank() {
		return false;
	}

	@Override
	public void setAlignment(Alignment alignment) { /* Not needed for testing */ }

	@Override
	public void setFocused(boolean focused) { /* Not needed for testing */ }

	@Override
	public float getMinHeight() {
		return 0;
	}

	@Override
	public void setDisabled(boolean disabled) { /* Not needed for testing */ }

	@Override
	public boolean isOver() {
		return false;
	}
}