package com.darzalgames.libgdxtools.ui.screen;

import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.inputpriority.InputPriorityManager;

/**
 * Essentially a wrapper for the main menu (a highly-custom class which extends {@link NavigableListMenu}) which handles initialization and cleanup
 * @author DarZal
 */
public class MainMenuScreen extends GameScreen {

	private NavigableListMenu mainMenu;
	
	public MainMenuScreen(NavigableListMenu mainMenu)
	{
		super(Runnables.nullRunnable());
		this.mainMenu = mainMenu;
		addActor(mainMenu);
	}
	
	@Override
	public void gainFocus() {
		InputPriorityManager.claimPriority(mainMenu);
	}
}
