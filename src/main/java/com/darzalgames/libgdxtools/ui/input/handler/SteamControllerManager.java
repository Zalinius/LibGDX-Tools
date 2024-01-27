package com.darzalgames.libgdxtools.ui.input.handler;

import com.codedisaster.steamworks.SteamAPI;

public class SteamControllerManager {

	private static SteamGamepadInputHandler steamGamepadInputHandler;

	private SteamControllerManager() {}

	public static void initialize(SteamGamepadInputHandler steamGamepadInputHandler) {
		SteamControllerManager.steamGamepadInputHandler = steamGamepadInputHandler;
	}

	public static void openControlsOverlay() {
		if (SteamAPI.isSteamRunning()) {
			steamGamepadInputHandler.openControlsOverlay();
		}
	}
}
