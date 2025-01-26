package com.darzalgames.libgdxtools.hexagon;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

class BlankHexagonController implements VisibleInputConsumer {
	@Override public Actor getView() {
		return new Actor();
	}
	@Override public void consumeKeyInput(Input input) {}
	@Override public void setTouchable(Touchable isTouchable) {}
	@Override public void focusCurrent() {}
	@Override public void clearSelected() {}
	@Override public void selectDefault() {}
}