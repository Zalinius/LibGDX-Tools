package com.darzalgames.libgdxtools.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class WindowsGamePlatform extends GenericDesktopGamePlatform {

	@Override
	public boolean supportsBorderlessFullscreen() {
		return true;
	}

	@Override
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		return Gdx.files.absolute(System.getenv("APPDATA") + "/" + fullGameAndSaveName);
	}

	@Override
	public String getPlatformName() {
		return GamePlatform.WINDOWS;
	}
}
