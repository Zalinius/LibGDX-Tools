package com.darzalgames.libgdxtools.save;

import com.badlogic.gdx.Gdx;
import com.darzalgames.libgdxtools.steam.agnostic.DummyPlatformStrategy;

public class DevelopperGamePlatformStrategy extends DummyPlatformStrategy {

	@Override
	public void initialize() {
		Gdx.app.log("PLATFORM", "Developper Platform initialized");
	}

	@Override
	public String getPlayersSaveFolderName() {
		return "dev";
	}

}
