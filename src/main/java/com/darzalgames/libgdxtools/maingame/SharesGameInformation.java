package com.darzalgames.libgdxtools.maingame;

import com.darzalgames.libgdxtools.platform.GamePlatform;
import com.darzalgames.libgdxtools.preferences.PreferenceManager;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;

public interface SharesGameInformation {

	public String getGameName();
	public String getGameVersion();
	
	public int getWidth();
	public int getHeight();

	public SaveManager getSaveManager();
	public PreferenceManager getPreferenceManager();
	public GamePlatform getGamePlatform();
	public SteamStrategy getSteamStrategy();
	
}
