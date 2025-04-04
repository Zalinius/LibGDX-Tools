package com.darzalgames.libgdxtools.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LinuxGamePlatform extends GenericDesktopGamePlatform {

	@Override
	public boolean supportsBorderlessFullscreen() {
		return false;
	}

	@Override
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		return Gdx.files.external(".local/share/" + fullGameAndSaveName);
	}

	@Override
	public String getPlatformName() {
		return GamePlatform.LINUX;
	}
}
