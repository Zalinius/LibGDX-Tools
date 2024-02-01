package com.darzalgames.libgdxtools.ui.input.strategy;

public interface InputStrategy  {
	public abstract boolean shouldFocusFirstButton();
	public abstract boolean shouldFlashButtons();
	public abstract boolean showMouseExclusiveButtons();

	public abstract String getButtonInputHint(String hintKey);
}
