package com.darzalgames.libgdxtools.ui;

import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.popup.TextChoicePopUp;

/**
 * A basic choice popup where the second button is a back button.
 */
public class ConfirmationMenu extends TextChoicePopUp {

	public ConfirmationMenu(String messageKey, String confirmButtonTextKey, Runnable confirmButtonRunnable, String stageName) {
		this(messageKey, confirmButtonTextKey, "back_message", confirmButtonRunnable, stageName);
	}

	public ConfirmationMenu(String messageKey, String confirmButtonTextKey, String backButtonTextKey, Runnable confirmButtonRunnable, String stageName) {
		super(messageKey, confirmButtonTextKey, confirmButtonRunnable, backButtonTextKey, false, true, true, stageName);
	}

	@Override
	protected BaseDrawable getBackgroundDrawable() {
		return GameInfo.getUserInterfaceFactory().getConfirmationMenuBackground();
	}

	@Override
	protected void setUpDesiredSize() {
		UserInterfaceSizer.sizeToPercentage(this, 0.4f, 0.3f);
		if (getActions().isEmpty()) {
			UserInterfaceSizer.makeActorCentered(this);
		}
	}

}
