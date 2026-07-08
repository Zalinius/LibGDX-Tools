package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

/**
 * A {@link Table} and a {@link VisibleInputConsumer}, the ideal base for a menu
 */
public abstract class NavigableLayout extends Table implements VisibleInputConsumer {

	private Predicate<VisibleInputConsumer> interactabilityFilter;
	protected final Map<VisibleInputConsumer, Cell<Actor>> allEntryCells;

	protected VisibleInputConsumer finalButton;
	protected VisibleInputConsumer currentButton;

	protected Alignment entryAlignment;
	protected Alignment tableAlignment;
	private final ScrollPane scrollPane;

	protected NavigableLayout() {
		interactabilityFilter = NavigableLayout::isInteractable;
		allEntryCells = new HashMap<>();

		currentButton = null;
		setAlignment(Alignment.CENTER, Alignment.TOP_LEFT);

		scrollPane = new ScrollPane(new Actor());
	}

	/**
	 * Set the focus to a particular {@link VisibleInputConsumer}
	 * @param visibleInputConsumer the input consumer to focus on
	 * @return Whether or not this menu has that entry (and if so, then it was selected). Returns false if that entry was already the current one.
	 */
	public abstract boolean goTo(VisibleInputConsumer visibleInputConsumer);

	/**
	 * Be SUPER SURE to call {@link populateButtons} in your implementation!
	 * And set whatever {@link defaults} modifiers you want before doing so (grow, colspan, etc...).
	 *
	 * Consider also calling things like {@link setAlignment} or {@link replaceContents} BEFORE populating the buttons.
	 *
	 * No need to call {@link clear} first or anything.
	 */
	protected abstract void setUpTable();

	protected abstract boolean returnToFirst();

	protected abstract Collection<VisibleInputConsumer> getAllEntries();

	protected abstract void findCurrentButton();

	protected abstract Consumer<Cell<Actor>> getSpacingPolicy();

	protected abstract void populateInnerTableWithButtons(Table innerTable);

	protected abstract void consumeInput(Input input);

	/**
	 * When clearing the selected button, clear any indices used to track positioning
	 */
	protected abstract void clearIndices();

	protected void setInteractabilityFilter(Predicate<VisibleInputConsumer> interactabilityFilter) {
		this.interactabilityFilter = interactabilityFilter;
		filterInteractableEntries();
	}

	protected List<VisibleInputConsumer> filterInteractableEntries() {
		return getAllEntriesPlusFinalButton().stream().filter(interactabilityFilter::test).toList();
	}

	private static boolean isInteractable(VisibleInputConsumer entry) {
		return !VisibleInputConsumer.isSpacer(entry) && !entry.isDisabled();
	}

	@Override
	public void gainFocus() {
		clear();
		setUpTable();
		selectDefault();
	}

	@Override
	public void regainFocus() {
		focusCurrent();
	}

	@Override
	public void focusCurrent() {
		getAllEntriesPlusFinalButton().stream().forEach(e -> e.setFocused(false));
		findCurrentButton();
		if (currentButton != null) {
			// an empty list doesn't find a button :(
			currentButton.setFocused(true);
		}
	}

	@Override
	public void selectDefault() {
		clearSelected();
		returnToFirst();
	}

	@Override
	public void resizeUI() {
		getAllEntriesPlusFinalButton().forEach(entry -> {
			Cell<Actor> cell = allEntryCells.get(entry);
			getSpacingPolicy().accept(cell);
			entry.resizeUI();
		});
		invalidate();
		layout();
	}

	public final void populateButtons() {
		allEntryCells.clear();

		Table innerTable = new Table();
		scrollPane.setActor(innerTable);
		add(scrollPane);
//			scrollPane.addAction(Actions.forever(Actions.run(() -> System.out.println(scrollPane.getHeight()))));

		// scrollY must be set every frame to some fraction of the screen height (for resizing)
//			play with overscroll distance?

		innerTable.align(tableAlignment.getAlignment());
		innerTable.defaults().align(entryAlignment.getAlignment());
		getSpacingPolicy().accept(innerTable.defaults());

		populateInnerTableWithButtons(innerTable);
		resizeUI();
	}

	@Override
	public final void consumeKeyInput(Input input) {
		consumeInput(input);
		if (currentButton != null) { // can be null when exiting a menu
			float scrollY = currentButton.getView().getTop();
			scrollPane.scrollTo(scrollPane.getScrollX(), scrollY, scrollPane.getWidth(), scrollPane.getHeight());
		}
	}

	/**
	 * @param newEntries The new entries to be held in this list, excluding a special finalButton (see next line). This can include spacers, which will not be interactable
	 */
	public void replaceContents(final List<VisibleInputConsumer> newEntries) {
		replaceContents(newEntries, null);
	}

	/**
	 * @param newEntries  The new entries to be held in this list, excluding a special finalButton (see next line). This can include spacers, which will not be interactable
	 * @param finalButton The button that will be pressed when the player presses *back*
	 */
	public void replaceContents(final List<VisibleInputConsumer> newEntries, VisibleInputConsumer finalButton) {
		getAllEntries().clear();
		getAllEntries().addAll(newEntries);
		setFinalButton(finalButton);
	}

	public void setFinalButton(VisibleInputConsumer finalButton) {
		if (finalButton != null && !finalButton.isBlank()) {
			this.finalButton = finalButton;
			filterInteractableEntries();
		}
	}

	public boolean hasFinalButton() {
		return finalButton != null;
	}

	@Override
	public Table getView() {
		return this;
	}

	/**
	 * @return The entry that's currently in focus
	 */
	public VisibleInputConsumer getCurrentButton() {
		return currentButton;
	}

	@Override
	public void setTouchable(Touchable isTouchable) {
		super.setTouchable(isTouchable);
		getAllEntriesPlusFinalButton().forEach(entry -> entry.setTouchable(isTouchable));
	}

	/**
	 * Sets the alignment for all entries AND the table itself, use {@link #setAlignment(Alignment entryAlignment, Alignment tableAlignment)} to set them separately
	 */
	@Override
	public void setAlignment(Alignment alignment) {
		setAlignment(alignment, alignment);
	}

	/**
	 * Sets the alignment for all entries and the table separately
	 */
	public void setAlignment(Alignment entryAlignment, Alignment tableAlignment) {
		this.entryAlignment = entryAlignment;
		this.tableAlignment = tableAlignment;
	}

	@Override
	public boolean isDisabled() {
		return getAllEntries().stream().allMatch(VisibleInputConsumer::isDisabled)
				&& (finalButton == null || finalButton.isDisabled());
	}

	@Override
	public boolean isBlank() {
		return getAllEntries().stream().allMatch(VisibleInputConsumer::isBlank)
				&& finalButton == null;
	}

	@Override
	public void setDisabled(boolean disabled) {
		getAllEntriesPlusFinalButton().stream().forEach(entry -> entry.setDisabled(disabled));
		filterInteractableEntries();
	}

	@Override
	public final void clearSelected() {
		getAllEntriesPlusFinalButton().forEach(e -> e.setFocused(false));
		clearIndices();
		currentButton = null;
	}

	private List<VisibleInputConsumer> getAllEntriesPlusFinalButton() {
		List<VisibleInputConsumer> allPlusFinal = new ArrayList<>();
		if (getAllEntries() != null) {
			// when this class is created, this can be called before anything else is initialized
			allPlusFinal.addAll(getAllEntries());
		}
		if (hasFinalButton()) {
			allPlusFinal.add(finalButton);
		}
		return allPlusFinal;
	}

}