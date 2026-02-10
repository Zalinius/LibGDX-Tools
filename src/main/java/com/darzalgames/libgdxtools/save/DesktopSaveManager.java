package com.darzalgames.libgdxtools.save;

import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.os.GameOperatingSystem;

public abstract class DesktopSaveManager implements SaveManager {

	private final String gameName;
	private final String developerName;
	private final GameOperatingSystem operatingSystem;

	protected DesktopSaveManager(String gameName, String developerName, GameOperatingSystem operatingSystem) {
		this.gameName = gameName.replace(" ", "").trim();
		this.developerName = developerName.replace(" ", "").trim();
		this.operatingSystem = operatingSystem;
	}

	/**
	 * Creates a filehandle for a specific subsystem game save, i.e. a file for current run progress, or for meta-progression.<br>
	 * This method is designed for games that use multiple split files for saves.
	 * @param subsystemName The name of the subsystem of the game, spaces are okay.
	 * @return The actively-used subsystem save game file, named and pathed using the gameName and subsystemName
	 */
	protected FileHandle getSubsystemFileSaveFile(String subsystemName) {
		String fileName = getSubsystemSaveFileName(subsystemName);
		return getSaveFileHandleByOperatingSystem(fileName);
	}

	/**
	 * Creates a filehandle for a specific subsystem game save backup, i.e. a file for current run progress, or for meta-progression.<br>
	 * This method is designed for games that use multiple split files for saves.
	 * @param subsystemName The name of the subsystem of the game, spaces are okay.
	 * @return The BACKUP subsystem save game file, named and pathed using the gameName and subsystemName
	 */
	protected FileHandle getSubsystemFileBackupSaveFile(String subsystemName) {
		String fileName = getSubsystemBackupSaveFileName(subsystemName);
		return getSaveFileHandleByOperatingSystem(fileName);
	}

	String getSubsystemSaveFileName(String subsystemName) {
		subsystemName = subsystemName.replace(" ", "").trim();
		return gameName
				+ subsystemName
				+ ".json";
	}

	String getSubsystemBackupSaveFileName(String subsystemName) {
		subsystemName = subsystemName.replace(" ", "").trim();
		return gameName
				+ subsystemName
				+ "-backup"
				+ ".json";
	}

	private FileHandle getSaveFileHandleByOperatingSystem(String saveFileName) {
		String fullGameAndSaveName = gameName + "-" + developerName + "/" + GameInfo.getPlatformStrategy().getPlayersSaveFolderName() + "/" + saveFileName;
		return operatingSystem.getSaveFileLocation(fullGameAndSaveName);
	}

}
