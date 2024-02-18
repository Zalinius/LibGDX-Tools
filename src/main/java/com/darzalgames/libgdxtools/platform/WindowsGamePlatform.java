package com.darzalgames.libgdxtools.platform;

import javax.swing.filechooser.FileSystemView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.steam.SteamConnection;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;

public class WindowsGamePlatform implements GamePlatform {

	@Override
	public boolean supportsBorderlessFullscreen() {
		return true;
	}
	
	@Override
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		return Gdx.files.absolute(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/My Games/"+ fullGameAndSaveName);
	}
	
	@Override
	public SteamStrategy getSteamStrategy() {
		return SteamConnection.initializeStrategy();
	}
}
