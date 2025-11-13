package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.popup.PopUpMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public class KeyboardSelectBox extends KeyboardButton {

	private PopUpMenu options;
	private final Label displayLabel;
	private String defaultEntry;
	private Consumer<String> action;

	// TODO experiment with making a constructor that takes a list of buttons instead of making them?
	public KeyboardSelectBox(Collection<String> entries, TextButton textButton, InputStrategyManager inputStrategyManager, Runnable soundInteractListener) {
		super(textButton, inputStrategyManager, soundInteractListener);

		// Make buttons out of all Strings in entries, and so pressing one of these buttons hides the navigable selectable portion of this select box,
		// sets that as the text in our display label (e.g. English), and calls the Consumer (which responds to the new entry, e.g. changing the game language and refreshing the main menu)
		List<KeyboardButton> entryButtons = entries.stream().map(
				entry -> UserInterfaceFactory.getButton(
						entry,
						() -> {
							options.hideThis();
							setSelected(entry);
							action.accept(entry);
						}
				)
		).collect(Collectors.toList());

		// This is the keyboard navigable pop up which lists all of the options for the select box, and so handles things like claiming input priority, adding the cancel button, etc.
		options = new PopUpMenu(true, entryButtons, "back_message") {
			@Override
			protected void setUpTable() {
				menu.setAlignment(Alignment.LEFT, Alignment.LEFT);
				NinePatchDrawable background = UserInterfaceFactory.getUIBorderedNine();
				this.setBackground(background);
				add(menu.getView()).left();

				float longestWidth = 0;
				for (Iterator<KeyboardButton> iterator = entryButtons.iterator(); iterator.hasNext();) {
					TextButton button = iterator.next().getView();
					if (button.getWidth() > longestWidth) {
						longestWidth = button.getWidth();
					}
				}
				if (menu.getFinalButtonWidth() > longestWidth) {
					longestWidth = menu.getFinalButtonWidth();
				}
				options.setWidth(longestWidth + background.getPatch().getLeftWidth() + background.getPatch().getRightWidth());
				options.setHeight(options.getPrefHeight() + background.getPatch().getBottomHeight() + background.getPatch().getTopHeight());
				options.setPosition(textButton.getRight(), textButton.getTop());
			}
		};

		displayLabel = UserInterfaceFactory.getLabel("");
		displayLabel.setWrap(false);
		textButton.add(displayLabel);
		setButtonRunnable(this::showInnerOptionsPopUpMenu);
		setWrap(false);

		setSelected(entryButtons.get(0).getView().getText().toString());
	}

	/**
	 * Select a button based on the string of the entry, generally used after a choice has been made
	 * or when first setting up the select box to make sure that the currently used value is highlighted (e.g. current language/font/window setting)
	 */
	public void setSelected(String entry) {
		defaultEntry = entry;
		displayLabel.setText(entry);
		displayLabel.layout();
		TextButton view = getView();
		Label label = view.getLabel();
		view.getLabelCell().padRight(3);
		view.setWidth(view.getLabelCell().getPadRight() + label.getWidth() + displayLabel.getPrefWidth());
	}

	private void showInnerOptionsPopUpMenu() {
		InputPriorityManager.claimPriority(options);
		options.goTo(defaultEntry);
	}

	public void setAction(Consumer<String> action) {
		this.action = action;
	}

}
