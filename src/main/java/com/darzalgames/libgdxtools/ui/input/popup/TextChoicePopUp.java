package com.darzalgames.libgdxtools.ui.input.popup;

import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.i18n.TextSupplier;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.UserInterfaceFactory;

/**
 * @author DarZal
 * A pop up that offers two choices, and can respond differently based on which choice is made.
 * This is versatile: it can be used for dialog choices, menus, warnings, etc.
 */
public class TextChoicePopUp extends ChoicePopUp {

	protected final String messageKey;
	private final String firstChoiceKey;
	private final String secondChoiceKey;
	protected final Runnable firstChoiceRunnable;
	private final boolean isWarning;

	protected TextChoicePopUp(String messageKey, String firstChoiceKey, Runnable firstChoiceRunnable, 
			String secondChoiceKey, boolean isSecondButtonBack, boolean isWarning) {
		super(firstChoiceRunnable, false, isSecondButtonBack);
		this.messageKey = messageKey;
		this.firstChoiceKey = firstChoiceKey;
		this.secondChoiceKey = secondChoiceKey;
		this.firstChoiceRunnable = firstChoiceRunnable;
		this.isWarning = isWarning;

		InputPriorityManager.claimPriority(this);
	}

	// TODO Compress these two in2 one
	@Override
	protected KeyboardButton getFirstChoiceButton() {
		Runnable firstAndHideRunnable = () -> {
			setChosenKey(firstChoiceKey);
			hideThis();
			firstChoiceRunnable.run();
		};
		return UserInterfaceFactory.getButton(TextSupplier.getLine(firstChoiceKey), firstAndHideRunnable); 
	}

	@Override
	protected KeyboardButton getSecondChoiceButton() {
		Runnable secondAndHideRunnable = () -> {
			setChosenKey(secondChoiceKey);
			hideThis();
			getSecondChoiceRunnable().run();
		};
		return UserInterfaceFactory.getButton(TextSupplier.getLine(secondChoiceKey), secondAndHideRunnable);
	}

	@Override
	protected Runnable getSecondChoiceRunnable() {
		return Runnables.nullRunnable();
	}

	@Override
	protected Table getMessage() {
		Function<String, Label> labelFunction = isWarning ? UserInterfaceFactory::getWarningLabel : UserInterfaceFactory::getLabelWithBackground;
		Label label = labelFunction.apply(TextSupplier.getLine(messageKey));
		label.setAlignment(Alignment.CENTER.getAlignment());
		Table table = new Table();
		table.add(label).growX();
		return table;
	}

}