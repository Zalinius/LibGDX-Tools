package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;
import com.github.tommyettinger.textra.TextraButton;

/**
 * Extend the LibGDX TextButton class, making it implement my own BasicButton interface
 */
public class MyTextButton extends TextraButton implements BasicButton {

	public MyTextButton(String text, TextButtonStyle textButtonStyle) {
		super(text, textButtonStyle);
	}

	@Override
	public Actor getView() {
		return this;
	}

	@Override
	public String getButtonText() {
		return getText();
	}


}
