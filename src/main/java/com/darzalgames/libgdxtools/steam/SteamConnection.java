package com.darzalgames.libgdxtools.steam;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.*;

public class SteamConnection {

	private static SteamUserStats steamUserStats;
	private static SteamUser steamUser;
	private static SteamFriends steamFriends;
	
	private static final String STEAM_CONNECTED = "[STEAM]";
	private static final String STEAM_NOT_CONNECTED = "[STEAM (NOT CONNECTED)]";
	
	private SteamConnection() {}

	public static void initialize() {
		try {
			SteamAPI.loadLibraries();
			if (!SteamAPI.init()) {
				Gdx.app.error(STEAM_NOT_CONNECTED, "Could not load Steam libraries. Is Steam running?");
			}
			else {
				steamUserStats = new SteamUserStats(getSteamUserStatsCallback());
				steamUserStats.requestCurrentStats();
				steamUserStats.requestGlobalStats(0);
				steamUser = new SteamUser(null);
				steamFriends = new SteamFriends(new FriendsCallback());
				Gdx.app.log(STEAM_CONNECTED, "Libraries loaded!");
			}
		} catch (SteamException e) {
			e.printStackTrace();
		}
	}

	public static void update() {
		if (SteamAPI.isSteamRunning()) {
			SteamAPI.runCallbacks();
		}
	}

	public static void dispose() {
		if(SteamAPI.isSteamRunning()) {
			SteamAPI.shutdown();
			steamUserStats.dispose();			
		}
	}

	public static int getStat(String stat) {
		if (SteamAPI.isSteamRunning()) {
			return steamUserStats.getStatI(stat, 0);
		} else {
			return -1;
		}
	}

	public static long getGlobalStat(String stat) {
		if (SteamAPI.isSteamRunning()) {
			return steamUserStats.getGlobalStat(stat, 0);
		} else {
			return -1;
		}
	}

	public static void setStat(String stat, int val) {
		if(SteamAPI.isSteamRunning()) {
			Gdx.app.log(STEAM_CONNECTED, "Updating stat: " + stat + " to " + val);
			steamUserStats.setStatI(stat, val);
		} else {
			Gdx.app.log(STEAM_NOT_CONNECTED, "Didn't actually update stat: " + stat + " to " + val);
		}
	}

	public static String getSteamID() {
		if(SteamAPI.isSteamRunning()) {
			return String.valueOf(steamUser.getSteamID().getAccountID());
		} else {
			return "dev";
		}
	}

	public static void giveAchievement(String achievement) {
		if(SteamAPI.isSteamRunning()) {
			Gdx.app.log(STEAM_CONNECTED, "Granting achievement: " + achievement);
			steamUserStats.setAchievement(achievement);
			steamUserStats.storeStats();
		} else {
			Gdx.app.log(STEAM_NOT_CONNECTED, "Granting achievement: " + achievement);
		}
	}

	public static void setRichPresentsVariable(String key, String val) {
		if(SteamAPI.isSteamRunning()) {
			Gdx.app.log(STEAM_CONNECTED, "Updating rich presence variable: " + key + " to " + val);
			steamFriends.setRichPresence(key, val);
		} else {
			Gdx.app.log(STEAM_NOT_CONNECTED, "Didn't actually update rich presence to: " + val);
		}
	}

	public static void setRichPresents(String val) {
		if(SteamAPI.isSteamRunning()) {
			Gdx.app.log(STEAM_CONNECTED, "Updating rich presence to: " + val);
			steamFriends.setRichPresence("steam_display", val);
			steamUserStats.storeStats();
		} else {
			Gdx.app.log(STEAM_NOT_CONNECTED, "Didn't actually update rich presence to: " + val);
		}
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
