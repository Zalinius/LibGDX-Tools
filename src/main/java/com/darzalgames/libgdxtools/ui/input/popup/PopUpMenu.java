package com.darzalgames.libgdxtools.ui.input.popup;

import java.util.List;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

/**
 * It's a navigable menu, and it's a pop up!
 */
public abstract class PopUpMenu extends NavigableListMenu implements PopUp {

	private Runnable runJustBeforeRemove = Runnables.nullRunnable();

	protected PopUpMenu(boolean isVertical) {
		super(isVertical);
	}

	protected PopUpMenu(boolean isVertical, List<UniversalButton> entries, String finalButtonMessageKey) {
		super(isVertical, entries);
		menu.replaceContents(entries, makeFinalButton(finalButtonMessageKey)); // Because the final button calls this::hideThis, we make it after the call to super()
	}

	protected abstract void setUpDesiredSize();

	protected UniversalButton makeFinalButton(String finalButtonMessageKey) {
		return GameInfo.getUserInterfaceFactory().getButton(() -> TextSupplier.getLine(finalButtonMessageKey), this::hideThis);
	}

	protected boolean slidesInAndOut() {
		return true;
	}

	@Override
	public void gainFocus() {
		super.gainFocus();
		if (slidesInAndOut()) {
			float startX = this.getX();
			float startY = this.getY();
			this.setY(UserInterfaceSizer.getCurrentHeight());
			addAction(Actions.moveTo(startX, startY, 0.25f, Interpolation.circle));
		}
	}

	@Override
	public void regainFocus() {
		menu.regainFocus();
	}

	@Override
	public void hideThis() {
		releasePriority();
		if (slidesInAndOut()) {
			addAction(Actions.sequence(
					Actions.moveTo(getX(), UserInterfaceSizer.getCurrentHeight(), 0.25f, Interpolation.circle),
					new RunnableActionBest(runJustBeforeRemove),
					new RunnableActionBest(super::remove)));
			toFront();
		} else {
			remove();
		}
	}

	protected void setRunJustBeforeRemove(Runnable runJustBeforeRemove) {
		this.runJustBeforeRemove = runJustBeforeRemove;
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


	@Override
	public void resizeUI() {
		setUpDesiredSize();
		menu.resizeUI();
	}
}
