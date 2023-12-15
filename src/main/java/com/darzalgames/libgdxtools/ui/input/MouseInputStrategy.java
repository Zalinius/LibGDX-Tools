package com.darzalgames.libgdxtools.ui.input;

import com.darzalgames.libgdxtools.i18n.TextSupplier;

public class MouseInputStrategy extends InputStrategy {

	@Override
	public boolean shouldFocusFirstButton() {
		return false;
	}

	@Override
	public String getRosterButtonInputHint() {
		return TextSupplier.getLine("third_tab");
	}

	@Override
	public String getContractButtonInputHint() {
		return TextSupplier.getLine("first_tab") + " ";
	}
	
	@Override
	public boolean shouldFlashButtons() {
		return false;
	}

	@Override
	public boolean showMouseExclusiveButtons() {
		return true;
	}

}
