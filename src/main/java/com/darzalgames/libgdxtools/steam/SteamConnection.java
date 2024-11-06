package com.darzalgames.libgdxtools.steam;

import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;
import com.darzalgames.libgdxtools.steam.agnostic.DummySteamStrategy;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.input.handler.FallbackGamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.SteamGamepadInputHandler;

public class SteamConnection {

	private static final String STEAM_CONNECTED = "[STEAM]";
	private static final String STEAM_NOT_CONNECTED = "[STEAM (NOT CONNECTED)]";
	
	private static boolean steamInitialized;

	private SteamConnection() {}
	
	public static SteamStrategy initializeStrategy(
			Supplier<FallbackGamepadInputHandler> makeFallbackGamepadInputHandler,
			Supplier<SteamGamepadInputHandler> makeSteamGamepadInputHandler) {
		try {
			SteamAPI.loadLibraries();
			steamInitialized = SteamAPI.init();
			if(steamInitialized) {
				ConnectedSteamStrategy connectedSteamStrategy = new ConnectedSteamStrategy(makeSteamGamepadInputHandler.get());
				connectedSteamStrategy.initialize();
				return connectedSteamStrategy;
			}
			else {
				Gdx.app.error(STEAM_NOT_CONNECTED, "Could not initialize Steam. Is Steam running?");
				return new DummySteamStrategy(makeFallbackGamepadInputHandler.get());
			}
		} catch (SteamException e) {
			Gdx.app.error(STEAM_NOT_CONNECTED, "Could not load Steam libraries. Is Steam running?");
			return new DummySteamStrategy(makeFallbackGamepadInputHandler.get());
		}
	}
	
	public static boolean isSteamConnected() {
		return steamInitialized;
	}

}
