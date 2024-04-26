package com.darzalgames.libgdxtools.steam;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.*;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.input.handler.SteamGamepadInputHandler;

public class ConnectedSteamStrategy implements SteamStrategy {
	private SteamGamepadInputHandler gamepadInputHandler;
	
	private static SteamUserStats steamUserStats;
	private static SteamUser steamUser;
	private static SteamFriends steamFriends;
	private static SteamController steamController;

	public ConnectedSteamStrategy(SteamGamepadInputHandler gamepadInputHandler) {
		this.gamepadInputHandler = gamepadInputHandler;
	}

	public void initialize() {
		steamUserStats = new SteamUserStats(getSteamUserStatsCallback());
		steamUserStats.requestCurrentStats();
		steamUserStats.requestGlobalStats(0);
		steamUser = new SteamUser(null);
		steamFriends = new SteamFriends(new FriendsCallback());
		steamController = new SteamController();
		this.gamepadInputHandler.setSteamController(steamController);
		steamController.init();
	}

	@Override
	public void update() {
		SteamAPI.runCallbacks();
	}

	@Override
	public void dispose() {
		steamController.shutdown();
		SteamAPI.shutdown();
		steamUserStats.dispose();
	}
	
	@Override
	public SteamGamepadInputHandler getGamepadInputHandler() {
		return gamepadInputHandler;
	}
	
	@Override
	public int getStat(String stat) {
		return steamUserStats.getStatI(stat, 0);
	}

	@Override
	public long getGlobalStat(String stat) {
		return steamUserStats.getGlobalStat(stat, 0);
	}
	
	@Override
	public void setStat(String stat, int val) {
		steamUserStats.setStatI(stat, val);
	}
	
	@Override
	public String getSteamID() {
		return String.valueOf(steamUser.getSteamID().getAccountID());
	}
	
	@Override
	public void giveAchievement(String achievement) {
		steamUserStats.setAchievement(achievement);
		steamUserStats.storeStats();
	}
	
	@Override
	public void setRichPresentsVariable(String key, String value) {
		steamFriends.setRichPresence(key, value);
	}
	
	@Override
	public void setRichPresents(String key) {
		steamFriends.setRichPresence("steam_display", key);
		steamUserStats.storeStats();
	}	
	
	private static SteamUserStatsCallback getSteamUserStatsCallback() {
		return new SteamUserStatsCallback() {

			@Override
			public void onUserStatsUnloaded(SteamID steamIDUser) { /* To be implemented when needed */ }
			@Override
			public void onUserStatsStored(long gameId, SteamResult result) {
				steamUserStats.requestGlobalStats(0);
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
				if (result.equals(SteamResult.OK))
					Gdx.app.log("[SteamConnection]", "Global stats received");
			}
		};
	}

}
