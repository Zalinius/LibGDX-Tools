package com.darzalgames.libgdxtools.ui.input.popup;

import java.util.List;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.UserInterfaceFactory;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;

/**
 * It's a navigable menu, and it's a pop up!
 * @author DarZal
 */
public abstract class PopUpMenu extends NavigableListMenu implements PopUp {

	protected int desiredWidth;
	protected int desiredHeight;
	
	protected PopUpMenu(boolean isVertical) {
		super(isVertical);
		setUpDesiredSize();
	}

	protected PopUpMenu(boolean isVertical, List<KeyboardButton> entries, String finalButtonMessageKey) {
		super(isVertical, entries);
		menu.replaceContents(entries, makeFinalButton(finalButtonMessageKey)); // Because the final button calls this::hideThis, we make it after the call to super()
		
		setUpDesiredSize();
	}

	/** The height and width the menu background should be, override if you don't like the default  */
	protected void setUpDesiredSize() {
		desiredWidth = 200;
		desiredHeight = 100;
	}
	
	private KeyboardButton makeFinalButton(String finalButtonMessageKey) {
		return UserInterfaceFactory.getButton(TextSupplier.getLine(finalButtonMessageKey), this::hideThis);
	}

	@Override
	public void gainFocus() {
		super.gainFocus();
		InputPriorityManager.showPopup(this);
		float startX = this.getX();
		float startY = this.getY();
		this.setY(getStage().getHeight());
		this.addAction(Actions.moveTo(startX, startY, 0.25f, Interpolation.circle));
	}
	
	@Override
	public void regainFocus() {
		super.gainFocus();
		InputPriorityManager.showPopup(this);
	}
	
	
	@Override
	public void hideThis() {
		InputPriorityManager.releasePriority(this);
		this.toFront();
		this.addAction(Actions.sequence(
				Actions.moveTo(getX(), getStage().getHeight(), 0.25f, Interpolation.circle),
				new RunnableActionBest(super::remove)));
	}
	
	@Override
	public void consumeKeyInput(Input input) {
		if (canDismiss() && input == Input.PAUSE) {
			input = Input.BACK;
		}
		super.consumeKeyInput(input);
	}
}
