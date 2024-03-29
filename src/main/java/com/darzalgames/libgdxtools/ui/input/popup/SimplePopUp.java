package com.darzalgames.libgdxtools.ui.input.popup;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumerWrapper;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;

public abstract class SimplePopUp extends Table implements InputConsumerWrapper, PopUp {
	
	protected abstract void setUpTable();
	
	@Override
	public void consumeKeyInput(Input input) {
		if (input == Input.ACCEPT || input == Input.BACK || input == Input.PAUSE) {
			hideThis();	
		}
	}

	/**
	 * Make the pop up visible and have it claim input priority
	 */
	public void showThis() {
		InputPriorityManager.claimPriority(this);
	}

	@Override
	public void gainFocus() {
		clear();
		setUpTable();
		float startX = this.getX();
		float startY = this.getY();
		this.setPosition(-getWidth(), -getHeight());
		this.addAction(Actions.moveTo(startX, startY, 0.25f, Interpolation.circle));
		InputPriorityManager.showPopup(this);
	}
	
	@Override
	public void regainFocus() {
		gainFocus();
	}
	
	@Override
	public void hideThis() {
		InputPriorityManager.releasePriority(this);
		this.toFront();
		this.addAction(Actions.sequence(
				Actions.moveTo(-getWidth(), -getHeight(), 0.25f, Interpolation.circle),
				new RunnableActionBest(super::remove)));
	}
}
