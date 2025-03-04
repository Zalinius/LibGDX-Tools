package com.darzalgames.libgdxtools.ui.input.popup;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.darzalcommon.data.ListFactory;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.maingame.MainGame;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

/**
 * A pop up that offers two choices, and can respond differently based on which choice is made.
 * This is versatile: it can be used for dialog choices, menus, warnings, etc.
 */
public abstract class ChoicePopUp extends PopUpMenu {

	protected final Runnable firstChoiceRunnable;
	private final boolean isSecondButtonBack;
	protected boolean addRowAfterMessage = true;

	protected ChoicePopUp(Runnable firstChoiceRunnable, boolean isVertical, boolean isSecondButtonBack) {
		super(isVertical);
		this.firstChoiceRunnable = firstChoiceRunnable;
		this.isSecondButtonBack = isSecondButtonBack;
	}

	protected abstract UniversalButton getFirstChoiceButton();
	protected abstract UniversalButton getSecondChoiceButton();

	/**
	 * What to do when the second choice is chosen. Sometimes this will be the same as the firstChoiceRunnable,
	 * sometimes it'll be {@link Runnables#nullRunnable()} when the second button is a back button.
	 * @return
	 */
	protected abstract Runnable getSecondChoiceRunnable();

	/**
	 * @return the message at the top of the choice pop up, set up by the child class in anyway they want (in a {@link Table})
	 */
	protected abstract Table getMessage();

	/**
	 * Lets the child class optionally respond depending on which key is chosen
	 * @param chosenKey The key that was chosen
	 */
	protected void setChosenKey(String chosenKey) {}

	@Override
	protected void setUpTable() {
		setUpDesiredSize();
		background(MainGame.getUserInterfaceFactory().getUIBorderedNine());
		UserInterfaceSizer.makeActorCentered(this);

		add(getMessage()).grow();
		if (addRowAfterMessage) {
			row();
		}


		UniversalButton firstButton = getFirstChoiceButton(); 
		UniversalButton secondButton = getSecondChoiceButton();
		if (isSecondButtonBack) {
			menu.replaceContents(ListFactory.of(firstButton), secondButton); // Pressing "back" on the controller or keyboard presses the second button
		} else {
			menu.replaceContents(ListFactory.of(firstButton, secondButton)); // Pressing "back" on the controller or keyboard DOES NOT press the second button			
		}

		menu.setAlignment(Alignment.CENTER, Alignment.CENTER);
		Table table = menu.getView();
		add(table).center().grow();
	}

}