package com.darzalgames.libgdxtools.ui.input.popup;

import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UserInterfaceFactory;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.inputpriority.InputPriorityManager;

/**
 * @author DarZal
 * A pop up that offers two choices, and can respond differently based on which choice is made.
 * This is versatile: it can be used for dialog choices, menus, warnings, etc.
 */
public class TextChoicePopUp extends ChoicePopUp {

	protected final String messageKey;
	private final String firstChoiceKey;
	private final String secondChoiceKey;
	private final boolean isWarning;

	protected TextChoicePopUp(String messageKey, String firstChoiceKey, Runnable firstChoiceRunnable, 
			String secondChoiceKey, boolean isVertical, boolean isSecondButtonBack, boolean isWarning) {
		super(firstChoiceRunnable, isVertical, isSecondButtonBack);
		this.messageKey = messageKey;
		this.firstChoiceKey = firstChoiceKey;
		this.secondChoiceKey = secondChoiceKey;
		this.isWarning = isWarning;

		InputPriorityManager.claimPriority(this);
	}

	@Override
	protected UniversalButton getFirstChoiceButton() {
		return getChoiceButton(firstChoiceKey, firstChoiceRunnable);
	}

	@Override
	protected UniversalButton getSecondChoiceButton() {
		return getChoiceButton(secondChoiceKey, () -> getSecondChoiceRunnable().run());
	}
	
	private UniversalButton getChoiceButton(String key, Runnable toRun) {
		Runnable chooseAndHideRunnable = () -> {
			setChosenKey(key);
			hideThis();
			toRun.run();
		};
		return UserInterfaceFactory.getButton(TextSupplier.getLine(key), chooseAndHideRunnable);
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
		table.add(label).grow();
		return table;
	}

}