package com.darzalgames.libgdxtools.save;

import com.codedisaster.steamworks.SteamUser;

public class SteamFileLocationStrategy implements FileLocationStrategy {

	private SteamUser steamUser;

	/**
	 * Initialize all Steam interfaces
	 */
	public void initialize() {
		steamUser = new SteamUser(null);
	}

	@Override
	public String getPlayersSaveFolder() {
		return String.valueOf(steamUser.getSteamID().getAccountID());
	}

}
