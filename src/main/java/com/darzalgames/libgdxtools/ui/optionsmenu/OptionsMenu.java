package com.darzalgames.libgdxtools.ui.optionsmenu;

import com.darzalgames.darzalcommon.misc.DoesNotPause;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.scrollablemenu.PopUpMenu;

public abstract class OptionsMenu extends PopUpMenu implements DoesNotPause {
	
	// TODO Actually grab this class from Quest Giver once the music is in this library

	protected KeyboardButton optionsButton;

	protected OptionsMenu() {
		super(true);
	}

	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}

	public KeyboardButton getButton() {
		return optionsButton;
	}
}
