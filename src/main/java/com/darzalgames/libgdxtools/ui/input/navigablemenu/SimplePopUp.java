package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.Input;

public abstract class SimplePopUp extends Table implements PopUpMenu {

	protected abstract void setUpTable();

	@Override
	public void consumeKeyInput(Input input) {
		if (input == Input.ACCEPT || input == Input.BACK) {
			hideThis();
		}
	}

	@Override
	public void gainFocus() {
		clear();
		setUpTable();
		setUpDesiredSize();
		UserInterfaceSizer.makeActorCentered(this);
		slideIn(Runnables.nullRunnable(), Runnables.nullRunnable());
	}

	@Override
	public void regainFocus() {
		gainFocus();
	}

	@Override
	public void selectDefault() {}

	@Override
	public void clearSelected() {}

	@Override
	public void focusCurrent() {}

	@Override
	public Actor getAsActor() {
		return this;
	}

	@Override
	public Actor getView() {
		return this;
	}

}
