package com.darzalgames.libgdxtools.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.steam.SteamConnection;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;

public class LinuxGamePlatform implements GamePlatform {

	@Override
	public boolean supportsBorderlessFullscreen() {
		return false;
	}
	
	@Override
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		return Gdx.files.external(".local/share/" + fullGameAndSaveName);
	}
	
	@Override
	public SteamStrategy getSteamStrategy() {
		return SteamConnection.initializeStrategy();
	}
}
