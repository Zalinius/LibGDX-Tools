package com.darzalgames.libgdxtools.platform;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.codedisaster.steamworks.SteamController;
import com.codedisaster.steamworks.SteamControllerDigitalActionHandle;
import com.darzalgames.darzalcommon.data.BiMap;
import com.darzalgames.libgdxtools.errorhandling.CrashHandler;
import com.darzalgames.libgdxtools.errorhandling.DesktopCrashHandler;
import com.darzalgames.libgdxtools.steam.SteamConnection;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.handler.SteamGamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputReceiver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public abstract class GenericDesktopGamePlatform implements GamePlatform {

	@Override
	public SteamStrategy getSteamStrategy(InputStrategySwitcher inputStrategySwitcher, InputReceiver inputReceiver) {
		Supplier<SteamGamepadInputHandler> makeSteamGamepadInputHandler = () -> new SteamGamepadInputHandler(inputStrategySwitcher, inputReceiver, null) {
			// Don't be using this default does-nothing SteamGamepadInputHandler, this is mainly here for the LibGDXTools TestGame which isn't on Steam
			@Override
			protected BiMap<SteamControllerDigitalActionHandle, Input> makeButtonMappings(SteamController steamController) {
				return new BiMap<>();
			}

			@Override
			protected List<Input> getTrackedInputs() {
				return Collections.emptyList();
			}

			@Override
			protected void sendAxisInput() {
				// Do nothing
			}

			@Override
			protected Texture getTextureFromDescriptor(AssetDescriptor<Texture> descriptor) {
				return null;
			}

		};
		return SteamConnection.initializeStrategy(GamePlatform.makeFallbackGamepadInputHandlerSupplier(inputStrategySwitcher, inputReceiver), makeSteamGamepadInputHandler);
	}
	
	@Override
	public CrashHandler getCrashTool() {
		return new DesktopCrashHandler();
	}
	
	@Override
	public boolean isDevMode() {
		return !SteamConnection.isSteamConnected();
	}
}
