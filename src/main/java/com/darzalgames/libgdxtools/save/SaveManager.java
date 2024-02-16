package com.darzalgames.libgdxtools.save;

import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.steam.SteamConnection;

public interface SaveManager {

	public void save();
	public boolean load();

	/**
	 * @param gameName The name of the game, spaces are okay 
	 * @return The actively-used save game file, named and pathed using the game's name
	 */
	default FileHandle getSaveFile(String gameName) {
		return getSaveFileByOperatingSystem(getSaveName(false, gameName), gameName);
	}

	/**
	 * @param gameName The name of the game, spaces are okay 
	 * @return The BACKUP save game file, named and pathed using the game's name
	 */
	default FileHandle getBackupSaveFile(String gameName) {
		return getSaveFileByOperatingSystem(getSaveName(true, gameName), gameName);
	}

	private String getSaveName(boolean isBackup, String gameName) {
		String suffix = isBackup ? "-backup" : "";
		return gameName.replace(" ", "").trim() + "Save" + suffix + ".json";
	}

	private FileHandle getSaveFileByOperatingSystem(String saveFileName, String gameName) {
		String fullGameAndSaveName = gameName + "/" + SteamConnection.getSteamID() + "/" + saveFileName;
		return GameInfo.getGamePlatform().getSaveFileLocation(fullGameAndSaveName);
	}

}
