package com.darzalgames.libgdxtools.platform.steam;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.*;

public class SteamUserStatsCallbackDefault implements SteamUserStatsCallback {

	private SteamUserStats userStats;

	@Override
	public void onUserStatsUnloaded(SteamID steamIDUser) { /* To be implemented when needed */ }

	@Override
	public void onUserStatsStored(long gameId, SteamResult result) {
		userStats.requestGlobalStats(0);
	}

	@Override
	public void onUserStatsReceived(long gameId, SteamID steamIDUser, SteamResult result) { /* To be implemented when needed */ }

	@Override
	public void onUserAchievementStored(long gameId, boolean isGroupAchievement, String achievementName,
			int curProgress, int maxProgress) { /* To be implemented when needed */ }

	@Override
	public void onNumberOfCurrentPlayersReceived(boolean success, int players) { /* To be implemented when needed */ }

	@Override
	public void onLeaderboardScoresDownloaded(SteamLeaderboardHandle leaderboard, SteamLeaderboardEntriesHandle entries, int numEntries) { /* To be implemented when needed */ }

	@Override
	public void onLeaderboardScoreUploaded(boolean success, SteamLeaderboardHandle leaderboard, int score,
			boolean scoreChanged, int globalRankNew, int globalRankPrevious) { /* To be implemented when needed */ }

	@Override
	public void onLeaderboardFindResult(SteamLeaderboardHandle leaderboard, boolean found) { /* To be implemented when needed */ }

	@Override
	public void onGlobalStatsReceived(long gameId, SteamResult result) {
		if (SteamResult.OK.equals(result)) {
			Gdx.app.log("[SteamConnection]", "Global stats received");
		}
	}

	public void setUserStats(SteamUserStats userStats) {
		this.userStats = userStats;
	}

}