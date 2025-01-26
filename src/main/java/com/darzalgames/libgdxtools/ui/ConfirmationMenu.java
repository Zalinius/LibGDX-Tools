package com.darzalgames.libgdxtools.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.darzalgames.libgdxtools.ui.input.popup.TextChoicePopUp;

/**
 * A basic choice popup where the second button is a back button.
 */
public class ConfirmationMenu extends TextChoicePopUp {
	
	public static NinePatch confirmationBackground;

	public ConfirmationMenu(String messageKey, String confirmButtonTextKey, Runnable confirmButtonRunnable) {
		this(messageKey, confirmButtonTextKey, "back_message", confirmButtonRunnable);
	}
	
	public ConfirmationMenu(String messageKey, String confirmButtonTextKey, String backButtonTextKey, Runnable confirmButtonRunnable) {
		super(messageKey, confirmButtonTextKey, confirmButtonRunnable, backButtonTextKey, false, true, true);
	}

	@Override
	protected void setUpDesiredSize() {
		desiredWidth = 275;
		desiredHeight = 100;
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
