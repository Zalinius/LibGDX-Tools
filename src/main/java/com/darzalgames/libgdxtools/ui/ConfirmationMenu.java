package com.darzalgames.libgdxtools.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.popup.TextChoicePopUp;

/**
 * A basic choice popup where the second button is a back button.
 * @author DarZal
 */
public class ConfirmationMenu extends TextChoicePopUp {
	
	private static NinePatch confirmationBackground;

	public ConfirmationMenu(String messageKey, String confirmButtonTextKey, Runnable confirmButtonRunnable) {
		this(messageKey, confirmButtonTextKey, "back_message", confirmButtonRunnable);
	}
	
	public ConfirmationMenu(String messageKey, String confirmButtonTextKey, String backButtonTextKey, Runnable confirmButtonRunnable) {
		super(messageKey, confirmButtonTextKey, confirmButtonRunnable, backButtonTextKey, false, true, true);
		InputPriorityManager.releasePriority(this);
		desiredWidth = 275;
		desiredHeight = 100;
		InputPriorityManager.claimPriority(this);
	}
	
	public static void setConfirmationBackground(NinePatch confirmationBackground) {
		ConfirmationMenu.confirmationBackground = confirmationBackground;
	}

	@Override
	protected void setSizeAndBackground() {
		setSize(desiredWidth, desiredHeight);
        NinePatchDrawable back = new NinePatchDrawable(confirmationBackground);
		background(back);
	}
	
}
