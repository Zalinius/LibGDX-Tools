package com.darzalgames.libgdxtools.maingame;

import com.darzalgames.libgdxtools.platform.GamePlatform;
import com.darzalgames.libgdxtools.preferences.PreferenceManager;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriorityStack;

public interface SharesGameInformation {

	public int getWidth();
	public int getHeight();
	public SaveManager getSaveManager();
	public PreferenceManager getPreferenceManager();
	public GamePlatform getGamePlatform();
	public SteamStrategy getSteamStrategy();
	public InputPriorityStack getInputPriorityStack();
	
}
