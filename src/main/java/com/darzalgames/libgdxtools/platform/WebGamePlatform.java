package com.darzalgames.libgdxtools.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.steam.agnostic.DummySteamStrategy;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public class WebGamePlatform implements GamePlatform {
	@Override
	public boolean supportsBorderlessFullscreen() {
		return false;//TODO or does it??
	}
	
	@Override
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		throw new RuntimeException("Not implemented lol");
	}
	
	@Override
	public SteamStrategy getSteamStrategy(InputStrategyManager inputStrategyManager) {
		return new DummySteamStrategy(GamePlatform.makeFallbackGamepadInputHandlerSupplier(inputStrategyManager).get());
	}
	
	@Override
	public SaveManager makeSaveManager() {
		return new SaveManager() {
			
			@Override
			public void save() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean load() {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}
}
