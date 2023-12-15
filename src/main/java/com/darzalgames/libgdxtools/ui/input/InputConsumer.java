package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Touchable;

public interface InputConsumer {
	public void consumeKeyInput(final Input input);
	public void setTouchable(Touchable isTouchable); // If the receiver of this function has a child that is also an InputConsumer on top of it, then instead of setting disabled it should set childrenOnly
	public default void gainFocus() {}
	public default void regainFocus() {}
	public default void loseFocus() {}
	public void focusCurrent();
	
	public void clearSelected();
	public void selectDefault();
}
