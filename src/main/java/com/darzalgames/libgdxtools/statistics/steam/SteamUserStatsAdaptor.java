package com.darzalgames.libgdxtools.statistics.steam;

import com.codedisaster.steamworks.SteamUserStats;
import com.codedisaster.steamworks.SteamUserStatsCallback;
import com.darzalgames.libgdxtools.statistics.StatsController;

public class SteamUserStatsAdaptor extends SteamUserStats implements StatsController {

	public SteamUserStatsAdaptor(SteamUserStatsCallback callback) {
		super(callback);
		requestCurrentStats();
		requestGlobalStats(0);
	}

	@Override
	public int getStat(String stat) {
		return getStatI(stat, 0);
	}

	@Override
	public long getGlobalStat(String stat) {
		return getGlobalStat(stat, 0);
	}

	@Override
	public void setStat(String stat, int val) {
		setStatI(stat, val);
	}

	@Override
	public void giveAchievement(String achievement) {
		setAchievement(achievement);
		storeStats();
	}

}
