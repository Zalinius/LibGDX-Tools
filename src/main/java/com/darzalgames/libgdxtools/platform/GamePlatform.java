package com.darzalgames.libgdxtools.platform;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.handler.FallbackGamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputReceiver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public interface GamePlatform {

	String WINDOWS = "Windows";
	String LINUX = "Linux";
	String MAC = "Mac";

	/**
	 * @return The platform name
	 */
	String getPlatformName();

	/**
	 * @return True if the active game platform supports borderless fullscreen
	 */
	boolean supportsBorderlessFullscreen();

	/**
	 * Return the correct directory for save files based on the platform
	 * @param fullGameAndSaveName The sub-path and name of the desired save file
	 * @return A LibGDX file handle to the save file
	 */
	FileHandle getSaveFileLocation(String fullGameAndSaveName);

	/**
	 * @param inputStrategySwitcher The input strategy manager, so we can make the gamepad input handler
	 * @return A SteamStrategy befitting the GamePlatform
	 */
	SteamStrategy getSteamStrategy(InputStrategySwitcher inputStrategySwitcher, InputReceiver inputReceiver);

	/**
	 * @return True if a developer is playing, false if a player is playing
	 */
	boolean isDevMode();
	
	void platformSpecificOpenGlInitialization();

	static Supplier<FallbackGamepadInputHandler> makeFallbackGamepadInputHandlerSupplier(InputStrategySwitcher inputStrategySwitcher, InputReceiver inputReceiver) {
		return () -> new FallbackGamepadInputHandler(inputStrategySwitcher, inputReceiver) {
			@Override
			protected List<Input> getTrackedInputs() {
				List<Input> trackedInputs = new ArrayList<>();
				trackedInputs.add(Input.ACCEPT);
				trackedInputs.add(Input.BACK);
				trackedInputs.add(Input.PAUSE);
				trackedInputs.add(Input.UP);
				trackedInputs.add(Input.DOWN);
				trackedInputs.add(Input.LEFT);
				trackedInputs.add(Input.RIGHT);
				return trackedInputs;
			}

			@Override
			protected Map<Function<Controller, Integer>, Input> makeButtonMappings() {
				Map<Function<Controller, Integer>, Input> buttonMappings = new HashMap<>();
				buttonMappings.put(controller -> controller.getMapping().buttonA, Input.ACCEPT);
				buttonMappings.put(controller -> controller.getMapping().buttonB, Input.BACK);
				buttonMappings.put(controller -> controller.getMapping().buttonStart, Input.PAUSE);

				buttonMappings.put(controller -> controller.getMapping().buttonL1, Input.LEFT);
				buttonMappings.put(controller -> controller.getMapping().buttonR1, Input.RIGHT);
				buttonMappings.put(controller -> controller.getMapping().buttonDpadLeft, Input.LEFT);
				buttonMappings.put(controller -> controller.getMapping().buttonDpadRight, Input.RIGHT);
				buttonMappings.put(controller -> controller.getMapping().buttonDpadUp, Input.UP);
				buttonMappings.put(controller -> controller.getMapping().buttonDpadDown, Input.DOWN);
				return buttonMappings;
			}

			@Override
			protected Map<Input, AssetDescriptor<Texture>> makeGlyphMappings() {
				return new HashMap<>();
			}

			@Override
			protected Texture getTextureFromDescriptor(AssetDescriptor<Texture> descriptor) {
				return null;
			}

			@Override
			public void setActionSet(Supplier<String> newActionSetKeySupplier) {
				// Not implemented
			}
		};
	}
}
