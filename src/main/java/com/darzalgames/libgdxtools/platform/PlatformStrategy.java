package com.darzalgames.libgdxtools.platform;

import java.util.function.Supplier;

import com.darzalgames.libgdxtools.ui.input.handler.FallbackGamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.SteamGamepadInputHandler;

public interface PlatformStrategy {

	/**
	 * Initialization function, called very early in the game launch process
	 */
	void initialize();

	/**
	 * Initialization function, called once the input system is available
	 */
	void initializeInput(
			Supplier<FallbackGamepadInputHandler> makeFallbackGamepadInputHandler,
			Supplier<SteamGamepadInputHandler> makeSteamGamepadInputHandler);

	/**
	 * Must be called every frame to check for Steam callbacks
	 */
	void update();

	/**
	 * Must be called when disposing of the game for proper cleanup
	 */
	void dispose();

	/**
	 * Get an folder name for the current user, useful for differentiating save files
	 * @return The folder name
	 */
	String getPlayersSaveFolderName();

	StatsController getStatsController();

	/**
	 * @return The game-specific input handler for Steam
	 */
	GamepadInputHandler getGamepadInputHandler();

	/**
	 * Sets a variable used when generating the strings for rich presence.
	 * This only works when Steam is running.
	 * @param key   The variable key, matching those we set up in the externalMedia RichPresence.txt file
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
