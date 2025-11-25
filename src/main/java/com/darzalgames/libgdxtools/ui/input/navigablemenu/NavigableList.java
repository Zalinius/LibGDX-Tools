package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

/**
 * This class is not an Actor, it's the logical list of buttons held in a Table.
 * One of these is owned by a {@link NavigableListMenu}, which is responsible for any decorative elements around this,
 * and any interactions with it.
 */
public class NavigableList implements VisibleInputConsumer {

	protected final LinkedList<VisibleInputConsumer> allEntries;
	protected List<VisibleInputConsumer> interactableEntries;
	private VisibleInputConsumer finalButton;
	protected Table table;

	private final MenuOrientation menuOrientation;
	private final Supplier<Float> spacing;
	private boolean shouldGrowX;

	private boolean pressButtonOnEntryChanged;
	private Alignment entryAlignment;
	private Alignment tableAlignment;
	private Runnable refreshPageRunnable;

	private boolean menuLoops;
	private int currentEntryIndex;
	private VisibleInputConsumer currentButton;

	NavigableList(MenuOrientation menuOrientation, final List<VisibleInputConsumer> entries) {
		allEntries = new LinkedList<>(entries);
		filterInteractableEntities();
		this.menuOrientation = menuOrientation;
		pressButtonOnEntryChanged = false;
		entryAlignment = Alignment.CENTER;
		tableAlignment = Alignment.TOP_LEFT;
		spacing = menuOrientation.getSpacingPolicy();
		setShouldGrowX(false);
		setMenuLoops(true);

		setRefreshPageRunnable(this::defaultRefreshPage);
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
		refreshPage();
	}

	private void filterInteractableEntities() {
		interactableEntries = allEntries.stream().filter(NavigableList::isInteractable).toList();
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
		if (table == null) {
			table = new Table();
		}
		return table;
	}

	public void refreshPage() {
		refreshPageRunnable.run();
	}

	@Override
	public void resizeUI() {
		allEntries.forEach(VisibleInputConsumer::resizeUI);
		if (table != null) {
			allEntries.forEach(entry -> {
				Cell<Actor> cell = table.getCell(entry.getView());
				cell.spaceTop(spacing.get());
				cell.spaceBottom(spacing.get());
			});
			table.invalidate();
			table.layout();
		}
	}

	public void defaultRefreshPage() {
		if (table == null) {
			table = new Table();
		}
		table.clearChildren();
		table.clear();
		table.defaults().expandX().spaceTop(spacing.get()).spaceBottom(spacing.get()).align(entryAlignment.getAlignment());
		if (shouldGrowX) {
			table.defaults().growX();
		}

		if (!isVertical()) {
			table.defaults().expandY();
		}
		table.align(tableAlignment.getAlignment());

		for (VisibleInputConsumer entry : allEntries) {
			if (isVertical()) {
				table.row();
			}
			entry.setAlignment(entryAlignment);
			Actor button = entry.getView();
			table.add(button);
			if (VisibleInputConsumer.isSpacer(entry)) {
				menuOrientation.applySpacerExpansionPolicy(table.getCell(button));
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
		} else {
			if (currentButton != null
					&& (currentEntryIndex < interactableEntries.size() && currentButton != interactableEntries.get(currentEntryIndex))) {
				currentButton.setFocused(false);
			}
			findCurrentButton();
			if (currentButton != null) {
				currentButton.setFocused(true);
			}
		}
	}

	@Override
	public void consumeKeyInput(final Input input) {
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

	public void returnToFirst() {
		goTo(0);
	}

	public void returnToSecondLast() {
		int tryIndex = interactableEntries.size() - 2;
		if (tryIndex >= 0) {
			goTo(tryIndex);
		} else {
			returnToLast();
		}
	}

	public void returnToLast() {
		goTo(interactableEntries.size() - 1);
	}

	public void goTo(final int index) {
		if (currentEntryIndex != index) {
			currentEntryIndex = index;
			changedEntries();
		}

		if (!pressButtonOnEntryChanged) {
			focusCurrent();
		}
	}

	public void goTo(VisibleInputConsumer visibleInputConsumer) {
		for (int i = 0; i < interactableEntries.size(); i++) {
			VisibleInputConsumer entry = interactableEntries.get(i);
			if (entry.equals(visibleInputConsumer)) {
				goTo(i);
			}
		}
	}

	@Override
	public void selectDefault() {
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
		if (table == null) {
			table = new Table();
		}
		table.setTouchable(isTouchable);
		interactableEntries.forEach(entry -> entry.setTouchable(isTouchable));
	}

	/**
	 * False by default, useful if you want to make a tabbed menu
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

	public void setRefreshPageRunnable(Runnable refreshPageRunnable) {
		this.refreshPageRunnable = refreshPageRunnable;
	}

	/**
	 * @param menuLoops Whether or not the menu loops around, true by default
	 */
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
		refreshPageRunnable.run();
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

	@Override
	public float getMinHeight() {
		return table.getMinHeight();
	}

}