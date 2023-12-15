package com.darzalgames.libgdxtools.steam;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.*;

public class SteamConnection {

	private static SteamUserStats steamUserStats;
	private static SteamUser steamUser;
	private static SteamFriends steamFriends;
	

	public static void initialize() {
		try {
			SteamAPI.loadLibraries();
			if (!SteamAPI.init()) {
				System.err.println("Could not load Steam libraries. Is Steam running?");
			}
			else {
				steamUserStats = new SteamUserStats(getSteamUserStatsCallback());
				steamUserStats.requestCurrentStats();
				steamUserStats.requestGlobalStats(0);
				steamUser = new SteamUser(null);
				steamFriends = new SteamFriends(new FriendsCallback());
				System.out.println("Steam libraries loaded!");
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
			//Gdx.app.log("STEAM (NOT CONNECTED)", "Tried to get stat: " + stat);
			return -1;
		}
	}

	public static long getGlobalStat(String stat) {
		if (SteamAPI.isSteamRunning()) {
			return steamUserStats.getGlobalStat(stat, 0);
		} else {
			//Gdx.app.log("STEAM (NOT CONNECTED)", "Tried to get stat: " + stat);
			return -1;
		}
	}

	public static void setStat(String stat, int val) {
		if(SteamAPI.isSteamRunning()) {
			Gdx.app.log("STEAM", "Updating stat: " + stat + " to " + val);
			steamUserStats.setStatI(stat, val);
		} else {
			Gdx.app.log("STEAM (NOT CONNECTED)", "Didn't actually update stat: " + stat + " to " + val);
		}
	}

	public static String getSteamID() {
		if(SteamAPI.isSteamRunning()) {
			String id = String.valueOf(steamUser.getSteamID().getAccountID()); 
			//Gdx.app.log("STEAM", "Returning SteamID " + id);
			return id;
		} else {
			//Gdx.app.log("STEAM (NOT CONNECTED)", "Returning default ID");
			return "dev";
		}
	}

	public static void giveAchievement(String achievement) {
		if(SteamAPI.isSteamRunning()) {
			Gdx.app.log("STEAM", "Granting achievement: " + achievement);
			steamUserStats.setAchievement(achievement);
			steamUserStats.storeStats();
		} else {
			Gdx.app.log("STEAM (NOT CONNECTED)", "Granting achievement: " + achievement);
		}
	}

	public static void setRichPresentsVariable(String key, String val) {
		if(SteamAPI.isSteamRunning()) {
			//Gdx.app.log("STEAM", "Updating rich presence: " + key + " to " + val);
			steamFriends.setRichPresence(key, val);
		} else {
			//Gdx.app.log("STEAM (NOT CONNECTED)", "Didn't actually update rich presence to: " + val);
		}
	}

	public static void setRichPresents(String val) {
		if(SteamAPI.isSteamRunning()) {
			//Gdx.app.log("STEAM", "Updating rich presence to: " + val);
			steamFriends.setRichPresence("steam_display", val);
			steamUserStats.storeStats();
		} else {
			//Gdx.app.log("STEAM (NOT CONNECTED)", "Didn't actually update rich presence to: " + val);
		}
	}

	private static SteamUserStatsCallback getSteamUserStatsCallback() {
		return new SteamUserStatsCallback() {

			@Override
			public void onUserStatsUnloaded(SteamID steamIDUser) {}
			@Override
			public void onUserStatsStored(long gameId, SteamResult result) {
				steamUserStats.requestGlobalStats(0);
			}
			@Override
			public void onUserStatsReceived(long gameId, SteamID steamIDUser, SteamResult result) {}
			@Override
			public void onUserAchievementStored(long gameId, boolean isGroupAchievement, String achievementName,
					int curProgress, int maxProgress) {}
			@Override
			public void onNumberOfCurrentPlayersReceived(boolean success, int players) {}
			@Override
			public void onLeaderboardScoresDownloaded(SteamLeaderboardHandle leaderboard, SteamLeaderboardEntriesHandle entries, int numEntries) {}
			@Override
			public void onLeaderboardScoreUploaded(boolean success, SteamLeaderboardHandle leaderboard, int score,
					boolean scoreChanged, int globalRankNew, int globalRankPrevious) {}
			@Override
			public void onLeaderboardFindResult(SteamLeaderboardHandle leaderboard, boolean found) {}
			@Override
			public void onGlobalStatsReceived(long gameId, SteamResult result) {
				if (result.equals(SteamResult.OK))
					System.out.println("Global stats received");
			}
		};
	}


}
