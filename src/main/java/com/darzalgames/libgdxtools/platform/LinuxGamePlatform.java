package com.darzalgames.libgdxtools.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LinuxGamePlatform extends GenericDesktopGamePlatform {

	@Override
	public boolean supportsBorderlessFullscreen() {
		return false;
	}

	@Override
	public FileHandle getOldSaveFileLocation(String fullGameAndSaveName) {
		return Gdx.files.external(".local/share/" + fullGameAndSaveName);
	}

	@Override
	public FileHandle getNewSaveFileLocation(String fullGameAndSaveName) {
		return getOldSaveFileLocation(fullGameAndSaveName);
	}

	@Override
	public String getPlatformName() {
		return GamePlatform.LINUX;
	}
}
