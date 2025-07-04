package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.*;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.maingame.MultipleStage;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.GetOnStage;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.popup.PopUpMenu;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;


/**
 * The base class for options menus (in-game versus when on the main menu)
 */
public abstract class OptionsMenu extends PopUpMenu {

	protected UniversalButton optionsButton;
	private final Supplier<UniversalButton> makeWindowModeSelectBox;

	private final String platformName;

	protected abstract Alignment getEntryAlignment();
	protected abstract Alignment getMenuAlignment();
	protected abstract String getGameVersion();

	/**
	 * @return A button for players to report bugs (return null if you don't want this)
	 */
	protected abstract UniversalButton makeReportBugButton();

	/**
	 * @return A list of any game-specific options buttons (e.g. language, text speed, etc)
	 */
	protected abstract Collection<UniversalButton> makeMiddleButtons();

	/**
	 * @return An optional button to display controls (return null if you don't want this)
	 */
	protected abstract UniversalButton makeControlsButton();

	/**
	 * @return An optional button that goes right above a quit button (e.g. in-game have a "return to main menu" button)
	 * (return null if you don't want this)
	 */
	protected abstract UniversalButton makeButtonAboveQuitButton();

	/**
	 * @return Make it however you like (return null if you don't want this)
	 */
	protected abstract UniversalButton makeQuitButton();



	/**
	 * @return A PopUp that explains the control schemes
	 */
	protected abstract PopUp makeControlsPopUp();

	protected OptionsMenu(Supplier<UniversalButton> makeWindowModeSelectBox, int bottomPadding) {
		super(true);
		this.makeWindowModeSelectBox = makeWindowModeSelectBox;
		platformName = " (" + GameInfo.getGamePlatform().getPlatformName() + ")";
		defaults().padBottom(bottomPadding);
	}


	@Override
	protected void setUpDesiredSize() {
		UserInterfaceSizer.sizeToPercentage(this, 0.75f, 0.85f);
		if (getActions().isEmpty()) {
			UserInterfaceSizer.makeActorCentered(this);
		}
	}


	/**
	 * To be used by child classes to have buttons in the menu hide/show it. E.g. pressing a button to change the
	 * language does toggleScreenVisibility(false), refreshes the menu, then does toggleScreenVisibility(true).
	 * @param show
	 */
	protected void toggleScreenVisibility(boolean show) {
		if (show) {
			GamePauser.pause();
		} else {
			hideThis();
		}
	}

	@Override
	public void gainFocus() {
		setUpDesiredSize();
		super.gainFocus();
	}

	@Override
	public void regainFocus() {
		focusCurrent();
		optionsButton.setTouchable(Touchable.enabled);
	}

	@Override
	protected void setUpTable() {
		setUpBackground();

		// ALL SELECTABLE MENU BUTTONS
		List<UniversalButton> menuButtons = new ArrayList<>();
		menuButtons.add(GameInfo.getUserInterfaceFactory().getSpacer());

		menuButtons.addAll(makeMiddleButtons());

		UniversalButton reportBugButton = makeReportBugButton();
		if (reportBugButton != null) {
			menuButtons.add(reportBugButton);
		}

		UniversalButton controlsButton = makeControlsButton();
		menuButtons.add(controlsButton);

		// Window mode select box
		UniversalButton windowModeSelectBox = makeWindowModeSelectBox.get();
		menuButtons.add(windowModeSelectBox);

		UniversalButton optionalButtonAboveQuit = makeButtonAboveQuitButton();
		menuButtons.add(optionalButtonAboveQuit);

		// Quit game button
		UniversalButton quitButton = makeQuitButton();
		menuButtons.add(quitButton);

		// Back button
		UniversalButton backButton = makeBackButton();

		menuButtons.add(GameInfo.getUserInterfaceFactory().getSpacer());

		menuButtons.removeIf(Objects::isNull);

		menu.setAlignment(getEntryAlignment(), getMenuAlignment());
		menu.replaceContents(menuButtons, backButton);
		add(menu.getView()).grow().top();


		// Set up the version tag to be in its own inner table, so that the actual buttons can still reach the bottom of the main table
		Table versionTable = new Table();
		versionTable.setFillParent(true);
		versionTable.setTouchable(Touchable.disabled);
		addActor(versionTable);

		Label authors = GameInfo.getUserInterfaceFactory().getFlavorTextLabel(() -> TextSupplier.getLine("authors_label"));
		authors.setAlignment(Align.topLeft);
		versionTable.add(authors).grow().top().left().padTop(getPadTop()).padLeft(getPadLeft());
		versionTable.row();

		Label versionLabel = GameInfo.getUserInterfaceFactory().getFlavorTextLabel(() -> getGameVersion() + platformName);
		versionLabel.setAlignment(Alignment.BOTTOM_RIGHT.getAlignment());
		versionTable.add(versionLabel).bottom().grow().padBottom(getPadBottom()).padRight(getPadRight());
	}

	/**
	 * NOTE: Setting the position here is important, otherwise the options menu will not open!<p>
	 * e.g. call {@link UserInterfaceSizer#makeActorCentered(Actor) UserInterfaceSizer.makeActorCentered(this)}
	 */
	private void setUpBackground() {
		this.setBackground(GameInfo.getUserInterfaceFactory().getDefaultBackgroundDrawable());
		UserInterfaceSizer.makeActorCentered(this);
	}

	private UniversalButton makeBackButton() {
		return GameInfo.getUserInterfaceFactory().getButton(() -> TextSupplier.getLine("back_message"), () -> toggleScreenVisibility(false));
	}

	@Override
	public boolean isGamePausedWhileThisIsInFocus() {
		return true;
	}

	private void positionOptionsButton() {
		float padding = UserInterfaceSizer.getHeightPercentage(0.01f);
		optionsButton.getView().setPosition(padding, UserInterfaceSizer.getCurrentHeight() - optionsButton.getView().getHeight() - padding);
		UserInterfaceSizer.scaleToMinimumPercentage(optionsButton.getView(), 0.08f);
	}

	protected void showOptionsButton(boolean show) {
		optionsButton.setTouchable(show ? Touchable.enabled : Touchable.disabled);
		optionsButton.getView().setVisible(show);
	}

	protected void addOptionsButtonToStage() {
		GetOnStage.addActorToStage(optionsButton.getView(), MultipleStage.OPTIONS_STAGE_NAME);
		optionsButton.getView().toFront();
		positionOptionsButton();
	}

	/**
	 * A sub-menu that opens up within this menu (e.g. a sub-menu for sound options)
	 */
	protected class NestedMenu extends PopUpMenu {

		private final String buttonKey;

		public NestedMenu(final List<UniversalButton> entries, String buttonKey) {
			super(true, entries, "back_message");
			this.buttonKey = buttonKey;
		}

		@Override
		protected void setUpDesiredSize() {
			menu.setSpacing(OptionsMenu.this.menu.getSpacing());
			UserInterfaceSizer.sizeToPercentage(this, 0.5f);
			if (getActions().isEmpty()) {
				UserInterfaceSizer.makeActorCentered(this);
			}
		}

		public UniversalButton getButton() {
			return GameInfo.getUserInterfaceFactory().getButton(() -> TextSupplier.getLine(buttonKey), () -> InputPriority.claimPriority(this, MultipleStage.OPTIONS_STAGE_NAME));
		}

		@Override
		protected void setUpTable() {
			setUpDesiredSize();
			background(GameInfo.getUserInterfaceFactory().getDefaultBackgroundDrawable());

			menu.setAlignment(Alignment.CENTER, Alignment.TOP);
			add(menu.getView()).growX().top();
			UserInterfaceSizer.makeActorCentered(this);
		}
	}
}

