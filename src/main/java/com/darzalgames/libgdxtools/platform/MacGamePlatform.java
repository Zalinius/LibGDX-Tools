package com.darzalgames.libgdxtools.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class MacGamePlatform extends GenericDesktopGamePlatform {

	@Override
	public boolean supportsBorderlessFullscreen() {
		return false;
	}

	@Override
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		return Gdx.files.absolute(System.getProperty("user.home") + "/Documents/My Games/"+ fullGameAndSaveName);
	}

	@Override
	public String getPlatformName() {
		return GamePlatform.MAC;
	}
}
