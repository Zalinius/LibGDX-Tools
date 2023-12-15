package com.darzalgames.libgdxtools.ui.input.scrollablemenu;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.InputPrioritizer;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;

public abstract class ScrollableMenu extends Table implements InputConsumer {
	protected ScrollableUI menu;

	protected ScrollableMenu(boolean isVertical) {
		this(isVertical, new LinkedList<>());
	}

	protected ScrollableMenu(boolean isVertical, List<KeyboardButton> entries) {
		menu = new ScrollableUI(isVertical, entries);
	}

	@Override
	public void consumeKeyInput(Input input) {
		menu.consumeKeyInput(input);
	}

	/**
	 * Be SUPER SURE to call the following in your implementation:
	 * 		add(menu.getView());
	 * with whatever modifiers you want (grow, colspan, etc...)
	 * 
	 * Consider calling things like:
	 * 		menu.setAlignment(Align.topLeft);
	 * 		menu.replaceContents(menuButtons, backButton);
	 * 
	 * No need to call clear() first or anything.
	 */
	protected abstract void setUpTable();

	@Override
	public void setTouchable(Touchable isTouchable) {
		if (menu != null) { // This is called in the constructor for Table, at which point the menu is not yet made
			menu.setTouchable(isTouchable);
		}
	}

	public void hideThis() {
		remove();
		InputPrioritizer.releasePriority(this);
	}

	public void goTo(final int index) {
		menu.goTo(index);
	}

	public void setPressButtonOnEntryChanged(boolean pressButtonOnEntryChanged) {
		menu.setPressButtonOnEntryChanged(pressButtonOnEntryChanged);
	}

	@Override
	public void gainFocus() {
		clear();
		setUpTable();
		selectDefault();
	}

	@Override
	public void regainFocus() {
		gainFocus();
	}

	@Override
	public void clearSelected() {
		menu.clearSelected();
	}

	@Override
	public void selectDefault() {
		menu.clearSelected();
		menu.returnToFirst();
	}

	@Override
	public float getPrefHeight() {
		return menu.getPrefHeight();
	}

	@Override
	public void focusCurrent() {
		menu.focusCurrent();
	}

	/**
	 * This method will try to find the entry corresponding to the string provided, 
	 * if it fails then we default to the first entry.
	 * @param entry The string of the entry that you want to go to
	 */
	public void goTo(String entry) {
		for (int i = 0; i < menu.allEntries.size(); i++) {
			if (menu.allEntries.get(i).getButtonText().equalsIgnoreCase(entry)) {
				menu.goTo(i);
				return;
			}
		}
		menu.goTo(0);
	}
	
}
