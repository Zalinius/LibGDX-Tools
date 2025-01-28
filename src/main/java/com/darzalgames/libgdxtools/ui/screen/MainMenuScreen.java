package com.darzalgames.libgdxtools.ui.screen;

import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriorityStack;
import com.darzalgames.libgdxtools.ui.input.inputpriority.Priority;
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
		Priority.claimPriority(mainMenu);
	}

	@Override
	public void selectDefault() {}
	@Override
	public void clearSelected() {}
	@Override
	public void focusCurrent() {}
	@Override
	public void consumeKeyInput(Input input) {}

	@Override public String toString() { return "Main menu SCREEN, but not the buttons themselves"; }
}
