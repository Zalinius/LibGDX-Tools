package com.darzalgames.libgdxtools.ui;

import java.util.function.Function;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.i18n.TextSupplier;
import com.darzalgames.libgdxtools.ui.input.popup.ChoicePopUp;

/**
 * A basic choice popup where the second button is a back button.
 * @author DarZal
 */
public class ConfirmationMenu extends ChoicePopUp {
	
	private static NinePatch confirmationBackground;

	public ConfirmationMenu(String messageKey, String confirmButtonTextKey, Runnable confirmButtonRunnable) {
		this(messageKey, confirmButtonTextKey, "back_message", confirmButtonRunnable);
	}
	
	public ConfirmationMenu(String messageKey, String confirmButtonTextKey, String backButtonTextKey, Runnable confirmButtonRunnable) {
		super(messageKey, confirmButtonTextKey, backButtonTextKey, confirmButtonRunnable, false, true, true);
	}
	
	public static void setConfirmationBackground(NinePatch confirmationBackground) {
		ConfirmationMenu.confirmationBackground = confirmationBackground;
	}

	@Override
	protected Runnable getSecondChoiceRunnable() {
		return Runnables.nullRunnable();
	}

	@Override
	protected void setSizeAndBackground() {
		setSize(275, 100);
        NinePatchDrawable back = new NinePatchDrawable(confirmationBackground);
		background(back);
	}
	
	@Override
	protected Table getMessage(Function<String, Label> labelFunction) {
		Label label = labelFunction.apply(TextSupplier.getLine(messageKey));
		label.setAlignment(Alignment.CENTER.getAlignment());
		Table table = new Table();
		table.add(label).growX();
		return table;
	}
	
}
