package com.darzalgames.libgdxtools.steam;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;
import com.darzalgames.libgdxtools.steam.agnostic.DummySteamStrategy;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;

public class SteamConnection {

	private static final String STEAM_CONNECTED = "[STEAM]";
	private static final String STEAM_NOT_CONNECTED = "[STEAM (NOT CONNECTED)]";

	private SteamConnection() {}
	
	public static SteamStrategy initializeStrategy() {
		try {
			SteamAPI.loadLibraries();
			boolean steamInitialized = SteamAPI.init();
			if(steamInitialized) {
				ConnectedSteamStrategy connectedSteamStrategy = new ConnectedSteamStrategy();
				connectedSteamStrategy.initialize();
				return connectedSteamStrategy;
			}
			else {
				Gdx.app.error(STEAM_NOT_CONNECTED, "Could not initialize Steam. Is Steam running?");
				return new DummySteamStrategy();
			}
		} catch (SteamException e) {
			Gdx.app.error(STEAM_NOT_CONNECTED, "Could not load Steam libraries. Is Steam running?");
			return new DummySteamStrategy();
		}
	}

}
