package com.darzalgames.libgdxtools.steam;

import com.codedisaster.steamworks.*;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.steam.stats.StatsController;
import com.darzalgames.libgdxtools.steam.stats.StatsControllerFactory;
import com.darzalgames.libgdxtools.ui.input.handler.SteamGamepadInputHandler;

public class ConnectedSteamStrategy implements SteamStrategy {
	private final SteamGamepadInputHandler gamepadInputHandler;

	private StatsController userStats;
	private SteamUser steamUser;
	private SteamFriends steamFriends;
	private SteamController steamController;

	public ConnectedSteamStrategy(SteamGamepadInputHandler gamepadInputHandler) {
		this.gamepadInputHandler = gamepadInputHandler;
	}

	/**
	 * Initialize all Steam interfaces
	 */
	public void initialize() {
		userStats = StatsControllerFactory.buildSteam();
		steamUser = new SteamUser(null);
		steamFriends = new SteamFriends(new FriendsCallback());
		steamController = new SteamController();
		gamepadInputHandler.setSteamController(steamController);
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
		userStats.dispose();
	}

	@Override
	public SteamGamepadInputHandler getGamepadInputHandler() {
		return gamepadInputHandler;
	}

	@Override
	public String getSteamID() {
		return String.valueOf(steamUser.getSteamID().getAccountID());
	}

	@Override
	public void setRichPresentsVariable(String key, String value) {
		steamFriends.setRichPresence(key, value);
	}

	@Override
	public void setRichPresents(String key) {
		steamFriends.setRichPresence("steam_display", key);
	}

	@Override
	public StatsController getStatsController() {
		return userStats;
	}

}
