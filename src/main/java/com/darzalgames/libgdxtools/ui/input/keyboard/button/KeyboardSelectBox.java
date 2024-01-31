package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.libgdxtools.ui.input.InputPrioritizer;
import com.darzalgames.libgdxtools.ui.input.scrollablemenu.PopUpMenu;

public class KeyboardSelectBox extends KeyboardButton {

	private PopUpMenu options;
	private Label displayLabel;
	private String defaultEntry;

	public KeyboardSelectBox(Collection<String> entries, TextButton textButton, Consumer<String> action) {
		super(textButton);

		// Make buttons out of all Strings in entries, and so pressing one of these buttons hides the scrollable selectable portion of this select box,
		// sets that as the text in our display label (e.g. English), and calls the Consumer (which responds to the new entry, e.g. changing the game language and refreshing the main menu)
		List<KeyboardButton> entryButtons = entries.stream().map(entry -> UserInterfaceFactory.getButton(entry.toUpperCase(),
				() -> {
					options.hideThis();
					displayLabel.setText(entry);
					action.accept(entry);
				}
				)).toList();
		
		// This is the keyboard scrollable pop up which lists all of the options for the select box, and so handles things like claiming input priority, adding the cancel button, etc.
		this.options = new PopUpMenu(true, entryButtons, "back_message") {
			@Override
			protected void setUpTable() {
				menu.setAlignment(Align.left, Align.left);
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
		this.setButtonRunnable(this::showScrollPane);
		this.setWrap(false);
	}

	public void setSelected(String entry) {
		defaultEntry = entry;
		displayLabel.setText(entry);
		displayLabel.layout();
		TextButton view = this.getView();
		Label label = view.getLabel();
		view.getLabelCell().padRight(3);
		view.setWidth(view.getLabelCell().getPadRight() + label.getWidth() + displayLabel.getPrefWidth());
	}

	public void showScrollPane() {
		InputPrioritizer.claimPriority(options);
		options.goTo(defaultEntry);
	}
}

