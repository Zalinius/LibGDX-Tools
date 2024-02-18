package com.darzalgames.libgdxtools.platform;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.codedisaster.steamworks.SteamController;
import com.codedisaster.steamworks.SteamControllerDigitalActionHandle;
import com.darzalgames.libgdxtools.steam.SteamConnection;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.handler.SteamGamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public abstract class GenericDesktopGamePlatform implements GamePlatform {

	@Override
	public SteamStrategy getSteamStrategy(InputStrategyManager inputStrategyManager) {
		Supplier<SteamGamepadInputHandler> makeSteamGamepadInputHandler = () -> new SteamGamepadInputHandler(inputStrategyManager, null) {
			// Don't be using this default does-nothing SteamGamepadInputHandler, this is mainly here for the LibGDXTools TestGame which isn't on Steam
			@Override
			protected Map<SteamControllerDigitalActionHandle, Input> makeButtonMappings(SteamController steamController) {
				return Collections.emptyMap();
			}

			@Override
			protected List<Input> getTrackedInputs() {
				return Collections.emptyList();
			}

		};
		return SteamConnection.initializeStrategy(GamePlatform.makeFallbackGamepadInputHandlerSupplier(inputStrategyManager), makeSteamGamepadInputHandler);
	}
}
