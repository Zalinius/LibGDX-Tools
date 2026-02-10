package com.darzalgames.libgdxtools.os;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class WindowsGameOperatingSystem implements GameOperatingSystem {

	@Override
	public boolean supportsBorderlessFullscreen() {
		return true;
	}

	@Override
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		return Gdx.files.absolute(System.getenv("APPDATALOCAL") + "/" + fullGameAndSaveName);
	}

	@Override
	public String getOperatingSystemName() {
		return GameOperatingSystem.WINDOWS;
	}
}
