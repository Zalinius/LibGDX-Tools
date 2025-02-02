package com.darzalgames.libgdxtools.platform;

import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.steam.agnostic.DummySteamStrategy;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputReceiver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class WebGamePlatform implements GamePlatform {
	
	@Override
	public boolean needsQuitButton() {
		return false;
	}
	
	@Override
	public boolean supportsBorderlessFullscreen() {
		return false;//TODO or does it??
	}
	
	@Override
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SteamStrategy getSteamStrategy(InputStrategySwitcher inputStrategySwitcher, InputReceiver inputReceiver) {
		return new DummySteamStrategy(GamePlatform.makeFallbackGamepadInputHandlerSupplier(inputStrategySwitcher, inputReceiver).get());
	}

	@Override
	public String getPlatformName() {
		return GamePlatform.WEB;
	}
	
	@Override
	public boolean isDevMode() {
		return false;
	}
}
