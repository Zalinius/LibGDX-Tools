package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.*;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.popup.PopUpMenu;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UserInterfaceFactory;


/**
 * The base class for pause menus (in-game versus when on the main menu)
 */
public abstract class PauseMenu extends PopUpMenu implements DoesNotPause {

	protected UniversalButton pauseButton;
	private final Supplier<UniversalButton> makeWindowModeSelectBox;

	private final String platformName;
	
	/**
	 * NOTE: Setting the position is important, otherwise the pause menu will not open!<p>
	 * e.g. call {@link UserInterfaceFactory#makeActorCentered(Actor) UserInterfaceFactory.makeActorCentered(this)} 
	 */
	protected abstract void setUpBackground();
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
		return UserInterfaceFactory.getButton(TextSupplier.getLine("back_message"), () -> toggleScreenVisibility(false));
	}

	/**
	 * @return A PopUp that explains the control schemes
	 */
	protected abstract PopUp makeControlsPopUp();

	protected PauseMenu(Supplier<UniversalButton> makeWindowModeSelectBox, int bottomPadding) {
		super(true);
		this.makeWindowModeSelectBox = makeWindowModeSelectBox;
		this.platformName = " (" + GameInfo.getGamePlatform().getPlatformName() + ")";
		setBounds(0, 0, GameInfo.getWidth(), GameInfo.getHeight());
		defaults().padBottom(bottomPadding);
	}

	@Override
	protected void setUpDesiredSize() {
		desiredWidth = 250;
		desiredHeight = 125;
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
	public void regainFocus() {
		focusCurrent();
		pauseButton.setTouchable(Touchable.enabled);
	}

	@Override
	protected void setUpTable() {
		setUpBackground();

		// ALL SELECTABLE MENU BUTTONS
		List<UniversalButton> menuButtons = new ArrayList<>();
		menuButtons.add(UserInterfaceFactory.getSpacer());

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
		
		menuButtons.add(UserInterfaceFactory.getSpacer());

		menuButtons.removeIf(Objects::isNull);

		menu.setAlignment(getEntryAlignment(), getMenuAlignment());
		menu.replaceContents(menuButtons, backButton);
		add(menu.getView()).grow().top();

		// Set up the version tag to be in its own inner table, so that the actual buttons can still reach the bottom of the main table
		Table versionTable = new Table();
		versionTable.setFillParent(true);
		Label versionLabel = UserInterfaceFactory.getFlavorTextLabel(getGameVersion() + platformName);
		versionLabel.setAlignment(Alignment.BOTTOM_RIGHT.getAlignment());
		versionTable.setTouchable(Touchable.disabled);
		addActor(versionTable);
		versionTable.add(versionLabel).bottom().grow().padBottom(getPadBottom() + 4).padRight(getPadRight());
	}

	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}
	
	@Override
	public boolean isGamePausedWhileThisIsInFocus() {
		return true;
	}
	
	protected void positionPauseButton() {
		int padding = 3;
		pauseButton.getView().setPosition(padding, GameInfo.getHeight() - pauseButton.getView().getHeight() - padding);
		showPauseButton(false); // Only enable the button after the splash screen
	}

	protected void showPauseButton(boolean show) {
		pauseButton.setTouchable(show ? Touchable.enabled : Touchable.disabled);
		pauseButton.getView().setVisible(show);
	}
	
	protected void addPauseButtonToStage(Stage stage) {
		stage.addActor(pauseButton.getView());
		pauseButton.getView().toFront();
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
			desiredWidth = 252;
			desiredHeight = 100;
		}

		public UniversalButton getButton() {
			return UserInterfaceFactory.getButton(TextSupplier.getLine(buttonKey), () -> InputPriority.claimPriority(this));
		}

		@Override
		public void actWhilePaused(float delta) {
			act(delta);
		}

		@Override
		protected void setUpTable() {
			setSize(desiredWidth, desiredHeight);
			NinePatchDrawable back = UserInterfaceFactory.getUIBorderedNine();
			background(back);
			UserInterfaceFactory.makeActorCentered(this);

			menu.setAlignment(Alignment.CENTER, Alignment.TOP);
			add(menu.getView()).growX().top();
		}
	}
}

