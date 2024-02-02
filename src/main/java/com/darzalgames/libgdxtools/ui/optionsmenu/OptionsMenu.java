package com.darzalgames.libgdxtools.ui.optionsmenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.MainGame;
import com.darzalgames.libgdxtools.i18n.TextSupplier;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.handler.SteamControllerManager;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.UserInterfaceFactory;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.popup.PopUpMenu;


/**
 * The base class for options menus (in-game versus when on the main menu)
 * @author DarZal
 */
public abstract class OptionsMenu extends PopUpMenu implements DoesNotPause {

	protected KeyboardButton optionsButton;
	private final Supplier<KeyboardButton> makeWindowModeSelectBox;

	protected abstract void setUpBackground();
	protected abstract Alignment getEntryAlignment();
	protected abstract Alignment getMenuAlignment();
	protected abstract String getGameVersion();
	
	/**
	 * @return A list of any game-specific options buttons (e.g. language, text speed, etc)
	 */
	protected abstract Collection<KeyboardButton> makeMiddleButtons();
	
	/**
	 * @return An optional button that goes right above a quit button (e.g. in-game have a "return to main menu" button)
	 */
	protected abstract KeyboardButton makeButtonAboveQuitButton();
	
	
	/**
	 * @return A PopUp that explains the control schemes
	 */
	protected abstract PopUp makeControlsPopUp();
	
	protected OptionsMenu(Supplier<KeyboardButton> makeWindowModeSelectBox, int bottomPadding) {
		super(true);
		this.makeWindowModeSelectBox = makeWindowModeSelectBox;
		setBounds(0, 0, MainGame.getWidth(), MainGame.getHeight());
		defaults().padBottom(bottomPadding);
	}
	
	/**
	 * @return The button that opens this options menu
	 */
	public KeyboardButton getButton() {
		return optionsButton;
	}

	/**
	 * To be used by child classes to have buttons in the menu hide/show it (e.g. "return to main menu" should toggle screen visibility to false)
	 * @param show
	 */
	protected void toggleScreenVisibility(boolean show) {
		if (show) {
			InputPriorityManager.pauseIfNeeded();
		} else {
			hideThis();
		}
	}

	@Override
	public void regainFocus() {
		focusCurrent();
		optionsButton.setTouchable(Touchable.enabled);
	}

	@Override
	protected void setUpTable() {
		setUpBackground();

		padTop(7);
		padBottom(3);

		// ALL SELECTABLE MENU BUTTONS
		List<KeyboardButton> menuButtons = new ArrayList<>();
		menuButtons.add(UserInterfaceFactory.getSpacer());
		
		menuButtons.addAll(makeMiddleButtons());

		KeyboardButton reportBugButton = UserInterfaceFactory.getButton(
				TextSupplier.getLine("report_bug_message"),
				() -> {
					String form = "https://forms.gle/j1CjPH8xdJskiYe19";
					Gdx.net.openURI(form);
				});
		menuButtons.add(reportBugButton);
		
		KeyboardButton controlsButton = UserInterfaceFactory.getButton(TextSupplier.getLine("controls_message"), () -> {
			InputPriorityManager.claimPriority(makeControlsPopUp());
			SteamControllerManager.openControlsOverlay();
		});
		menuButtons.add(controlsButton);

		// Window mode select box
		KeyboardButton windowModeSelectBox = makeWindowModeSelectBox.get();
		menuButtons.add(windowModeSelectBox);

		KeyboardButton optionalButtonAboveQuit = makeButtonAboveQuitButton();
		if (optionalButtonAboveQuit != null) {
			menuButtons.add(optionalButtonAboveQuit);
		}
		
		// Quit game button
		menuButtons.add(UserInterfaceFactory.getQuitGameButtonWithWarning(() -> optionsButton.setTouchable(Touchable.disabled)));

		// Back button
		KeyboardButton backButton = UserInterfaceFactory.getButton(TextSupplier.getLine("back_message"), () -> toggleScreenVisibility(false));
		menuButtons.add(UserInterfaceFactory.getSpacer());

		menu.setAlignment(getEntryAlignment(), getMenuAlignment());
		menu.replaceContents(menuButtons, backButton);
		add(menu.getView()).grow().top();

		// Set up the version tag to be in its own inner table, so that the actual buttons can still reach the bottom of the main table
		Table versionTable = new Table();
		versionTable.setFillParent(true);
		Label versionLabel = UserInterfaceFactory.getFlavorTextLabel(getGameVersion());
		versionLabel.setAlignment(Alignment.BOTTOM_RIGHT.getAlignment());
		versionTable.setTouchable(Touchable.disabled);
		addActor(versionTable);
		versionTable.add(versionLabel).bottom().grow().padBottom(getPadBottom() + 4).padRight(getPadRight());
	}

	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}

	/**
	 * A sub-menu that opens up within this menu (e.g. a sub-menu for sound options)
	 * @author DarZal
	 */
	protected class NestedMenu extends PopUpMenu implements DoesNotPause {
		
		private final String buttonKey;
		
		public NestedMenu(final List<KeyboardButton> entries, String buttonKey) {
			super(true, entries, "back_message");
			this.buttonKey = buttonKey;
		}
		
		public KeyboardButton getButton() {
			return UserInterfaceFactory.getButton(TextSupplier.getLine(buttonKey), () -> InputPriorityManager.claimPriority(this));
		}

		@Override
		public void actWhilePaused(float delta) {
			act(delta);
		}

		@Override
		protected void setUpTable() {
			setSize(250, 125);
	        NinePatchDrawable back = UserInterfaceFactory.getUIBorderedNine();
	        background(back);
	        UserInterfaceFactory.makeActorCentered(this);

			menu.setAlignment(Alignment.CENTER, Alignment.TOP);
			add(menu.getView()).growX().top();
		}
	}
}

