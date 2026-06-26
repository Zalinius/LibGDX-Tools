package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

public abstract class NavigableListPopUpMenu extends NavigableListMenu implements PopUpMenu {

	protected NavigableListPopUpMenu(MenuOrientation menuOrientation) {
		this(menuOrientation, new ArrayList<>(), false);
	}

	protected NavigableListPopUpMenu(MenuOrientation menuOrientation, List<VisibleInputConsumer> entries, boolean addBackButton) {
		super(menuOrientation, entries);
		if (addBackButton) {
			setFinalButton(makeDefaultBackButton());
		}
	}

	@Override
	public void gainFocus() {
		slideIn(super::gainFocus, this::focusCurrent);
	}

	@Override
	public void regainFocus() {
		focusCurrent();
	}

	@Override
	public void consumeKeyInput(Input input) {
		possiblyDismiss(input, super::consumeKeyInput);
	}

	@Override
	public void resizeUI() {
		setUpDesiredSize();
		super.resizeUI();
	}

	@Override
	public Actor getAsActor() {
		return this;
	}

}
