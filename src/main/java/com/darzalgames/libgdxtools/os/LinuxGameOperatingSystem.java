package com.darzalgames.libgdxtools.os;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LinuxGameOperatingSystem implements GameOperatingSystem {

	@Override
	public boolean supportsBorderlessFullscreen() {
		return false;
	}

	@Override
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		return Gdx.files.external(".local/share/" + fullGameAndSaveName);
	}

	@Override
	public String getOperatingSystemName() {
		return GameOperatingSystem.LINUX;
	}
}
