package com.darzalgames.libgdxtools.maingame;

import com.darzalgames.libgdxtools.edition.GameEdition;
import com.darzalgames.libgdxtools.os.GameOperatingSystem;
import com.darzalgames.libgdxtools.preferences.PreferenceManager;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.steam.agnostic.PlatformStrategy;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UserInterfaceFactory;

public interface SharesGameInformation {

	String getGameName();

	String getGameVersion();

	GameEdition getGameEdition();

	GameOperatingSystem getOperatingSystem();

	SaveManager getSaveManager();

	PreferenceManager getPreferenceManager();

	PlatformStrategy getPlatformStrategy();

	UserInterfaceFactory getUserInterfaceFactory();

}
