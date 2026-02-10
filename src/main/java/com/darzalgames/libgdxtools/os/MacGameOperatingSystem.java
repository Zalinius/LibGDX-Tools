package com.darzalgames.libgdxtools.os;

import org.lwjgl.system.Configuration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class MacGameOperatingSystem implements GameOperatingSystem {

	@Override
	public boolean supportsBorderlessFullscreen() {
		return false;
	}

	@Override
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		return Gdx.files.external("Library/Application Support/" + fullGameAndSaveName);
	}

	@Override
	public String getOperatingSystemName() {
		return GameOperatingSystem.MAC;
	}

	@Override
	public void operatingSystemSpecificOpenGlInitialization() {
		// Mac and java have an issue with the OpenGL rendering thread not being the main thread
		// See https://github.com/libgdx/libgdx/pull/7361
		Configuration.GLFW_CHECK_THREAD0.set(false);
		Configuration.GLFW_LIBRARY_NAME.set("glfw_async");
	}

}
