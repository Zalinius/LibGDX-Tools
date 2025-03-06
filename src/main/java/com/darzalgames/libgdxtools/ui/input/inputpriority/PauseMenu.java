package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.*;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.maingame.MainGame;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.popup.PopUpMenu;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;


/**
 * The base class for pause menus (in-game versus when on the main menu)
 */
public abstract class PauseMenu extends PopUpMenu implements DoesNotPause {

	protected UniversalButton pauseButton;
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
	 * @return Make it however you like, a default will be provided otherwise
	 */
	protected UniversalButton makeBackButton() {
		return MainGame.getUserInterfaceFactory().getButton(TextSupplier.getLine("back_message"), () -> toggleScreenVisibility(false));
	}

	/**
	 * @return A PopUp that explains the control schemes
	 */
	protected abstract PopUp makeControlsPopUp();

	protected PauseMenu(Supplier<UniversalButton> makeWindowModeSelectBox, int bottomPadding) {
		super(true);
		this.makeWindowModeSelectBox = makeWindowModeSelectBox;
		this.platformName = " (" + GameInfo.getGamePlatform().getPlatformName() + ")";
		setBounds(0, 0, UserInterfaceSizer.getCurrentWidth(), UserInterfaceSizer.getCurrentHeight());
		defaults().padBottom(bottomPadding);
	}

	/**
	 * NOTE: Setting the position here is important, otherwise the pause menu will not open!<p>
	 * e.g. call {@link UserInterfaceSizer#makeActorCentered(Actor) UserInterfaceSizer.makeActorCentered(this)} 
	 */
	protected void setUpBackground() {
		this.setBackground(MainGame.getUserInterfaceFactory().getUIBorderedNine());
		UserInterfaceSizer.makeActorCentered(this);
	}

	@Override
	protected void setUpDesiredSize() {
		UserInterfaceSizer.sizeSquareActorToPercentage(this, 0.75f);
		if (this.getActions().isEmpty()) {
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
		UserInterfaceSizer.makeActorCentered(this);
		super.gainFocus();
	}

	@Override
	public void regainFocus() {
		menu.refreshPage();
		focusCurrent();
		pauseButton.setTouchable(Touchable.enabled);
	}

	@Override
	protected void setUpTable() {
		setUpBackground();

		// ALL SELECTABLE MENU BUTTONS
		List<UniversalButton> menuButtons = new ArrayList<>();
		menuButtons.add(MainGame.getUserInterfaceFactory().getSpacer());

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

		menuButtons.add(MainGame.getUserInterfaceFactory().getSpacer());

		menuButtons.removeIf(Objects::isNull);

		menu.setAlignment(getEntryAlignment(), getMenuAlignment());
		menu.replaceContents(menuButtons, backButton);
		add(menu.getView()).grow().top();

		
		// Set up the version tag to be in its own inner table, so that the actual buttons can still reach the bottom of the main table
		Table versionTable = new Table();
		versionTable.setFillParent(true);
		versionTable.setTouchable(Touchable.disabled);
		addActor(versionTable);
		
		Label authors = MainGame.getUserInterfaceFactory().getFlavorTextLabel(TextSupplier.getLine("authors_label"));
		authors.setAlignment(Align.topLeft);
		versionTable.add(authors).grow().top().left().padTop(getPadTop()).padLeft(getPadLeft());
		versionTable.row();
		
		Label versionLabel = MainGame.getUserInterfaceFactory().getFlavorTextLabel(getGameVersion() + platformName);
		versionLabel.setAlignment(Alignment.BOTTOM_RIGHT.getAlignment());
		versionTable.add(versionLabel).bottom().grow().padBottom(getPadBottom()).padRight(getPadRight());
	}

	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}

	@Override
	public boolean isGamePausedWhileThisIsInFocus() {
		return true;
	}

	private void positionPauseButton() {
		float padding = UserInterfaceSizer.getHeightPercentage(0.01f);
		pauseButton.getView().setPosition(padding, UserInterfaceSizer.getCurrentHeight() - pauseButton.getView().getHeight() - padding);

		float size = Math.min(UserInterfaceSizer.getWidthPercentage(0.08f), UserInterfaceSizer.getHeightPercentage(0.08f));
		pauseButton.getView().setSize(size, size);
	}

	protected void showPauseButton(boolean show) {
		pauseButton.setTouchable(show ? Touchable.enabled : Touchable.disabled);
		pauseButton.getView().setVisible(show);
	}

	protected void addPauseButtonToStage(Stage stage) {
		stage.addActor(pauseButton.getView());
		pauseButton.getView().toFront();
		positionPauseButton();
	}

	/**
	 * A sub-menu that opens up within this menu (e.g. a sub-menu for sound options)
	 */
	protected class NestedMenu extends PopUpMenu implements DoesNotPause {

		private final String buttonKey;

		public NestedMenu(final List<UniversalButton> entries, String buttonKey) {
			super(true, entries, "back_message");
			this.buttonKey = buttonKey;
		}

		@Override
		protected void setUpDesiredSize() {
			menu.setSpacing(PauseMenu.this.menu.getSpacing());
			UserInterfaceSizer.sizeSquareActorToPercentage(this, 0.5f);
			if (this.getActions().isEmpty()) {
				UserInterfaceSizer.makeActorCentered(this);
			}
		}

		public UniversalButton getButton() {
			return MainGame.getUserInterfaceFactory().getButton(TextSupplier.getLine(buttonKey), () -> InputPriority.claimPriority(this));
		}

		@Override
		public void actWhilePaused(float delta) {
			act(delta);
		}

		@Override
		protected void setUpTable() {
			setUpDesiredSize();
			background(MainGame.getUserInterfaceFactory().getUIBorderedNine());

			menu.setAlignment(Alignment.CENTER, Alignment.TOP);
			add(menu.getView()).growX().top();
			UserInterfaceSizer.makeActorCentered(this);
		}
	}
}

