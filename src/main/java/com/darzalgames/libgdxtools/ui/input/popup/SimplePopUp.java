package com.darzalgames.libgdxtools.ui.input.popup;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;

public abstract class SimplePopUp extends Table implements InputConsumer, PopUp {
	
	protected abstract void setUpTable();
	
	@Override
	public void consumeKeyInput(Input input) {
		if (input == Input.ACCEPT || input == Input.BACK || input == Input.PAUSE) {
			hideThis();	
		}
	}

	@Override
	public void gainFocus() {
		clear();
		setUpTable();
		float startX = this.getX();
		float startY = this.getY();
		this.setPosition(-getWidth(), -getHeight());
		this.addAction(Actions.moveTo(startX, startY, 0.25f, Interpolation.circle));
	}
	
	@Override
	public void regainFocus() {
		gainFocus();
	}
	
	@Override
	public void hideThis() {
		releasePriority();
		this.toFront();
		this.addAction(Actions.sequence(
				Actions.moveTo(-getWidth(), -getHeight(), 0.25f, Interpolation.circle),
				new RunnableActionBest(super::remove)));
	}
	
    @Override
    public void selectDefault() {}
    @Override
    public void clearSelected() {}
    @Override
    public void focusCurrent() {}
    
    @Override
    public Actor getAsActor() { return this; }
	
}
