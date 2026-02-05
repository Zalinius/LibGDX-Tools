package com.darzalgames.libgdxtools.save;

import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.maingame.GameInfo;

public interface DesktopSaveManager extends SaveManager {

	/**
	 * @param gameName The name of the game, spaces are okay
	 * @return The actively-used save game file, named and pathed using the game's name
	 */
	default FileHandle getOldSaveFile(String gameName) {
		return getSaveFileByOperatingSystemOld(getSaveNameWithGameName(false, gameName), gameName);
	}

	/**
	 * @param gameName The name of the game, spaces are okay
	 * @return The BACKUP save game file, named and pathed using the game's name
	 */
	default FileHandle getOldBackupSaveFile(String gameName) {
		return getSaveFileByOperatingSystemOld(getSaveNameWithGameName(true, gameName), gameName);
	}

	/**
	 * @param gameName The name of the game, spaces are okay
	 * @return The actively-used save game file, named and pathed using the game's name
	 */
	default FileHandle getNewSaveFile(String gameName, String companyName) {
		return getSaveFileByOperatingSystemNew(getSaveNameWithGameName(false, gameName), gameName, companyName);
	}

	/**
	 * @param gameName The name of the game, spaces are okay
	 * @return The BACKUP save game file, named and pathed using the game's name
	 */
	default FileHandle getNewBackupSaveFile(String gameName, String companyName) {
		return getSaveFileByOperatingSystemNew(getSaveNameWithGameName(true, gameName), gameName, companyName);
	}

	private String getSaveNameWithGameName(boolean isBackup, String gameName) {
		String suffix = isBackup ? "-backup" : "";
		return gameName.replace(" ", "").trim() + "Save" + suffix + ".json";
	}

	private FileHandle getSaveFileByOperatingSystemOld(String saveFileName, String gameName) {
		String fullGameAndSaveName = gameName + "/" + GameInfo.getSteamStrategy().getSteamID() + "/" + saveFileName;
		return GameInfo.getGamePlatform().getOldSaveFileLocation(fullGameAndSaveName);
	}

	private FileHandle getSaveFileByOperatingSystemNew(String saveFileName, String gameName, String companyName) {
		String fullGameAndSaveName = gameName + " - " + companyName + "/" + GameInfo.getSteamStrategy().getSteamID() + "/" + saveFileName;
		return GameInfo.getGamePlatform().getNewSaveFileLocation(fullGameAndSaveName);
	}

}
