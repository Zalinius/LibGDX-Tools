package com.darzalgames.libgdxtools.ui.input.handler;

public class SteamControllerManager {

	private static SteamGamepadInputHandler steamGamepadInputHandler;

	private SteamControllerManager() {}

	public static void initialize(SteamGamepadInputHandler steamGamepadInputHandler) {
		SteamControllerManager.steamGamepadInputHandler = steamGamepadInputHandler;
	}

	public static void openControlsOverlay() {
		steamGamepadInputHandler.openControlsOverlay();
	}
}
