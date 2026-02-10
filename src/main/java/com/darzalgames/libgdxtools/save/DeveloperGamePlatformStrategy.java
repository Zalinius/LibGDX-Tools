package com.darzalgames.libgdxtools.save;

import com.badlogic.gdx.Gdx;
import com.darzalgames.libgdxtools.platform.agnostic.DummyPlatformStrategy;

/**
 * A simple PlatformStrategy for use when developping
 * It's the fallback/default Platform
 */
public class DeveloperGamePlatformStrategy extends DummyPlatformStrategy {

	@Override
	public void initialize() {
		Gdx.app.log("PLATFORM", "Developer Platform initialized");
	}

	@Override
	public String getPlayersSaveFolderName() {
		return "dev";
	}

}
