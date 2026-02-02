package com.darzalgames.libgdxtools.platform;

import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.steam.agnostic.DummySteamStrategy;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public class WebGamePlatform implements GamePlatform {

	@Override
	public boolean needsQuitButton() {
		return false;
	}

	@Override
	public boolean supportsBorderlessFullscreen() {
		return false;// TODO or does it??
	}

	@Override
	public boolean toggleFullScreenWithF11() {
		return false;
	}

	@Override
	public FileHandle getOldSaveFileLocation(String fullGameAndSaveName) {
		return null;
	}

	@Override
	public FileHandle getNewSaveFileLocation(String fullGameAndSaveName) {
		return null;
	}

	@Override
	public SteamStrategy getSteamStrategy(InputStrategyManager inputStrategyManager) {
		return new DummySteamStrategy(GamePlatform.makeFallbackGamepadInputHandlerSupplier(inputStrategyManager).get());
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
