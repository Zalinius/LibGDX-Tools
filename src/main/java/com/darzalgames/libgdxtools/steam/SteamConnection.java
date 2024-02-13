package com.darzalgames.libgdxtools.steam;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.*;

public class SteamConnection {

	private static SteamUserStats steamUserStats;
	private static SteamUser steamUser;
	private static SteamFriends steamFriends;
	private static SteamController steamController;

	private static final String STEAM_CONNECTED = "[STEAM]";
	private static final String STEAM_NOT_CONNECTED = "[STEAM (NOT CONNECTED)]";

	private SteamConnection() {}

	/**
	 * Attempts to load the Steam libraries, an initialize various Steam services (statistics, friends, etc.)
	 */
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
				steamController = new SteamController();
				steamController.init();
				Gdx.app.log(STEAM_CONNECTED, "Libraries loaded!");
			}
		} catch (SteamException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Must be called every frame to check for Steam callbacks
	 */
	public static void update() {
		if (SteamAPI.isSteamRunning()) {
			SteamAPI.runCallbacks();
		}
	}

	/**
	 * Must be called when disposing of the game for proper cleanup
	 */
	public static void dispose() {
		if(SteamAPI.isSteamRunning()) {
			steamController.shutdown();
			SteamAPI.shutdown();
			steamUserStats.dispose();
		}
	}

	/**
	 * Get a statistic specific to the user
	 * @param stat The statistic key, matching those we set up on the Steam website
	 * @return The fetched value with the default of 0, or -1 if Steam isn't running
	 */
	public static int getStat(String stat) {
		if (SteamAPI.isSteamRunning()) {
			return steamUserStats.getStatI(stat, 0);
		} else {
			return -1;
		}
	}

	/**
	 * Get a global/aggregated statistic
	 * @param stat The statistic key, matching those we set up on the Steam website
	 * @return The fetched value with the default of 0, or -1 if Steam isn't running
	 */
	public static long getGlobalStat(String stat) {
		if (SteamAPI.isSteamRunning()) {
			return steamUserStats.getGlobalStat(stat, 0);
		} else {
			return -1;
		}
	}

	/**
	 * Add to or subtract from a particular Steam statistic value. The should even work offline.
	 * @param stat The statistic key, matching those we set up on the Steam website
	 * @param val The amount to increment/decrement by
	 */
	public static void setStat(String stat, int val) {
		if(SteamAPI.isSteamRunning()) {
			Gdx.app.log(STEAM_CONNECTED, "Updating stat: " + stat + " to " + val);
			steamUserStats.setStatI(stat, val);
		} else {
			Gdx.app.log(STEAM_NOT_CONNECTED, "Didn't actually update stat: " + stat + " to " + val);
		}
	}

	/**
	 * Get the ID unique to the current user, useful for differentiating save files
	 * or for use as a seed when making interesting game features (e.g. generating a player portrait)
	 * @return The ID, or the string "dev" if Steam is not running
	 */
	public static String getSteamID() {
		if(SteamAPI.isSteamRunning()) {
			return String.valueOf(steamUser.getSteamID().getAccountID());
		} else {
			return "dev";
		}
	}

	/**
	 * @return The SteamController, will be null if initialization failed
	 */
	public static SteamController getSteamController() {
		return steamController;
	}

	/**
	 * Grant an achievement to a player. This only works if Steam is running.
	 * @param achievement The achievement key, matching those we set up on the Steam website
	 */
	public static void giveAchievement(String achievement) {
		if(SteamAPI.isSteamRunning()) {
			Gdx.app.log(STEAM_CONNECTED, "Granting achievement: " + achievement);
			steamUserStats.setAchievement(achievement);
			steamUserStats.storeStats();
		} else {
			Gdx.app.log(STEAM_NOT_CONNECTED, "Granting achievement: " + achievement);
		}
	}

	/**
	 * Sets a variable used when generating the strings for rich presence.
	 * This only works when Steam is running.
	 * @param key The variable key, matching those we set up in the externalMedia RichPresence.txt file
	 * @param value The string to be used to replace the variable
	 */
	public static void setRichPresentsVariable(String key, String value) {
		if(SteamAPI.isSteamRunning()) {
			Gdx.app.log(STEAM_CONNECTED, "Updating rich presence variable: " + key + " to " + value);
			steamFriends.setRichPresence(key, value);
		} else {
			Gdx.app.log(STEAM_NOT_CONNECTED, "Didn't actually update rich presence to: " + value);
		}
	}

	/**
	 * Sets the player's rich presence to localized string obtained from the supplied key.
	 * If this key uses variables (e.g. in Quest Giver we insert the scenario name), be sure to first
	 * call {@link #setRichPresentsVariable(String, String)} with the relevant value(s).
	 * This only works when Steam is running.
	 * @param key The rich presence key, matching those we set up in the externalMedia RichPresence.txt file
	 */
	public static void setRichPresents(String key) {
		if(SteamAPI.isSteamRunning()) {
			Gdx.app.log(STEAM_CONNECTED, "Updating rich presence to: " + key);
			steamFriends.setRichPresence("steam_display", key);
			steamUserStats.storeStats();
		} else {
			Gdx.app.log(STEAM_NOT_CONNECTED, "Didn't actually update rich presence to: " + key);
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
