package com.darzalgames.libgdxtools.ui;

import java.util.function.Function;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.i18n.TextSupplier;

public class ConfirmationMenu extends ChoicePopUp {
	
	protected Texture confirmationBackground;

	public ConfirmationMenu(String messageKey, String confirmButtonTextKey, Runnable confirmButtonRunnable) {
		this(messageKey, confirmButtonTextKey, "back_message", confirmButtonRunnable);
	}
	
	public ConfirmationMenu(String messageKey, String confirmButtonTextKey, String backButtonTextKey, Runnable confirmButtonRunnable) {
		super(messageKey, confirmButtonTextKey, backButtonTextKey, confirmButtonRunnable, false, true, true);
	}
	
	@Override
	protected Runnable getSecondChoiceRunnable() {
		return Runnables.nullRunnable();
	}

	@Override
	protected void setSizeAndBackground() {
		setSize(275, 100);
        NinePatchDrawable back = new NinePatchDrawable(new NinePatch(confirmationBackground, 14, 14, 12, 12));
		background(back);
	}
	
	@Override
	protected Table getMessage(Function<String, Label> labelFunction) {
		Label label = labelFunction.apply(TextSupplier.getLine(messageKey));
		label.setAlignment(Align.center);
		Table table = new Table();
		table.add(label).growX();
		return table;
	}
	
}
