package com.darzalgames.libgdxtools.ui.input.popup;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.MenuOrientation;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;

public abstract class NavigableListPopUpMenu extends NavigableListMenu implements PopUpMenu {

	protected NavigableListPopUpMenu(MenuOrientation menuOrientation) {
		super(menuOrientation);
	}

	protected NavigableListPopUpMenu(MenuOrientation menuOrientation, List<VisibleInputConsumer> entries) {
		super(menuOrientation, entries);
	}

	@Override
	public void gainFocus() {
		slideIn(super::gainFocus, this::focusCurrent);
	}

	@Override
	public void consumeKeyInput(Input input) {
		possiblyDismiss(input, super::consumeKeyInput);
	}

	@Override
	public Actor getAsActor() {
		return this;
	}

	@Override
	public Actor getView() {
		return getAsActor();
	}

}
