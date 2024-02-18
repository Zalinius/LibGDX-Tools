package com.darzalgames.libgdxtools.steam.agnostic;

import com.codedisaster.steamworks.SteamController;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;

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
	public void acceptController(GamepadInputHandler gamepadInputHandler) {}
	
	@Override
	public void giveAchievement(String achievement) {}
	
	@Override
	public void setRichPresentsVariable(String key, String value) {}
	
	@Override
	public void setRichPresents(String key) {}
}
