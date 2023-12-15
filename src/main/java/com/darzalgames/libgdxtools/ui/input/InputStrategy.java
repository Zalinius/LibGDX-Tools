package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class InputStrategy extends Actor {
	public abstract boolean shouldFocusFirstButton();
	public abstract boolean shouldFlashButtons();
	public abstract boolean showMouseExclusiveButtons();	
	
	public abstract String getRosterButtonInputHint();
	public abstract String getContractButtonInputHint();
}
