package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.darzalgames.darzalcommon.functional.Suppliers;
import com.darzalgames.libgdxtools.maingame.MainGame;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.popup.PopUpMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class UniversalSelectBox extends UniversalButton {

	private PopUpMenu options;
	private final UniversalLabel displayLabel;
	private Supplier<String> defaultEntry;
	private Consumer<String> action;

	public UniversalSelectBox(Collection<Supplier<String>> entries, BasicButton textButton, Supplier<String> textSupplier, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(textButton, textSupplier, inputStrategySwitcher, soundInteractListener);

		// Make buttons out of all Strings in entries, and so pressing one of these buttons hides the navigable selectable portion of this select box,
		// sets that as the text in our display label (e.g. English), and calls the Consumer (which responds to the new entry, e.g. changing the game language and refreshing the main menu)
		List<UniversalButton> entryButtons = entries.stream().map(entry -> MainGame.getUserInterfaceFactory().getButton(
				entry,
				() -> {
					options.hideThis();
					setSelected(entry);
					action.accept(entry.get());
				}
				)).toList();

		// This is the keyboard navigable pop up which lists all of the options for the select box, and so handles things like claiming input priority, adding the cancel button, etc.
		options = new PopUpMenu(true, entryButtons, "back_message") {
			@Override
			protected void setUpTable() {
				menu.setAlignment(Alignment.LEFT, Alignment.LEFT);
				menu.getView().setBackground(MainGame.getUserInterfaceFactory().getDefaultBackgroundDrawable());
				options.add(menu.getView()).left();
				options.pack();
				UserInterfaceSizer.makeActorCentered(options);
			}
			@Override
			protected void setUpDesiredSize() {
				if (getActions().isEmpty()) {
					UserInterfaceSizer.makeActorCentered(options);
				}
			}
		};

		displayLabel = MainGame.getUserInterfaceFactory().getLabel(Suppliers.emptyString());
		displayLabel.setWrap(false);
		textButton.add(displayLabel);
		setButtonRunnable(this::showInnerOptionsPopUpMenu);
		setWrap(false);

		setSelected(() -> entryButtons.get(0).getButton().getButtonText());
	}

	/**
	 * Select a button based on the string of the entry, generally used after a choice has been made
	 * or when first setting up the select box to make sure that the currently used value is highlighted (e.g. current language/font/window setting)
	 * @param entry
	 */
	public void setSelected(Supplier<String> entry) {
		defaultEntry = entry;
		BasicButton view = getButton();
		Label label = view.getLabel();
		view.getLabelCell().padRight(3);
		view.setWidth(view.getLabelCell().getPadRight() + label.getWidth() + displayLabel.getPrefWidth());
	}

	private void showInnerOptionsPopUpMenu() {
		InputPriority.claimPriority(options);
		options.goTo(defaultEntry.get());
	}

	public void setAction(Consumer<String> action) {
		this.action = action;
	}

	@Override
	public void resizeUI() {
		super.resizeUI();
		displayLabel.setTextSupplier(defaultEntry);
		BasicButton thisButton = getButton();
		displayLabel.setColor(thisButton.isOver() ? thisButton.getStyle().overFontColor : thisButton.getStyle().fontColor);
		displayLabel.layout();
	}

}


