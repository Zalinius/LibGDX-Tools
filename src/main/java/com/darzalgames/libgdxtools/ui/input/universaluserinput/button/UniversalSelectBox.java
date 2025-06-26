package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.darzalgames.darzalcommon.functional.Suppliers;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.popup.PopUpMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class UniversalSelectBox extends UniversalButton {

	private final PopUpMenu options;
	private final UniversalLabel displayLabel;
	private UniversalButton defaultEntry;
	protected List<UniversalButton> entryButtons;

	public UniversalSelectBox(BasicButton textButton, Supplier<String> textSupplier, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(textButton, textSupplier, inputStrategySwitcher, soundInteractListener);

		// This is the keyboard navigable pop up which lists all of the options for the select box, and so handles things like claiming input priority, adding the cancel button, etc.
		options = new PopUpMenu(true) {
			@Override
			protected void setUpTable() {
				menu.replaceContents(entryButtons, makeFinalButton("back_message"));
				menu.setAlignment(Alignment.LEFT, Alignment.LEFT);
				menu.getView().setBackground(GameInfo.getUserInterfaceFactory().getDefaultBackgroundDrawable());
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

			@Override
			public void gainFocus() {
				super.gainFocus();
				options.goTo(defaultEntry);
			}

		};

		displayLabel = GameInfo.getUserInterfaceFactory().getLabel(Suppliers.emptyString());
		displayLabel.setWrap(false);
		textButton.add(displayLabel);
		setButtonRunnable(() -> InputPriority.claimPriority(options, getView().getStage().getRoot().getName()));
		setWrap(false);
	}

	protected void setEntryButtons(List<UniversalButton> entryButtons) {
		this.entryButtons = entryButtons;
		entryButtons.forEach(entry -> entry.getButton().addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				setSelected(entry);
				options.hideThis();
			}
		}));
	}

	/**
	 * Select a button based on the string of the entry, generally used after a choice has been made
	 * or when first setting up the select box to make sure that the currently used value is highlighted (e.g. current language/font/window setting)
	 * @param entry
	 */
	public void setSelected(UniversalButton entry) {
		defaultEntry = entry;
		BasicButton view = getButton();
		Label label = view.getLabel();
		view.getLabelCell().padRight(3);
		view.setWidth(view.getLabelCell().getPadRight() + label.getWidth() + displayLabel.getPrefWidth());
	}

	public void setSelected(String entryText) {
		Optional<UniversalButton> desiredButton = entryButtons.stream().filter(button -> entryText.equalsIgnoreCase(button.getButton().getButtonText())).findFirst();
		if (desiredButton.isPresent()) {
			setSelected(desiredButton.get());
		}
	}

	@Override
	public void resizeUI() {
		super.resizeUI();
		defaultEntry.resizeUI(); // Needed for the displayLabel to update when changing language
		displayLabel.setTextSupplier(() -> defaultEntry.getButton().getButtonText());
		BasicButton thisButton = getButton();
		displayLabel.setColor(thisButton.isOver() ? thisButton.getStyle().overFontColor : thisButton.getStyle().fontColor);
		displayLabel.layout();
	}

}


