package com.darzalgames.libgdxtools.statistics;

public interface StatsController {

	/**
	 * Get a statistic specific to the user
	 * @param stat The statistic key, matching those we set up on the Steam website
	 * @return The fetched value with the default of 0, or -1 if Steam isn't running
	 */
	int getStat(String stat);

	/**
	 * Get a global/aggregated statistic
	 * @param stat The statistic key, matching those we set up on the Steam website
	 * @return The fetched value with the default of 0, or -1 if Steam isn't running
	 */
	long getGlobalStat(String stat);

	/**
	 * Add to or subtract from a particular Steam statistic value. The should even work offline.
	 * @param stat The statistic key, matching those we set up on the Steam website
	 * @param val  The amount to increment/decrement by
	 */
	void setStat(String stat, int val);

	/**
	 * Grant an achievement to a player. This only works if Steam is running.
	 * @param achievement The achievement key, matching those we set up on the Steam website
	 */
	void giveAchievement(String achievement);

	void dispose();

}
