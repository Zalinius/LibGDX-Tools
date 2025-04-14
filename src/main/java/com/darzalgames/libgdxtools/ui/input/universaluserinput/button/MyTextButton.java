package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Extend the LibGDX TextButton class, making it implement my own BasicButton interface
 */
public class MyTextButton extends TextButton implements BasicButton {

	public MyTextButton(String text, TextButtonStyle textButtonStyle) {
		super(text, textButtonStyle);
	}

	@Override
	public Actor getView() {
		return this;
	}

	@Override
	public String getButtonText() {
		return getText().toString();
	}

}
