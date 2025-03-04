package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.darzalgames.libgdxtools.maingame.MainGame;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.popup.PopUpMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class UniversalSelectBox extends UniversalButton {

	private PopUpMenu options;
	private Label displayLabel;
	private String defaultEntry;
	private Consumer<String> action;

	public UniversalSelectBox(Collection<String> entries, TextButton textButton, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(textButton, inputStrategySwitcher, soundInteractListener);

		// Make buttons out of all Strings in entries, and so pressing one of these buttons hides the navigable selectable portion of this select box,
		// sets that as the text in our display label (e.g. English), and calls the Consumer (which responds to the new entry, e.g. changing the game language and refreshing the main menu)
		List<UniversalButton> entryButtons = entries.stream().map(entry -> MainGame.getUserInterfaceFactory().getButton(entry,
				() -> {
					options.hideThis();
					setSelected(entry);
					action.accept(entry);
				}
				)).collect(Collectors.toList());
		
		// This is the keyboard navigable pop up which lists all of the options for the select box, and so handles things like claiming input priority, adding the cancel button, etc.
		this.options = new PopUpMenu(true, entryButtons, "back_message") {
			@Override
			protected void setUpTable() {
				menu.setAlignment(Alignment.LEFT, Alignment.LEFT);
				menu.getView().setBackground(MainGame.getUserInterfaceFactory().getUIBorderedNine());
				options.add(menu.getView()).left();
				options.pack();
				UserInterfaceSizer.makeActorCentered(options);
			}
			@Override
			protected void setUpDesiredSize() {
				if (this.getActions().isEmpty()) {
					UserInterfaceSizer.makeActorCentered(options);
				}
			}
		};

		displayLabel = MainGame.getUserInterfaceFactory().getLabel("");
		displayLabel.setWrap(false);
		textButton.add(displayLabel);
		this.setButtonRunnable(this::showInnerOptionsPopUpMenu);
		this.setWrap(false);
		
		setSelected(entryButtons.get(0).getView().getText().toString());
	}

	/**
	 * Select a button based on the string of the entry, generally used after a choice has been made
	 * or when first setting up the select box to make sure that the currently used value is highlighted (e.g. current language/font/window setting)
	 * @param entry
	 */
	public void setSelected(String entry) {
		defaultEntry = entry;
		displayLabel.setText(entry);
		displayLabel.layout();
		TextButton view = this.getView();
		Label label = view.getLabel();
		view.getLabelCell().padRight(3);
		view.setWidth(view.getLabelCell().getPadRight() + label.getWidth() + displayLabel.getPrefWidth());
	}

	private void showInnerOptionsPopUpMenu() {
		InputPriority.claimPriority(options);
		options.goTo(defaultEntry);
	}

	public void setAction(Consumer<String> action) {
		this.action = action;
	}
	
}


