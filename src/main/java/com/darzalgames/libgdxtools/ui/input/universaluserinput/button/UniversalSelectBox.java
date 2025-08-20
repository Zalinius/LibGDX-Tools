package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.darzalgames.darzalcommon.functional.FunctionalConverter;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.darzalcommon.functional.Suppliers;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.popup.PopUpMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;

public class UniversalSelectBox extends UniversalButton {

	private final PopUpMenu options;
	private final UniversalLabel mainLabel;
	private final UniversalLabel displayLabel;
	private UniversalTextButton defaultEntry;
	protected List<UniversalTextButton> entryButtons;
	private final Cell<UniversalLabel> mainLabelCell;

	public UniversalSelectBox(Supplier<String> textSupplier, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener, TextButtonStyle buttonStyle) {
		super(Runnables.nullRunnable(), inputStrategySwitcher, soundInteractListener, buttonStyle);

		mainLabel = GameInfo.getUserInterfaceFactory().getLabel(textSupplier);
		mainLabelCell = add(mainLabel);

		// This is the keyboard navigable pop up which lists all of the options for the select box, and so handles things like claiming input priority, adding the cancel button, etc.
		options = new PopUpMenu(true) {
			@Override
			protected void setUpTable() {
				List<VisibleInputConsumer> buttonsAsVisibleInputConsumers = FunctionalConverter.convertList(entryButtons);
				menu.replaceContents(buttonsAsVisibleInputConsumers, makeFinalButton("back_message"));
				menu.setAlignment(Alignment.LEFT, Alignment.LEFT);
				menu.getView().setBackground(GameInfo.getUserInterfaceFactory().getDefaultBackgroundDrawable());
				entryButtons.forEach(UniversalTextButton::resizeUI);
				options.add(menu.getView()).left();
				options.pack();
				UserInterfaceSizer.makeActorCentered(options);
			}
			@Override
			protected void setUpDesiredSize() {
				if (getActions().isEmpty()) {
					UserInterfaceSizer.makeActorCentered(options);
					options.pack();
				}
			}

			@Override
			public void gainFocus() {
				super.gainFocus();
				options.goTo(defaultEntry);
			}

		};

		displayLabel = GameInfo.getUserInterfaceFactory().getLabel(Suppliers.emptyString());
		add(displayLabel).growX();
		setButtonRunnable(() -> InputPriority.claimPriority(options, getView().getStage().getRoot().getName()));
		setAlignment(Alignment.LEFT);
	}

	protected void setEntryButtons(List<UniversalTextButton> entryButtons) {
		this.entryButtons = entryButtons;
	}

	public void closeSelectBox() {
		options.hideThis();
	}

	/**
	 * Select a button based on the string of the entry, generally used after a choice has been made
	 * or when first setting up the select box to make sure that the currently used value is highlighted (e.g. current language/font/window setting)
	 * @param entry
	 */
	public void setSelected(UniversalTextButton entry) {
		defaultEntry = entry;
		//		BasicButton view = getButton();
		//		TextraLabel label = view.getTextraLabel();
	}

	public void setSelected(String entryText) {
		Optional<UniversalTextButton> desiredButton = entryButtons.stream().filter(button -> entryText.equalsIgnoreCase(button.getText())).findFirst();
		if (desiredButton.isPresent()) {
			setSelected(desiredButton.get());
		}
	}

	@Override
	public void resizeUI() {

		mainLabel.resizeUI();

		displayLabel.setTextSupplier(() -> defaultEntry.getText()); // Needed for the displayLabel to update when changing language
		displayLabel.resizeUI();
		displayLabel.setColor(isOver() ? getStyle().overFontColor : getStyle().fontColor);
		displayLabel.layout();

		mainLabelCell.padRight(calculatePadding());
		pack();
		super.resizeUI();
	}

	@Override
	public float getPrefWidth() {
		return mainLabel.getWidth() + calculatePadding() + displayLabel.getWidth();
	}

	private float calculatePadding() {
		return UserInterfaceSizer.getWidthPercentage(0.005f);
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


