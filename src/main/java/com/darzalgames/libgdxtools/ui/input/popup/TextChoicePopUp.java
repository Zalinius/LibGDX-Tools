package com.darzalgames.libgdxtools.ui.input.popup;

import java.util.function.Function;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalLabel;

/**
 * A pop up that offers two choices, and can respond differently based on which choice is made.
 * This is versatile: it can be used for dialog choices, menus, warnings, etc.
 */
public abstract class TextChoicePopUp extends ChoicePopUp {

	protected final String messageKey;
	private final String firstChoiceKey;
	private final String secondChoiceKey;
	private final boolean isWarning;

	protected TextChoicePopUp(String messageKey, String firstChoiceKey, Runnable firstChoiceRunnable,
			String secondChoiceKey, boolean isVertical, boolean isSecondButtonBack, boolean isWarning, String stageName) {
		super(firstChoiceRunnable, isVertical, isSecondButtonBack);
		this.messageKey = messageKey;
		this.firstChoiceKey = firstChoiceKey;
		this.secondChoiceKey = secondChoiceKey;
		this.isWarning = isWarning;

		InputPriority.claimPriority(this, stageName);
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
		return GameInfo.getUserInterfaceFactory().makeTextButton(() -> TextSupplier.getLine(key), chooseAndHideRunnable);
	}

	@Override
	protected Runnable getSecondChoiceRunnable() {
		return Runnables.nullRunnable();
	}

	@Override
	protected Table getMessage() {
		Function<Supplier<String>, UniversalLabel> labelFunction = isWarning ? GameInfo.getUserInterfaceFactory()::getWarningLabel : GameInfo.getUserInterfaceFactory()::getLabelWithBackground;
		UniversalLabel label = labelFunction.apply(() -> TextSupplier.getLine(messageKey));
		label.setAlignment(Alignment.CENTER);
		Table table = new Table();
		table.add(label).grow();
		return table;
	}

}