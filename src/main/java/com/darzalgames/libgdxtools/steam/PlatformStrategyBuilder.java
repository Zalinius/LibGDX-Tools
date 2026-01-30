package com.darzalgames.libgdxtools.steam;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;
import com.darzalgames.libgdxtools.save.DevelopperGamePlatformStrategy;
import com.darzalgames.libgdxtools.save.ItchGamePlatformStrategy;
import com.darzalgames.libgdxtools.steam.agnostic.PlatformStrategy;

public class PlatformStrategyBuilder {

	private PlatformStrategyBuilder() {}

	public static PlatformStrategy initializeGamePlatform(boolean isItch) {
		PlatformStrategy platformStrategy;

		if (isItch) {
			platformStrategy = new ItchGamePlatformStrategy();
		} else {
			try {
				SteamAPI.loadLibraries();
				boolean steamInitialized = SteamAPI.init();
				if (steamInitialized) {
					platformStrategy = new SteamPlatformStrategy();
				} else {
					platformStrategy = new DevelopperGamePlatformStrategy();
				}
			} catch (SteamException e) {
				platformStrategy = new DevelopperGamePlatformStrategy();
			}
		}
		platformStrategy.initialize();
		return platformStrategy;
	}

}
