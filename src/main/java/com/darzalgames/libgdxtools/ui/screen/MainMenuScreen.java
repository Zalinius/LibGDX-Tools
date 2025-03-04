package com.darzalgames.libgdxtools.ui.screen;

import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriorityStack;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;

/**
 * Essentially a wrapper for the main menu (a highly-custom class which extends {@link NavigableListMenu}) which handles initialization and cleanup
 */
public class MainMenuScreen extends GameScreen {

	private final NavigableListMenu mainMenu;

	public MainMenuScreen(NavigableListMenu mainMenu, InputPriorityStack inputPriorityStack)
	{
		super(Runnables.nullRunnable(), inputPriorityStack);
		this.mainMenu = mainMenu;
		addActor(mainMenu);
	}

	@Override
	public void gainFocus() {
		InputPriority.claimPriority(mainMenu);
	}
	
	/**
	 * Recreates the main menu buttons, useful when changing language or font
	 */
	public void refresh() {
		// TODO no longer needed, presumably?? Or does QG still need this?
		mainMenu.regainFocus();
	}

	@Override public String toString() { return "Main menu SCREEN, but not the buttons themselves"; }

	@Override
	public void resizeUI() {
		mainMenu.resizeUI();
	}
}
