package com.darzalgames.libgdxtools.ui.input.popup;

import java.util.List;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.MainGame;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

/**
 * It's a navigable menu, and it's a pop up!
 */
public abstract class PopUpMenu extends NavigableListMenu implements PopUp {

	protected int desiredWidth;
	protected int desiredHeight;
	
	protected PopUpMenu(boolean isVertical) {
		super(isVertical);
		setUpDesiredSize();
	}

	protected PopUpMenu(boolean isVertical, List<UniversalButton> entries, String finalButtonMessageKey) {
		super(isVertical, entries);
		menu.replaceContents(entries, makeFinalButton(finalButtonMessageKey)); // Because the final button calls this::hideThis, we make it after the call to super()
		
		setUpDesiredSize();
	}

	/** The height and width the menu background should be, override if you don't like the default  */
	protected void setUpDesiredSize() {
		desiredWidth = 200;
		desiredHeight = 100;
	}
	
	private UniversalButton makeFinalButton(String finalButtonMessageKey) {
		return MainGame.getUserInterfaceFactory().getButton(TextSupplier.getLine(finalButtonMessageKey), this::hideThis);
	}

	@Override
	public void gainFocus() {
		super.gainFocus();
		float startX = this.getX();
		float startY = this.getY();
		this.setY(getStage().getHeight());
		this.addAction(Actions.moveTo(startX, startY, 0.25f, Interpolation.circle));
	}
	
	@Override
	public void hideThis() {
		releasePriority();
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
    
    @Override
    public Actor getAsActor() { return this; }
}
