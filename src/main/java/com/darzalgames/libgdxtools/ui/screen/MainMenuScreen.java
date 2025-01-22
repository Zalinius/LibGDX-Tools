package com.darzalgames.libgdxtools.ui.screen;

import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;

/**
 * Essentially a wrapper for the main menu (a highly-custom class which extends {@link NavigableListMenu}) which handles initialization and cleanup
 * @author DarZal
 */
public class MainMenuScreen extends GameScreen {
	
	public MainMenuScreen(NavigableListMenu mainMenu)
	{
		super(Runnables.nullRunnable());
		addActor(mainMenu);
	}
	
	@Override
	public void gainFocus() {
		show();
	}
}
