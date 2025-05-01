package com.darzalgames.libgdxtools.statistics;

import com.darzalgames.libgdxtools.statistics.steam.SteamUserStatsAdaptor;
import com.darzalgames.libgdxtools.statistics.steam.SteamUserStatsCallbackDefault;

public class StatsControllerFactory {

	private StatsControllerFactory() {
	}

	public static StatsController buildSteam() {
		SteamUserStatsCallbackDefault callback = new SteamUserStatsCallbackDefault();
		SteamUserStatsAdaptor userStats = new SteamUserStatsAdaptor(callback);
		callback.setUserStats(userStats);
		return userStats;
	}

	public static StatsController buildDummy() {
		return new StatsController() {
			@Override public void setStat(String stat, int val) { /* Dummy */ }
			@Override public void giveAchievement(String achievement) { /* Dummy */ }
			@Override public void dispose() { /* Dummy */ }
			@Override public int getStat(String stat) { /* Dummy */ return 0; }
			@Override public long getGlobalStat(String stat) { /* Dummy */ return 0; }
		};
	}
}
