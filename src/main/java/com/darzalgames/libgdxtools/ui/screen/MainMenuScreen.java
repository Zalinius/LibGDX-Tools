package com.darzalgames.libgdxtools.ui.screen;

import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;

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

    @Override
    public void selectDefault() {}
    @Override
    public void clearSelected() {}
    @Override
    public void focusCurrent() {}
    @Override
    public void consumeKeyInput(Input input) {}
}
