package com.darzalgames.libgdxtools.ui;

import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.darzalgames.libgdxtools.maingame.MainGame;
import com.darzalgames.libgdxtools.ui.input.popup.TextChoicePopUp;

/**
 * A basic choice popup where the second button is a back button.
 */
public class ConfirmationMenu extends TextChoicePopUp {

	public ConfirmationMenu(String messageKey, String confirmButtonTextKey, Runnable confirmButtonRunnable) {
		this(messageKey, confirmButtonTextKey, "back_message", confirmButtonRunnable);
	}

	public ConfirmationMenu(String messageKey, String confirmButtonTextKey, String backButtonTextKey, Runnable confirmButtonRunnable) {
		super(messageKey, confirmButtonTextKey, confirmButtonRunnable, backButtonTextKey, false, true, true);
	}
	
	@Override
	protected BaseDrawable getBackgroundDrawable() {
		return MainGame.getUserInterfaceFactory().getConfirmationMenuBackground();
	}

	@Override
	protected void setUpDesiredSize() {
		UserInterfaceSizer.sizeToPercentage(this, 0.4f, 0.3f);
		if (this.getActions().isEmpty()) {
			UserInterfaceSizer.makeActorCentered(this);
		}
	}

}
