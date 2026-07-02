package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.darzalgames.darzalcommon.data.GenericInheritanceConverter;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.MenuOrientation;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListPopUpMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.zalaudiolibrary.sfx.SoundEffect;

public class UniversalSelectBox extends UniversalTextButton {

	private final NavigableListPopUpMenu options;
	private UniversalTextButton defaultEntry;
	protected List<UniversalTextButton> entryButtons;

	public UniversalSelectBox(String mainLabelKey, InputStrategySwitcher inputStrategySwitcher, ButtonStyle buttonStyle, Consumer<SoundEffect> soundEffectConsumer, SoundEffect soundEffect) {
		super(GameInfo.getUserInterfaceFactory().getLabel(() -> TextSupplier.getLine(mainLabelKey)), Runnables.nullRunnable(), inputStrategySwitcher, buttonStyle, soundEffectConsumer, soundEffect);

		options = new OptionsPopUp();

		setButtonRunnable(() -> InputPriority.claimPriority(options, getView().getStage().getRoot().getName()));
		setAlignment(Alignment.LEFT);
	}

	protected void setEntryButtons(List<UniversalTextButton> entryButtons) {
		this.entryButtons = entryButtons;
	}

	public void closeSelectBox() {
		options.hideThis();
	}

	public void setSelected(UniversalTextButton entry) {
		defaultEntry = entry;
	}

	/**
	 * Select a button based on the string of the entry, generally used after a choice has been made
	 * or when first setting up the select box to make sure that the currently used value is highlighted (e.g. current locale/font/window setting)
	 * @param entryText the selected button's text
	 */
	public void setSelected(String entryText) {
		Optional<UniversalTextButton> desiredButton = entryButtons.stream().filter(button -> entryText.equalsIgnoreCase(button.getText())).findFirst();
		if (desiredButton.isPresent()) {
			setSelected(desiredButton.get());
		}
	}

	/**
	 * This is the keyboard navigable pop up which lists all of the options for the select box, and so handles things like claiming input priority, adding the cancel button, etc.
	 */
	private class OptionsPopUp extends NavigableListPopUpMenu {

		protected OptionsPopUp() {
			super(MenuOrientation.VERTICAL);
		}

		@Override
		protected void setUpTable() {
			UniversalLabel currentlySelectedIndicator = GameInfo.getUserInterfaceFactory().getFlavorTextLabel(() -> TextSupplier.getLine("select_box_current", defaultEntry.label.textSupplier.get()));
			add(currentlySelectedIndicator).center();
			row();
			List<VisibleInputConsumer> buttonsAsVisibleInputConsumers = GenericInheritanceConverter.convertList(entryButtons);
			replaceContents(buttonsAsVisibleInputConsumers, makeDefaultBackButton());
			setAlignment(Alignment.CENTER, Alignment.CENTER);
			setBackground(GameInfo.getUserInterfaceFactory().getCompactBackgroundDrawable());
			entryButtons.forEach(UniversalTextButton::resizeUI);
			defaults().grow().center();
			populateButtons();
			pack();
			UserInterfaceSizer.makeActorCentered(options);
		}

		@Override
		public void setUpDesiredSize() {
			pack();
		}

		@Override
		public void gainFocus() {
			super.gainFocus();
			goTo(defaultEntry);
		}
	}

}
