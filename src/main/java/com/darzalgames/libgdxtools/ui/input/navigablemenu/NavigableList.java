package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.libgdxtools.maingame.MainGame;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

/**
 * This class is not an Actor, it's the logical list of buttons held in a Table.
 * One of these is owned by a {@link NavigableListMenu}, which is responsible for any decorative elements around this,
 * and any interactions with it.
 */
public class NavigableList implements InputConsumer {
	private final Input backCode;	
	private final Input forwardCode;
	protected final LinkedList<UniversalButton> allEntries;
	private LinkedList<UniversalButton> interactableEntries;
	private UniversalButton finalButton;
	private UniversalButton currentButton = null;
	private int currentEntryIndex;
	protected final Table table;
	private boolean isVertical;
	private boolean pressButtonOnEntryChanged;
	private Alignment entryAlignment;
	private Alignment tableAlignment;
	private int spacing = 2;
	private Runnable refreshPageRunnable;
	private boolean menuLoops = true;

	private final List<Consumer<Input>> extraKeyListeners;

	NavigableList(boolean isVertical, final List<UniversalButton> entries) {
		this.backCode = (isVertical ? Input.UP : Input.LEFT);
		this.forwardCode = (isVertical ? Input.DOWN : Input.RIGHT);
		this.allEntries = new LinkedList<>(entries);
		this.interactableEntries = new LinkedList<>(entries);
		this.isVertical = isVertical; 
		this.pressButtonOnEntryChanged = false; 
		this.entryAlignment = Alignment.CENTER;
		this.tableAlignment = Alignment.TOP_LEFT;
		table = new Table();

		extraKeyListeners = new ArrayList<>();
		
		setRefreshPageRunnable(this::defaultRefreshPage);
	}

	public void replaceContents(final List<UniversalButton> newEntries) { 
		replaceContents(newEntries, null);
	}

	/**
	 * @param newEntries The new entries to be held in this list, excluding a special finalButton (see next line). This can include spacers, which will not be interactable
	 * @param finalButton The button that will be pressed when the player presses *back*
	 */
	public void replaceContents(final List<UniversalButton> newEntries, UniversalButton finalButton) {
		allEntries.clear();
		allEntries.addAll(newEntries);
		setFinalButton(finalButton);
		interactableEntries.clear();
		interactableEntries.addAll(allEntries);
		refreshPage();
	}

	protected void setFinalButton(UniversalButton finalButton) {
		this.finalButton = finalButton;
		if (finalButton != null && !finalButton.isBlank()) {
			this.allEntries.add(finalButton);
			this.interactableEntries.add(finalButton);
		}
	}

	public boolean hasFinalButton() {
		return finalButton != null;
	}
	
	public float getFinalButtonWidth() {
		if (hasFinalButton()) {
			return finalButton.getView().getWidth();
		}
		return -1;
	}

	public Table getView() {
		return table;
	}
	
	public void refreshPage() {
		refreshPageRunnable.run();
	}

	public void defaultRefreshPage() {
		table.clearChildren();
		table.clear();
		table.defaults().expandX().spaceTop(spacing).spaceBottom(spacing).align(entryAlignment.getAlignment());

		if (!isVertical()) {
			table.defaults().expandY();
		}
		table.align(tableAlignment.getAlignment());

		for (UniversalButton entry : allEntries) {
			if(isVertical()) {
				table.row();
			}
			entry.setAlignment(entryAlignment);
			Button button = entry.getView();
			table.add(button);
			if (MainGame.getUserInterfaceFactory().isSpacer(entry)) {
				interactableEntries.remove(entry);
				if (isVertical()) {
					table.getCell(button).expandY();
				} else {
					table.getCell(button).expandX();
				}
			}
			if (button.isDisabled()) {
				interactableEntries.remove(entry);				
			}
		}

		if (!pressButtonOnEntryChanged) {
			changedEntries();
		}
	}

	private void findCurrentButton() {
		if (!interactableEntries.isEmpty()) {
			if (currentEntryIndex >= interactableEntries.size() || currentEntryIndex < 0) {
				// this can happen between days when contents are refreshed but this object itself isn't
				currentEntryIndex = 0;
			}
			currentButton = interactableEntries.get(currentEntryIndex);
		} else {
			currentButton = null; // this list is empty, uhhh...? (though it may just contain unclickable buttons or a spacer)
		}
	}

	private void changedEntries() {
		if (pressButtonOnEntryChanged)
		{
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
		if(input.equals(forwardCode)) {
			if (currentEntryIndex < interactableEntries.size() - 1) {
				currentEntryIndex++;
				changedEntries();
			} else if (menuLoops) {
				currentEntryIndex = 0;
				changedEntries();
			}
		}
		else if(input.equals(backCode)) {
			if (currentEntryIndex > 0) {
				currentEntryIndex--;
				changedEntries();
			} else if (menuLoops) {
				currentEntryIndex = interactableEntries.size() - 1;
				changedEntries();
			}
		} else if (input.equals(Input.BACK) && finalButton != null) {
			finalButton.consumeKeyInput(Input.ACCEPT);
		}
		else if (currentButton != null) {
			currentButton.consumeKeyInput(input);
		}

		extraKeyListeners.forEach(listener -> listener.accept(input));
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
			refreshPage();
		}

		if (!pressButtonOnEntryChanged) {
			focusCurrent();
		}
	}

	public void goTo(UniversalButton UniversalButton) {
		for (int i = 0; i < interactableEntries.size(); i++) {
			UniversalButton entry = interactableEntries.get(i);
			if (entry.equals(UniversalButton)) {
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
		interactableEntries.stream().forEach(e->e.setFocused(false));
		currentEntryIndex = -1;
		currentButton = null;
	}

	@Override
	public void setTouchable(Touchable isTouchable) {
		table.setTouchable(isTouchable);
		interactableEntries.forEach(entry -> entry.setTouchable(isTouchable));
	}

	public void setPressButtonOnEntryChanged(boolean pressButtonOnEntryChanged) {
		this.pressButtonOnEntryChanged = pressButtonOnEntryChanged;
	}

	/**
	 * Adjust the vertical spacing between entries as needed (default is 2)
	 * @param spacing new spacing
	 */
	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}
	
	public void setAlignment(Alignment entryAlignment, Alignment tableAlignment) {
		this.entryAlignment = entryAlignment;
		this.tableAlignment = tableAlignment;
	}

	public void addExtraListener(Consumer<Input> listener) {
		extraKeyListeners.add(listener);
	}

	@Override
	public void focusCurrent() {
		interactableEntries.stream().forEach(e->e.setFocused(false));
		findCurrentButton();
		if (currentButton != null) {
			currentButton.setFocused(true);			
		}
	}

	public float getPrefHeight() {
		if(isVertical()) {
			float total = spacing;
			for (UniversalButton entry : allEntries) {
				Button button = entry.getView();
				total += button.getMinHeight();
				total += spacing;
			}
			return total;
		} else {
			return allEntries.get(0).getView().getMinHeight();
		}
	}

	public void setRefreshPageRunnable(Runnable refreshPageRunnable) {
		this.refreshPageRunnable = refreshPageRunnable;
	}

	public void setMenuLoops(boolean menuLoops) {
		this.menuLoops = menuLoops;
	}

	public boolean isVertical() {
		return isVertical;
	}

	public int getSpacing() {
		return spacing;
	}

}