package com.darzalgames.libgdxtools.ui.input.handler;

public class SteamControllerManager {

	private static SteamGamepadInputHandler steamGamepadInputHandler;

	private SteamControllerManager() {}

	public static void initialize(SteamGamepadInputHandler steamGamepadInputHandler) {
		SteamControllerManager.steamGamepadInputHandler = steamGamepadInputHandler;
	}

	/**
	 * Only works when playing through Steam, otherwise does nothing
	 */
	public static void openControlsOverlay() {
		if (steamGamepadInputHandler != null) {
			// Is null unless booted via Steam
			steamGamepadInputHandler.openControlsOverlay();
		}
	}
}
