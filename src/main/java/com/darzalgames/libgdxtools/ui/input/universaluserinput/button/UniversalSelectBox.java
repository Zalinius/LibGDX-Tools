package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.darzalcommon.functional.Suppliers;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.popup.PopUpMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;

public class UniversalSelectBox extends UniversalButton {

	private final PopUpMenu options;
	private final UniversalLabel mainLabel;
	private final UniversalLabel displayLabel;
	private UniversalButton defaultEntry;
	protected List<UniversalButton> entryButtons;

	public UniversalSelectBox(Supplier<String> textSupplier, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener, TextButtonStyle buttonStyle) {
		super(Runnables.nullRunnable(), inputStrategySwitcher, soundInteractListener, buttonStyle);

		mainLabel = GameInfo.getUserInterfaceFactory().getLabel(textSupplier);
		add(mainLabel).padRight(3);

		// This is the keyboard navigable pop up which lists all of the options for the select box, and so handles things like claiming input priority, adding the cancel button, etc.
		options = new PopUpMenu(true) {
			@Override
			protected void setUpTable() {
				//				menu.replaceContents(entryButtons, makeFinalButton("back_message"));
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
		add(displayLabel);
		setButtonRunnable(() -> InputPriority.claimPriority(options, getView().getStage().getRoot().getName()));
	}

	protected void setEntryButtons(List<UniversalButton> entryButtons) {
		this.entryButtons = entryButtons;
		entryButtons.forEach(entry -> entry.addListener(new ChangeListener() {
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
		//		BasicButton view = getButton();
		//		TextraLabel label = view.getTextraLabel();
		//		view.setWidth(view.getTextraLabelCell().getPadRight() + label.getWidth() + displayLabel.getPrefWidth());
	}

	public void setSelected(String entryText) {
		Optional<UniversalButton> desiredButton = entryButtons.stream().filter(button -> entryText.equalsIgnoreCase(button.getName())).findFirst();
		if (desiredButton.isPresent()) {
			setSelected(desiredButton.get());
		}
	}

	@Override
	public void resizeUI() {
		super.resizeUI();
		defaultEntry.resizeUI(); // Needed for the displayLabel to update when changing language
		//		displayLabel.setTextSupplier(() -> defaultEntry.getButtonText());
		displayLabel.setColor(isOver() ? getStyle().overFontColor : getStyle().fontColor);
		displayLabel.layout();
	}

	@Override
	public boolean isBlank() {
		return false;
	}

	@Override
	public void setAlignment(Alignment alignment) {
		mainLabel.setAlignment(alignment);
		displayLabel.setAlignment(alignment);
	}

}


