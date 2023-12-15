package com.darzalgames.libgdxtools.ui.input.scrollablemenu;

import java.util.List;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.darzalgames.libgdxtools.i18n.TextSupplier;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.PopUp;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputPrioritizer;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.LabelMaker;

public abstract class PopUpMenu extends ScrollableMenu implements PopUp {

	public PopUpMenu(boolean isVertical) {
		super(isVertical);
	}

	public PopUpMenu(boolean isVertical, List<KeyboardButton> entries, String finalButtonMessageKey) {
		super(isVertical, entries);
		menu.setFinalButton(LabelMaker.getButton(TextSupplier.getLine(finalButtonMessageKey), this::hideThis));
	}

	@Override
	public void gainFocus() {
		super.gainFocus();
		InputPrioritizer.showPopup(this);
		float startX = this.getX();
		float startY = this.getY();
		this.setY(getStage().getHeight());
		this.addAction(Actions.moveTo(startX, startY, 0.25f, Interpolation.circle));
	}
	
	@Override
	public void regainFocus() {
		super.gainFocus();
		InputPrioritizer.showPopup(this);
	}
	
	
	@Override
	public void hideThis() {
		InputPrioritizer.releasePriority(this);
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
