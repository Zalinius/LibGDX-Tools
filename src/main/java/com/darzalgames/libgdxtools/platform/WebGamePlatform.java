package com.darzalgames.libgdxtools.platform;

import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.steam.agnostic.DummySteamStrategy;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;

public class WebGamePlatform implements GamePlatform{
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
	public SteamStrategy getSteamStrategy() {
		return new DummySteamStrategy();
	}
}
