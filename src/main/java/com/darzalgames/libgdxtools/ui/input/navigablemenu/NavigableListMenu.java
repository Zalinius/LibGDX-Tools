package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.*;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Predicate;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

/**
 * The actor that holds a list of {@link VisibleInputConsumer} and handles how the list looks and is interacted with.
 */
public abstract class NavigableListMenu extends NavigableLayout {

	protected final LinkedList<VisibleInputConsumer> allEntries;
	private final Map<VisibleInputConsumer, Cell<Actor>> allEntryCells;
	protected List<VisibleInputConsumer> interactableEntries;
	private Predicate<VisibleInputConsumer> interactabilityFilter;
	private VisibleInputConsumer finalButton;

	private final MenuOrientation menuOrientation;
	private final Supplier<Float> spacing;
	private boolean shouldGrowX;

	private boolean pressButtonOnEntryChanged;
	private Alignment entryAlignment;
	private Alignment tableAlignment;

	private boolean menuLoops;
	private int currentEntryIndex;
	private VisibleInputConsumer currentButton;

	protected NavigableListMenu(MenuOrientation menuOrientation) {
		this(menuOrientation, new LinkedList<>());
	}

	protected NavigableListMenu(MenuOrientation menuOrientation, final List<VisibleInputConsumer> entries) {
		allEntries = new LinkedList<>(entries);
		allEntryCells = new HashMap<>();
		interactabilityFilter = NavigableListMenu::isInteractable;
		filterInteractableEntities();
		this.menuOrientation = menuOrientation;
		pressButtonOnEntryChanged = false;
		entryAlignment = Alignment.CENTER;
		tableAlignment = Alignment.TOP_LEFT;
		spacing = menuOrientation.getSpacingPolicy();
		setShouldGrowX(false);
		setMenuLoops(true);
	}

	/**
	 * Be SUPER SURE to call {@link populateButtons} in your implementation!
	 * And set whatever {@link defaults} modifiers you want before doing so (grow, colspan, etc...).
	 *
	 * Consider also calling things like {@link setAlignment} or {@link replaceContents} BEFORE populating the buttons.
	 *
	 * No need to call {@link clear} first or anything.
	 */
	protected abstract void setUpTable();

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

	public void replaceContents(final List<VisibleInputConsumer> newEntries) {
		replaceContents(newEntries, null);
	}

	/**
	 * @param newEntries  The new entries to be held in this list, excluding a special finalButton (see next line). This can include spacers, which will not be interactable
	 * @param finalButton The button that will be pressed when the player presses *back*
	 */
	public void replaceContents(final List<VisibleInputConsumer> newEntries, VisibleInputConsumer finalButton) {
		allEntries.clear();
		allEntries.addAll(newEntries);
		filterInteractableEntities();
		setFinalButton(finalButton);
	}

	private void filterInteractableEntities() {
		interactableEntries = allEntries.stream().filter(interactabilityFilter::evaluate).toList();
	}

	public void setInteractabilityFilter(Predicate<VisibleInputConsumer> interactabilityFilter) {
		this.interactabilityFilter = interactabilityFilter;
		filterInteractableEntities();
	}

	private static boolean isInteractable(VisibleInputConsumer entry) {
		return !VisibleInputConsumer.isSpacer(entry) && !entry.isDisabled();
	}

	protected void setFinalButton(VisibleInputConsumer finalButton) {
		this.finalButton = finalButton;
		if (finalButton != null && !finalButton.isBlank()) {
			allEntries.add(finalButton);
			filterInteractableEntities();
		}
	}

	@Override
	public Table getView() {
		return this;
	}

	@Override
	public void resizeUI() {
		allEntries.forEach(VisibleInputConsumer::resizeUI);
		allEntries.forEach(entry -> {
			Cell<Actor> cell = allEntryCells.get(entry);
			cell.spaceTop(spacing.get());
			cell.spaceBottom(spacing.get());
		});
		invalidate();
		layout();
	}

	public void populateButtons() {
		Table innerTable = new Table();
		add(innerTable);
		allEntryCells.clear();

		innerTable.defaults().expandX().spaceTop(spacing.get()).spaceBottom(spacing.get()).align(entryAlignment.getAlignment());
		if (shouldGrowX) {
			innerTable.defaults().growX();
		}
		if (!isVertical()) {
			innerTable.defaults().expandY();
		}
		innerTable.align(tableAlignment.getAlignment());

		for (VisibleInputConsumer entry : allEntries) {
			if (isVertical()) {
				innerTable.row();
			}
			entry.setAlignment(entryAlignment);
			Actor button = entry.getView();
			Cell<Actor> cell = innerTable.add(button);
			allEntryCells.put(entry, cell);
			if (VisibleInputConsumer.isSpacer(entry)) {
				menuOrientation.applySpacerExpansionPolicy(cell);
			}
		}
	}

	private void findCurrentButton() {
		if (!interactableEntries.isEmpty()) {
			if (currentEntryIndex >= interactableEntries.size() || currentEntryIndex < 0) {
				// this can happen between days in Quest Giver when contents are refreshed but this object itself isn't
				currentEntryIndex = 0;
			}
			currentButton = interactableEntries.get(currentEntryIndex);
		} else {
			currentButton = null; // this list is empty, uhhh...? (though it may just contain unclickable buttons or a spacer)
		}
	}

	private void changedEntries() {
		if (pressButtonOnEntryChanged) {
			interactableEntries.get(currentEntryIndex).consumeKeyInput(Input.ACCEPT);
		}

		if (currentButton != null
				&& (currentEntryIndex < interactableEntries.size() && currentButton != interactableEntries.get(currentEntryIndex))) {
			currentButton.setFocused(false);
		}
		findCurrentButton();
		if (currentButton != null) {
			currentButton.setFocused(true);
		}
	}

	@Override
	public void consumeKeyInput(Input input) {
		if (input.equals(menuOrientation.getForwardCode())) {
			if (currentEntryIndex < interactableEntries.size() - 1) {
				currentEntryIndex++;
				changedEntries();
			} else if (menuLoops) {
				currentEntryIndex = 0;
				changedEntries();
			}
		} else if (input.equals(menuOrientation.getBackCode())) {
			if (currentEntryIndex > 0) {
				currentEntryIndex--;
				changedEntries();
			} else if (menuLoops) {
				currentEntryIndex = interactableEntries.size() - 1;
				changedEntries();
			}
		} else if (input.equals(Input.BACK) && finalButton != null) {
			finalButton.consumeKeyInput(Input.ACCEPT);
		} else if (currentButton != null) {
			currentButton.consumeKeyInput(input);
		}
	}

	/**
	 * @param input the Input to query
	 * @return whether or not this menu can make use of the input in a meaningful way
	 */
	public boolean canUseInput(Input input) {
		if (input.equals(menuOrientation.getForwardCode())) {
			if (currentEntryIndex < interactableEntries.size() - 1) {
				return true;
			}
			return menuLoops;
		} else if (input.equals(menuOrientation.getBackCode())) {
			if (currentEntryIndex > 0) {
				return true;
			}
			return menuLoops;
		} else if (input.equals(Input.BACK) && finalButton != null) {
			return true;
		} else if (input.equals(Input.ACCEPT) && currentButton != null) {
			return true;
		}
		return false;
	}

	protected boolean returnToFirst() {
		return goTo(0);
	}

	protected boolean returnToSecondLast() {
		int tryIndex = interactableEntries.size() - 2;
		if (tryIndex >= 0) {
			return goTo(tryIndex);
		} else {
			return returnToLast();
		}
	}

	protected boolean returnToLast() {
		return goTo(interactableEntries.size() - 1);
	}

	private boolean goTo(final int index) {
		boolean changedEntry = false;
		if (currentEntryIndex != index) {
			currentEntryIndex = index;
			changedEntries();

			changedEntry = true;
		}

		if (!pressButtonOnEntryChanged) {
			focusCurrent();
		}
		return changedEntry;
	}

	/**
	 * Set the focus to a particular {@link VisibleInputConsumer}
	 * @param visibleInputConsumer the input consumer to focus on
	 * @return Whether or not this menu has that entry (and if so, then it was selected). Returns false if that entry was already the current one.
	 */
	public boolean goTo(VisibleInputConsumer visibleInputConsumer) {
		for (int i = 0; i < interactableEntries.size(); i++) {
			VisibleInputConsumer entry = interactableEntries.get(i);
			if (entry.equals(visibleInputConsumer)) {
				return goTo(i);
			}
		}
		return false;
	}

	@Override
	public void selectDefault() {
		clearSelected();
		returnToFirst();
	}

	@Override
	public void clearSelected() {
		interactableEntries.stream().forEach(e -> e.setFocused(false));
		currentEntryIndex = -1;
		currentButton = null;
	}

	@Override
	public void setTouchable(Touchable isTouchable) {
		super.setTouchable(isTouchable);
		if (interactableEntries != null) {
			// when this class is instantiated, creating itself as a Table calls setTouchable() before anything else is initialized
			interactableEntries.forEach(entry -> entry.setTouchable(isTouchable));
		}
	}

	/**
	 * This is generally false, but will be true for menu tabs navigated with the bumpers, for example.
	 * @param pressButtonOnEntryChanged Set whether or not navigating to an entry presses it automatically
	 */
	public void setPressButtonOnEntryChanged(boolean pressButtonOnEntryChanged) {
		this.pressButtonOnEntryChanged = pressButtonOnEntryChanged;
	}

	@Override
	public void focusCurrent() {
		interactableEntries.stream().forEach(e -> e.setFocused(false));
		findCurrentButton();
		if (currentButton != null) {
			currentButton.setFocused(true);
		}
	}

	public void setMenuLoops(boolean menuLoops) {
		this.menuLoops = menuLoops;
	}

	public void setShouldGrowX(boolean shouldGrowX) {
		this.shouldGrowX = shouldGrowX;
	}

	private boolean isVertical() {
		return MenuOrientation.VERTICAL.equals(menuOrientation);
	}

	@Override
	public boolean isBlank() {
		return allEntries.isEmpty();
	}

	/**
	 * Sets the alignment for all entries AND the table itself, use {@link #setAlignment(Alignment entryAlignment, Alignment tableAlignment)} to set them separately
	 */
	@Override
	public void setAlignment(Alignment alignment) {
		setAlignment(alignment, alignment);
	}

	public void setAlignment(Alignment entryAlignment, Alignment tableAlignment) {
		this.entryAlignment = entryAlignment;
		this.tableAlignment = tableAlignment;
	}

	@Override
	public boolean isDisabled() {
		return interactableEntries.stream().allMatch(VisibleInputConsumer::isDisabled);
	}

	@Override
	public void setDisabled(boolean disabled) {
		allEntries.forEach(entry -> entry.setDisabled(disabled));
		filterInteractableEntities();
	}

	@Override
	public boolean isOver() {
		return interactableEntries.stream().anyMatch(VisibleInputConsumer::isOver);
	}

	/**
	 * @return The entry that's currently in focus
	 */
	public VisibleInputConsumer getCurrentButton() {
		return currentButton;
	}

}
