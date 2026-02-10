package com.darzalgames.libgdxtools.save;

import com.badlogic.gdx.Gdx;
import com.darzalgames.libgdxtools.platform.agnostic.DummyPlatformStrategy;

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
