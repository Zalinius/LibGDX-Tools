package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.libgdxtools.maingame.MainGame;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

/**
 * The actor that holds a {@link NavigableList} and handles how it looks and is interacted with.
 */
public abstract class NavigableListMenu extends Table implements InputConsumer {
	protected NavigableList menu;

	protected NavigableListMenu(boolean isVertical) {
		this(isVertical, new LinkedList<>());
	}

	protected NavigableListMenu(boolean isVertical, List<UniversalButton> entries) {
		menu = new NavigableList(isVertical, entries);
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
	 * 		menu.setAlignment(Alignment.TOP_LEFT);
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

	/**
	 * Set the focus to a particular {@link UniversalButton}
	 * @param UniversalButton
	 */
	public void goTo(UniversalButton UniversalButton) {
		menu.goTo(UniversalButton);
	}

	/**
	 * Set the focus to a particular button index (often first or last, but there can be more specialized applications)
	 * @param index
	 */
	public void goTo(final int index) {
		menu.goTo(index);
	}

	/**
	 * Sets whether or not to press a button immediately when an entry is changed.
	 * This is generally false, but will be true for menu tabs navigated with the bumpers, for example.
	 * @param pressButtonOnEntryChanged
	 */
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
		int nonInteractablesToIgnore = 0;
		for (int i = 0; i < menu.allEntries.size(); i++) {
			UniversalButton thisEntry = menu.allEntries.get(i); 
			if (thisEntry.doesTextMatch(entry)) {
				menu.goTo(i - nonInteractablesToIgnore);
				return;
			}
			else if (thisEntry.getButton().isDisabled() || MainGame.getUserInterfaceFactory().isSpacer(thisEntry)) {
				nonInteractablesToIgnore++;
			}
		}
		menu.goTo(0);
	}
	
}
