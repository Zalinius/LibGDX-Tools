package com.darzalgames.libgdxtools.steam;

import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.*;
import com.darzalgames.libgdxtools.statistics.StatsController;
import com.darzalgames.libgdxtools.statistics.StatsControllerFactory;
import com.darzalgames.libgdxtools.steam.agnostic.PlatformStrategy;
import com.darzalgames.libgdxtools.ui.input.handler.FallbackGamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.SteamGamepadInputHandler;

public class SteamPlatformStrategy implements PlatformStrategy {

	private SteamUser steamUser;
	private StatsController userStats;
	private SteamFriends steamFriends;

	private SteamGamepadInputHandler gamepadInputHandler;
	private SteamController steamController;

	/**
	 * Initialize all Basic interfaces
	 */
	@Override
	public void initialize() {
		steamUser = new SteamUser(null);
		userStats = StatsControllerFactory.buildSteam();
		steamFriends = new SteamFriends(new FriendsCallback());
		Gdx.app.log("PLATFORM", "Steam Platform initialized");
	}

	@Override
	public void initializeInput(Supplier<FallbackGamepadInputHandler> makeFallbackGamepadInputHandler, Supplier<SteamGamepadInputHandler> makeSteamGamepadInputHandler) {
		gamepadInputHandler = makeSteamGamepadInputHandler.get();
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
	public String getPlayersSaveFolderName() {
		return getSteamID();
	}

	/**
	 * Get the ID unique to the current user, useful for differentiating save files
	 * or for use as a seed when making interesting game features (e.g. generating a player portrait)
	 * @return The ID
	 */
	public String getSteamID() {
		return String.valueOf(steamUser.getSteamID().getAccountID());
	}

	@Override
	public SteamGamepadInputHandler getGamepadInputHandler() {
		return gamepadInputHandler;
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
