package com.darzalgames.libgdxtools.steam.agnostic;

import com.codedisaster.steamworks.SteamController;

public interface SteamStrategy {
	
	/**
	 * Must be called every frame to check for Steam callbacks
	 */
	public void update();

	/**
	 * Must be called when disposing of the game for proper cleanup
	 */
	public void dispose();

	/**
	 * Get a statistic specific to the user
	 * @param stat The statistic key, matching those we set up on the Steam website
	 * @return The fetched value with the default of 0, or -1 if Steam isn't running
	 */
	public int getStat(String stat);
	
	/**
	 * Get a global/aggregated statistic
	 * @param stat The statistic key, matching those we set up on the Steam website
	 * @return The fetched value with the default of 0, or -1 if Steam isn't running
	 */
	public long getGlobalStat(String stat);
	
	/**
	 * Add to or subtract from a particular Steam statistic value. The should even work offline.
	 * @param stat The statistic key, matching those we set up on the Steam website
	 * @param val The amount to increment/decrement by
	 */
	public void setStat(String stat, int val);
	
	/**
	 * Get the ID unique to the current user, useful for differentiating save files
	 * or for use as a seed when making interesting game features (e.g. generating a player portrait)
	 * @return The ID, or the string "dev" if Steam is not running
	 */
	public String getSteamID();
	
	/**
	 * @return The SteamController, will be null if initialization failed
	 */
	public SteamController getSteamController();
	
	/**
	 * Grant an achievement to a player. This only works if Steam is running.
	 * @param achievement The achievement key, matching those we set up on the Steam website
	 */
	public void giveAchievement(String achievement);
	
	/**
	 * Sets a variable used when generating the strings for rich presence.
	 * This only works when Steam is running.
	 * @param key The variable key, matching those we set up in the externalMedia RichPresence.txt file
	 * @param value The string to be used to replace the variable
	 */
	public void setRichPresentsVariable(String key, String value);

	/**
	 * Sets the player's rich presence to localized string obtained from the supplied key.
	 * If this key uses variables (e.g. in Quest Giver we insert the scenario name), be sure to first
	 * call {@link #setRichPresentsVariable(String, String)} with the relevant value(s).
	 * This only works when Steam is running.
	 * @param key The rich presence key, matching those we set up in the externalMedia RichPresence.txt file
	 */
	public void setRichPresents(String key);
}
