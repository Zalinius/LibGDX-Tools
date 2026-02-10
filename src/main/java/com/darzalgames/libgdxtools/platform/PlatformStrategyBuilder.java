package com.darzalgames.libgdxtools.platform;

import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamException;
import com.darzalgames.libgdxtools.platform.steam.SteamPlatformStrategy;
import com.darzalgames.libgdxtools.save.DeveloperGamePlatformStrategy;
import com.darzalgames.libgdxtools.save.ItchGamePlatformStrategy;

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
					platformStrategy = new DeveloperGamePlatformStrategy();
				}
			} catch (SteamException e) {
				platformStrategy = new DeveloperGamePlatformStrategy();
			}
		}
		platformStrategy.initialize();
		return platformStrategy;
	}

}
