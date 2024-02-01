package com.darzalgames.libgdxtools.ui.input.popup;

import java.util.List;
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
public abstract class ChoicePopUp extends PopUpMenu {

	protected final String messageKey;
	private final String firstChoiceKey;
	private final String secondChoiceKey;
	protected final Runnable firstChoiceRunnable;
	private final boolean isSecondButtonBack;
	protected final boolean isWarning;

	protected ChoicePopUp(String messageKey, String firstChoiceKey, String secondChoiceKey, Runnable firstChoiceRunnable) {
		this(messageKey, firstChoiceKey, secondChoiceKey, firstChoiceRunnable, false, false, false);
	}

	protected ChoicePopUp(String messageKey, String firstChoiceKey, String secondChoiceKey, Runnable firstChoiceRunnable,
			boolean isVertical, boolean isSecondButtonBack, boolean isWarning) {
		super(isVertical);
		this.messageKey = messageKey;
		this.firstChoiceKey = firstChoiceKey;
		this.secondChoiceKey = secondChoiceKey;
		this.firstChoiceRunnable = firstChoiceRunnable;
		this.isSecondButtonBack = isSecondButtonBack;
		this.isWarning = isWarning;

		InputPriorityManager.claimPriority(this);
	}

	/**
	 * What to do when the second choice is chosen. Sometimes this will be the same as the firstChoiceRunnable,
	 * sometimes it'll be {@link Runnables#nullRunnable()} when the second button is a back button.
	 * @return
	 */
	protected abstract Runnable getSecondChoiceRunnable();
	
	/**
	 * Lets the child class set up the message at the top of the choice pop up in anyway they want (in a {@link Table})
	 * @param labelFunction Creates a label in a different style depending on whether or not the popup is a warning
	 * @return
	 */
	protected abstract Table getMessage(Function<String, Label> labelFunction);
	
	/**
	 * Lets the child class optionally respond depending on which key is chosen
	 * @param chosenKey The key that was chosen
	 */
	protected void setChosenKey(String chosenKey) {}

	protected void setSizeAndBackground() {
		setSize(200, 106);
		background(UserInterfaceFactory.getUIBorderedNine());
	}

	@Override
	protected void setUpTable() {
		setSizeAndBackground();
		UserInterfaceFactory.makeActorCentered(this);

		Function<String, Label> labelFunction = isWarning ? UserInterfaceFactory::getWarningLabel : UserInterfaceFactory::getLabelWithBackground;
		add(getMessage(labelFunction)).grow();
		row();


		Runnable firstAndHideRunnable = () -> {
			setChosenKey(firstChoiceKey);
			hideThis();
			firstChoiceRunnable.run();
		};
		Runnable secondAndHideRunnable = () -> {
			setChosenKey(secondChoiceKey);
			hideThis();
			getSecondChoiceRunnable().run();
		};
		KeyboardButton firstButton = UserInterfaceFactory.getButton(TextSupplier.getLine(firstChoiceKey), firstAndHideRunnable); 
		KeyboardButton secondButton = UserInterfaceFactory.getButton(TextSupplier.getLine(secondChoiceKey), secondAndHideRunnable);
		if (isSecondButtonBack) {
			menu.replaceContents(List.of(firstButton), secondButton); // Pressing "back" on the controller or keyboard presses the second button
		} else {
			menu.replaceContents(List.of(firstButton, secondButton)); // Pressing "back" on the controller or keyboard DOES NOT press the second button			
		}

		menu.setAlignment(Alignment.CENTER, Alignment.CENTER);
		Table table = menu.getView();
		add(table).center().grow();
	}

}