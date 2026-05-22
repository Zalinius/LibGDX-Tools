package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Predicate;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

/**
 * The actor that holds a {@link NavigableList} and handles how it looks and is interacted with.
 */
public abstract class NavigableListMenu extends Table implements InputConsumer {
	protected NavigableList menu;

	protected NavigableListMenu(MenuOrientation menuOrientation) {
		this(menuOrientation, new LinkedList<>());
	}

	protected NavigableListMenu(MenuOrientation menuOrientation, List<VisibleInputConsumer> entries) {
		menu = new NavigableList(menuOrientation, entries);
	}

	@Override
	public void consumeKeyInput(Input input) {
		menu.consumeKeyInput(input);
	}

	/**
	 * @param input the Input to query
	 * @return whether or not this menu can make use of the input in a meaningful way
	 */
	public boolean canUseInput(Input input) {
		return menu.canUseInput(input);
	}

	/**
	 * Be SUPER SURE to call the following in your implementation:
	 * add(menu.getView());
	 * with whatever modifiers you want (grow, colspan, etc...)
	 *
	 * Consider calling things like:
	 * menu.setAlignment(Alignment.TOP_LEFT);
	 * menu.replaceContents(menuButtons, backButton);
	 *
	 * No need to call clear() first or anything.
	 */
	protected abstract void setUpTable();

	/**
	 * Set the focus to a particular {@link VisibleInputConsumer}
	 * @param entry the input consumer to focus on
	 * @return Whether or not this menu has that entry (and if so, then it was selected). Returns false if that entry was already the current one.
	 */
	public boolean goTo(VisibleInputConsumer entry) {
		return menu.goTo(entry);
	}

	/**
	 * This is generally false, but will be true for menu tabs navigated with the bumpers, for example.
	 * @param pressButtonOnEntryChanged Set whether or not navigating to an entry presses it automatically
	 */
	public void setPressButtonOnEntryChanged(boolean pressButtonOnEntryChanged) {
		menu.setPressButtonOnEntryChanged(pressButtonOnEntryChanged);
	}

	/**
	 * @return The entry that's currently in focus
	 */
	public VisibleInputConsumer getCurrentButton() {
		return menu.getCurrentButton();
	}

	@Override
	public void gainFocus() {
		clear();
		menu.defaultRefreshPage();
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
	public void focusCurrent() {
		menu.focusCurrent();
	}

	@Override
	public void setFocused(boolean focused) {
		menu.setFocused(focused);
	}

	@Override
	public boolean isDisabled() {
		return menu.isDisabled();
	}

	@Override
	public boolean isBlank() {
		return menu.isBlank();
	}

	@Override
	public void resizeUI() {
		menu.resizeUI();
	}

	@Override
	public void setAlignment(Alignment alignment) {
		menu.setAlignment(alignment);
	}

	@Override
	public void setDisabled(boolean disabled) {
		menu.setDisabled(disabled);
	}

	/**
	 * @param menuLoops Whether or not the menu loops around, true by default
	 */
	public void setMenuLoops(boolean menuLoops) {
		menu.setMenuLoops(menuLoops);
	}

	/**
	 * Set a custom interactability filter, for example if you want to allow disabled buttons to still be selectable so that players can see their tooltips.
	 * @param interactabilityFilter The new filter rule: an entry must get 'true' out of this predicate to be considered interactable
	 */
	public void setInteractabilityFilter(Predicate<VisibleInputConsumer> interactabilityFilter) {
		menu.setInteractabilityFilter(interactabilityFilter);
	}

}
