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
	public void consumeKeyInput(Input input) {}

	@Override
	public void setTouchable(Touchable isTouchable) {}

	@Override
	public void focusCurrent() {}

	@Override
	public void clearSelected() {}

	@Override
	public void selectDefault() {}

	@Override
	public void resizeUI() {}

	@Override
	public boolean isDisabled() {
		return false;
	}

	@Override
	public boolean isBlank() {
		return false;
	}

	@Override
	public void setAlignment(Alignment alignment) {}

	@Override
	public void setFocused(boolean focused) {}

	@Override
	public float getMinHeight() {
		return 0;
	}

	@Override
	public void setDisabled(boolean disabled) {}

	@Override
	public boolean isOver() {
		return false;
	}
}