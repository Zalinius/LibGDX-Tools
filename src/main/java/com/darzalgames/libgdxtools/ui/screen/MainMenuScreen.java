package com.darzalgames.libgdxtools.ui.screen;

import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.scrollablemenu.ScrollableMenu;

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
