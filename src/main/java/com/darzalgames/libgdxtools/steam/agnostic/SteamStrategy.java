package com.darzalgames.libgdxtools.steam.agnostic;

import com.darzalgames.libgdxtools.steam.stats.StatsController;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;

public interface SteamStrategy {

	/**
	 * Must be called every frame to check for Steam callbacks
	 */
	void update();

	/**
	 * Must be called when disposing of the game for proper cleanup
	 */
	void dispose();


	StatsController getStatsController();

	/**
	 * Get the ID unique to the current user, useful for differentiating save files
	 * or for use as a seed when making interesting game features (e.g. generating a player portrait)
	 * @return The ID, or the string "dev" if Steam is not running
	 */
	String getSteamID();


	/**
	 * @return The game-specific input handler for Steam
	 */
	GamepadInputHandler getGamepadInputHandler();

	/**
	 * Sets a variable used when generating the strings for rich presence.
	 * This only works when Steam is running.
	 * @param key The variable key, matching those we set up in the externalMedia RichPresence.txt file
	 * @param value The string to be used to replace the variable
	 */
	void setRichPresentsVariable(String key, String value);

	/**
	 * Sets the player's rich presence to localized string obtained from the supplied key.
	 * If this key uses variables (e.g. in Quest Giver we insert the scenario name), be sure to first
	 * call {@link #setRichPresentsVariable(String, String)} with the relevant value(s).
	 * This only works when Steam is running.
	 * @param key The rich presence key, matching those we set up in the externalMedia RichPresence.txt file
	 */
	void setRichPresents(String key);
}
