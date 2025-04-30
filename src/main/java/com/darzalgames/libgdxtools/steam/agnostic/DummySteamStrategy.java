package com.darzalgames.libgdxtools.steam.agnostic;

import com.darzalgames.libgdxtools.ui.input.handler.FallbackGamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;

/**
 * Used when Steam isn't connected (e.g. running a dev build or one off of itch.io)
 */
public class DummySteamStrategy implements SteamStrategy {

	private final FallbackGamepadInputHandler gamepadInputHandler;

	public DummySteamStrategy(FallbackGamepadInputHandler gamepadInputHandler) {
		this.gamepadInputHandler = gamepadInputHandler;
	}

	@Override
	public void update() { /* Dummy strategy does nothing */ }

	@Override
	public void dispose() { /* Dummy strategy does nothing */ }

	@Override
	public int getStat(String stat) {
		return -1;
	}

	@Override
	public long getGlobalStat(String stat) {
		return -1;
	}

	@Override
	public void setStat(String stat, int val) { /* Dummy strategy does nothing */ }

	@Override
	public String getSteamID() {
		return "dev";
	}

	@Override
	public void giveAchievement(String achievement) { /* Dummy strategy does nothing */ }

	@Override
	public void setRichPresentsVariable(String key, String value) { /* Dummy strategy does nothing */ }

	@Override
	public void setRichPresents(String key) { /* Dummy strategy does nothing */ }

	@Override
	public GamepadInputHandler getGamepadInputHandler() {
		return gamepadInputHandler;
	}
}
