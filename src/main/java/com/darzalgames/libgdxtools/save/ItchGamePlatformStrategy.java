package com.darzalgames.libgdxtools.save;

import com.badlogic.gdx.Gdx;
import com.darzalgames.libgdxtools.platform.agnostic.DummyPlatformStrategy;

/**
 * A simple PlatformStrategy for use on Itch
 */
public class ItchGamePlatformStrategy extends DummyPlatformStrategy {

	@Override
	public void initialize() {
		Gdx.app.log("PLATFORM", "Itch.io Platform initialized");
	}

	@Override
	public String getPlayersSaveFolderName() {
		return "itchUser";
	}

}
