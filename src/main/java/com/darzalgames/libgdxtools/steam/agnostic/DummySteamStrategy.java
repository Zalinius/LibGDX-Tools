package com.darzalgames.libgdxtools.steam.agnostic;

import com.codedisaster.steamworks.SteamController;

public class DummySteamStrategy implements SteamStrategy {

	@Override
	public void update() {}
	
	@Override
	public void dispose() {}
	
	@Override
	public int getStat(String stat) {
		return -1;
	}

	@Override
	public long getGlobalStat(String stat) {
		return -1;
	}
	
	@Override
	public void setStat(String stat, int val) {}
	
	@Override
	public String getSteamID() {
		return "dev";
	}
	
	@Override
	public SteamController getSteamController() {
		return null;
	}
	
	@Override
	public void giveAchievement(String achievement) {}
	
	@Override
	public void setRichPresentsVariable(String key, String value) {}
	
	@Override
	public void setRichPresents(String key) {}
}
