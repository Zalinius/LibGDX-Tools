package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

/**
 * The actor that holds a {@link NavigableList} and handles how it looks and is interacted with.
 */
public abstract class NavigableListMenu extends Table implements InputConsumer {
	protected NavigableList menu;

	protected NavigableListMenu(boolean isVertical) {
		this(isVertical, new LinkedList<>());
	}

	protected NavigableListMenu(boolean isVertical, List<VisibleInputConsumer> entries) {
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

	/**
	 * Set the focus to a particular {@link VisibleInputConsumer}
	 * @param entry
	 */
	public void goTo(VisibleInputConsumer entry) {
		menu.goTo(entry);
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

}
