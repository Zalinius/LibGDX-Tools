package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.*;
import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

/**
 * The actor that holds a list of {@link VisibleInputConsumer} and handles how the list looks and is interacted with.
 */
public abstract class NavigableListMenu extends NavigableLayout {

	protected final LinkedList<VisibleInputConsumer> allEntries;

	private final MenuOrientation menuOrientation;
	private boolean shouldGrowX;

	private boolean pressButtonOnEntryChanged;
	private boolean menuLoops;

	private int currentEntryIndex;

	protected NavigableListMenu(MenuOrientation menuOrientation) {
		this(menuOrientation, new LinkedList<>());
	}

	protected NavigableListMenu(MenuOrientation menuOrientation, List<VisibleInputConsumer> entries) {
		allEntries = new LinkedList<>(entries);
		filterInteractableEntities();

		this.menuOrientation = menuOrientation;
		pressButtonOnEntryChanged = false;
		setShouldGrowX(false);
		setMenuLoops(true);
	}

	@Override
	public void populateInnerTableWithButtons(Table innerTable) {
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

		if (finalButton != null) {
			if (isVertical()) {
				innerTable.row();
			}
			Cell<Actor> cell = innerTable.add(finalButton.getView());
			allEntryCells.put(finalButton, cell);
		}
	}

	@Override
	protected Consumer<Cell<Actor>> getSpacingPolicy() {
		return cell -> {
			cell.expandX();
			menuOrientation.getSpacingPolicy().accept(cell);
			if (shouldGrowX) {
				cell.growX();
			}
			if (!isVertical()) {
				// TODO move into menuOrientation
				cell.expandY();
			}
			// hmm this is terrible
			if (VisibleInputConsumer.isSpacer((VisibleInputConsumer) cell.getActor())) {
				menuOrientation.applySpacerExpansionPolicy(cell);
			}
		};
	}

	@Override
	protected void findCurrentButton() {
		if (!interactableEntries.isEmpty()) {
			if (currentEntryIndex >= 0 && currentEntryIndex < interactableEntries.size()) {
				currentButton = interactableEntries.get(currentEntryIndex);
			} else {
				// this menu may only contain unclickable buttons or spacers, or the index tracker went off the rails somehow
				returnToFirst();
			}
		}
	}

	private void changedEntries() {
		if (pressButtonOnEntryChanged) {
			interactableEntries.get(currentEntryIndex).consumeKeyInput(Input.ACCEPT);
		}
		focusCurrent();
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

	@Override
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
	@Override
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
	protected void clearIndices() {
		currentEntryIndex = -1;
	}

	/**
	 * This is generally false, but will be true for menu tabs navigated with the bumpers, for example.
	 * @param pressButtonOnEntryChanged Set whether or not navigating to an entry presses it automatically
	 */
	public void setPressButtonOnEntryChanged(boolean pressButtonOnEntryChanged) {
		this.pressButtonOnEntryChanged = pressButtonOnEntryChanged;
	}

	public void setMenuLoops(boolean menuLoops) {
		this.menuLoops = menuLoops;
	}

	public void setShouldGrowX(boolean shouldGrowX) {
		this.shouldGrowX = shouldGrowX;
	}

	@Override
	protected Collection<VisibleInputConsumer> getAllEntries() {
		// when this class is created, this can be called before anything else is initialized
		if (allEntries != null) {
			return allEntries;
		}
		return new ArrayList<>();
	}

	private boolean isVertical() {
		return MenuOrientation.VERTICAL.equals(menuOrientation);
	}

}
