package com.darzalgames.libgdxtools.ui.screen;

import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.scrollablemenu.ScrollableMenu;

/**
 * Essentially a wrapper for the main menu (a highly-custom class which extends {@link ScrollableMenu}) which handles initialization and cleanup
 * @author DarZal
 */
public class MainMenuScreen extends GameScreen {

	private ScrollableMenu mainMenu;
	
	public MainMenuScreen(ScrollableMenu mainMenu)
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
