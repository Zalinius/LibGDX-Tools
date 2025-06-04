package com.darzalgames.libgdxtools.maingame;

import com.darzalgames.libgdxtools.platform.GameEdition;
import com.darzalgames.libgdxtools.platform.GamePlatform;
import com.darzalgames.libgdxtools.preferences.PreferenceManager;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UserInterfaceFactory;

public interface SharesGameInformation {

	String getGameName();
	String getGameVersion();

	SaveManager getSaveManager();
	PreferenceManager getPreferenceManager();
	GamePlatform getGamePlatform();
	GameEdition getGameEdition();
	SteamStrategy getSteamStrategy();
	UserInterfaceFactory getUserInterfaceFactory();

}
