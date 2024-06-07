package com.darzalgames.libgdxtools.maingame;

import com.darzalgames.libgdxtools.platform.GamePlatform;
import com.darzalgames.libgdxtools.preferences.PreferenceManager;
import com.darzalgames.libgdxtools.save.DesktopSaveManager;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;

/**
 * Allows static access to the globally useful variables/managers from {@link MainGame}
 * @author DarZal
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
	 * @return The width (in art "pixels") of the logical game window
	 */
	public static int getWidth() {
		return game.getWidth();
	}
	/**
	 * @return The height (in art "pixels") of the logical game window
	 */
	public static int getHeight() {
		return game.getHeight();
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
}
