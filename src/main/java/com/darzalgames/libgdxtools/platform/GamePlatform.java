package com.darzalgames.libgdxtools.platform;

import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;

public interface GamePlatform {
	
	/**
	 * @return True if the active game platform supports borderless fullsreen
	 */
	public boolean supportsBorderlessFullscreen();
	
	/**
	 * Return the correct directory for save files based on the platform
	 * @param fullGameAndSaveName The subpath and name of the desired save file
	 * @return A libgdx file handle to the save file
	 */
	public FileHandle getSaveFileLocation(String fullGameAndSaveName);
	
	/**
	 * @return A SteamStrategy befitting the GamePlatform
	 */
	public SteamStrategy getSteamStrategy();

}
