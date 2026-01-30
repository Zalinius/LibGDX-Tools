package com.darzalgames.libgdxtools.steam.agnostic;

import java.util.function.Supplier;

import com.darzalgames.libgdxtools.statistics.StatsController;
import com.darzalgames.libgdxtools.statistics.StatsControllerFactory;
import com.darzalgames.libgdxtools.ui.input.handler.FallbackGamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.SteamGamepadInputHandler;

/**
 * Used when Steam isn't connected (e.g. running a dev build or one off of itch.io)
 */
public abstract class DummyPlatformStrategy implements PlatformStrategy {

	private FallbackGamepadInputHandler gamepadInputHandler;

	@Override
	public void initializeInput(Supplier<FallbackGamepadInputHandler> makeFallbackGamepadInputHandler, Supplier<SteamGamepadInputHandler> makeSteamGamepadInputHandler) {
		gamepadInputHandler = makeFallbackGamepadInputHandler.get();
	}

	@Override
	public void update() { /* Dummy strategy does nothing */ }

	@Override
	public void dispose() { /* Dummy strategy does nothing */ }

	@Override
	public void setRichPresentsVariable(String key, String value) { /* Dummy strategy does nothing */ }

	@Override
	public void setRichPresents(String key) { /* Dummy strategy does nothing */ }

	@Override
	public GamepadInputHandler getGamepadInputHandler() {
		return gamepadInputHandler;
	}

	@Override
	public StatsController getStatsController() {
		return StatsControllerFactory.buildDummy();
	}
}
