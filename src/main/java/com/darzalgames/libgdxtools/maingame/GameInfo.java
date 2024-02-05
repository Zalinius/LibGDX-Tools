package com.darzalgames.libgdxtools.maingame;

import com.darzalgames.libgdxtools.preferencemanagers.PreferenceManager;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

/**
 * Allows static access to the globally useful variables/managers from {@link MainGame}
 * @author DarZal
 */
public class GameInfo {

	private static MainGame mainGame;
	
	private GameInfo() {}
	
	public static void initialize(MainGame mainGame) {
		GameInfo.mainGame = mainGame;
	}
	
	/**
	 * @return The width (in art "pixels") of the logical game window
	 */
	public static int getWidth() {
		return mainGame.width;
	}
	/**
	 * @return The height (in art "pixels") of the logical game window
	 */
	public static int getHeight() {
		return mainGame.height;
	}

	/**
	 * @return Gets the {@link InputStrategyManager}, useful to do things like un/registering input-sensitive labels,
	 * checking what the current input method is, changing input modes, etc.
	 */
	public static InputStrategyManager getInputStrategyManager() {
		return mainGame.inputStrategyManager;
	}

	/**
	 * @return Gets the {@link PreferenceManager}, useful to access the more specific preference managers (such as sound, or more temporary "other" managers)
	 */
	public static PreferenceManager getPreferenceManager() {
		return mainGame.preferenceManager;
	}

	/**
	 * @return Gets the {@link SaveManager}, which is a concrete class in a full game
	 */
	public static SaveManager getSaveManager() {
		return mainGame.saveManager;
	}
}
