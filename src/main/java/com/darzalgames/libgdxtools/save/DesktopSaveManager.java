package com.darzalgames.libgdxtools.save;

import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.maingame.GameInfo;

public interface DesktopSaveManager extends SaveManager {

	/**
	 * Creates a filehandle for game saves.<br>
	 * This method is designed for games that use a single overarching file for saves.
	 * @param gameName The name of the game, spaces are okay
	 * @return The actively-used save game file, named and pathed using the gameName
	 */
	default FileHandle getSingleFileSaveFile(String gameName) {
		return getSaveFileByOperatingSystem(getSaveName(false, gameName), gameName);
	}

	/**
	 * Creates a filehandle for a specific subsystem game save, i.e. a file for current run progress, or for meta-progression.<br>
	 * This method is designed for games that use multiple split files for saves.
	 * @param gameName      The name of the game, spaces are okay
	 * @param subsystemName The name of the subsystem of the game, spaces are okay.
	 * @return The actively-used subsystem save game file, named and pathed using the gameName and subsystemName
	 */
	default FileHandle getSubsystemFileSaveFile(String gameName, String subsystemName) {
		return getSaveFileByOperatingSystem(getSubsystemSaveName(false, gameName, subsystemName), gameName);
	}

	/**
	 * Creates a filehandle for game save backups.<br>
	 * This method is designed for games that use a single overarching file for saves.
	 * @param gameName The name of the game, spaces are okay
	 * @return The BACKUP save game file, named and pathed using the gameName
	 */
	default FileHandle getSingleFileBackupSaveFile(String gameName) {
		return getSaveFileByOperatingSystem(getSaveName(true, gameName), gameName);
	}

	/**
	 * Creates a filehandle for a specific subsystem game save backup, i.e. a file for current run progress, or for meta-progression.<br>
	 * This method is designed for games that use multiple split files for saves.
	 * @param gameName      The name of the game, spaces are okay
	 * @param subsystemName The name of the subsystem of the game, spaces are okay.
	 * @return The BACKUP subsystem save game file, named and pathed using the gameName and subsystemName
	 */
	default FileHandle getSubsystemFileBackupSaveFile(String gameName, String subsystemName) {
		return getSaveFileByOperatingSystem(getSubsystemSaveName(true, gameName, subsystemName), gameName);
	}

	static String getSaveName(boolean isBackup, String gameName) {
		String suffix = isBackup ? "-backup" : "";
		return gameName.replace(" ", "").trim()
				+ "Save"
				+ suffix
				+ ".json";
	}

	static String getSubsystemSaveName(boolean isBackup, String gameName, String subsystemName) {
		String suffix = isBackup ? "-backup" : "";
		return gameName.replace(" ", "").trim()
				+ subsystemName.replace(" ", "").trim()
				+ "Save"
				+ suffix
				+ ".json";
	}

	private FileHandle getSaveFileByOperatingSystem(String saveFileName, String gameName) {
		String fullGameAndSaveName = gameName + "/" + GameInfo.getPlatformStrategy().getPlayersSaveFolderName() + "/" + saveFileName;
		return GameInfo.getOperatingSystem().getSaveFileLocation(fullGameAndSaveName);
	}

}
