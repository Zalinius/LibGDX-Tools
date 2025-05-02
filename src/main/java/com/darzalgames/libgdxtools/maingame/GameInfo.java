package com.darzalgames.libgdxtools.maingame;

import com.darzalgames.libgdxtools.platform.GamePlatform;
import com.darzalgames.libgdxtools.preferences.PreferenceManager;
import com.darzalgames.libgdxtools.save.DesktopSaveManager;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UserInterfaceFactory;

/**
 * Allows static access to the globally useful variables/managers from {@link MainGame}
 */
public class GameInfo {

	private static SharesGameInformation game;

	private GameInfo() {}

	/**
	 * @param game The game that this class will be sharing info from
	 */
	public static void setMainGame(SharesGameInformation game) {
		GameInfo.game = game;
	}

	/**
	 * @return The name of the game
	 */
	public static String getGameName() {
		return game.getGameName();
	}

	/**
	 * @return The semantic version of the game, possibly appended with the edition, e.g. <b>1.0.3-demo</b>
	 */
	public static String getGameVersion() {
		return game.getGameVersion();
	}

	/**
	 * @return Gets the {@link PreferenceManager}, useful to access the more specific preference managers (such as sound, or more temporary "other" managers)
	 */
	public static PreferenceManager getPreferenceManager() {
		return game.getPreferenceManager();
	}

	/**
	 * @return Gets the {@link DesktopSaveManager}, which is a concrete class in a full game
	 */
	public static SaveManager getSaveManager() {
		return game.getSaveManager();
	}

	public static GamePlatform getGamePlatform() {
		return game.getGamePlatform();
	}

	public static SteamStrategy getSteamStrategy() {
		return game.getSteamStrategy();
	}

	public static UserInterfaceFactory getUserInterfaceFactory() {
		return game.getUserInterfaceFactory();
	}
}
