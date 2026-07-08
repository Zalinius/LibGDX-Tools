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
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.MenuOrientation;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListPopUpMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.zalaudiolibrary.sfx.SoundEffect;

public class UniversalSelectBox extends UniversalTextButton {

	private NavigableListPopUpMenu options;
	private UniversalTextButton defaultEntry;
	protected List<UniversalTextButton> entryButtons;

	public UniversalSelectBox(String mainLabelKey, InputStrategySwitcher inputStrategySwitcher, ButtonStyle buttonStyle, Consumer<SoundEffect> soundEffectConsumer, SoundEffect soundEffect, ControlsGlyph controlsGlyph) {
		super(
				GameInfo.getUserInterfaceFactory().getLabel(() -> TextSupplier.getLine(mainLabelKey)), Runnables.nullRunnable(), inputStrategySwitcher, buttonStyle,
				soundEffectConsumer, soundEffect, controlsGlyph
		);

		setButtonRunnable(this::showOptions);
		setAlignment(Alignment.LEFT);
	}

	private void showOptions() {
		options = new OptionsPopUp();
		InputPriority.claimPriority(options, getView().getStage().getRoot().getName());
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
			super(MenuOrientation.VERTICAL, GenericInheritanceConverter.convertList(entryButtons), true);
		}

		@Override
		protected void setUpTable() {
			setAlignment(Alignment.CENTER, Alignment.CENTER);
			setBackground(GameInfo.getUserInterfaceFactory().getCompactBackgroundDrawable());
			defaults().center();
			populateButtons();
			UserInterfaceSizer.makeActorCentered(options);
		}

		@Override
		public void setUpDesiredSize() {
			setSize(UserInterfaceSizer.getWidthPercentage(0.25f), UserInterfaceSizer.getHeightPercentage(0.45f));
		}

		@Override
		public void gainFocus() {
			super.gainFocus();
			goTo(defaultEntry);
		}
	}

}
