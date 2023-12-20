package com.darzalgames.libgdxtools.save;

import java.util.Locale;

import javax.swing.filechooser.FileSystemView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.steam.SteamConnection;

public interface SaveManager {

	public void save();

	public boolean load();

	default FileHandle getSaveFile() {
		return getOperatingSystemType().getSave();
	}

	default FileHandle getBackupSaveFile() {
		return getOperatingSystemType().getBackupSave();
	}

	public enum OSType { 
		WINDOWS,
		LINUX,
		;

		private final transient String saveName = "QuestGiverSave.json";
		private final transient String backupSaveName = "QuestGiverSave-backup.json";

		public FileHandle getBackupSave() {
			return getSave(backupSaveName);
		}

		public FileHandle getSave() {
			return getSave(saveName);
		}

		private FileHandle getSave(String name) {
			switch (this) {
			case WINDOWS:
				return Gdx.files.absolute(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/My Games/Quest Giver/" + SteamConnection.getSteamID() + "/" + name);
			case LINUX:
				return Gdx.files.external(".local/share/Quest Giver/" + SteamConnection.getSteamID() + "/" + name);
			default:
				return null;
			}
		}

	}

	// thanks https://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
	/**
	 * detect the operating system from the os.name System property and cache
	 * the result
	 * 
	 * @return - the operating system detected
	 */
	public static OSType getOperatingSystemType() {
		OSType detectedOS = OSType.WINDOWS;
		String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if ((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0)) {
			// This is Mac, which we don't support
		} else if (os.indexOf("win") >= 0) {
			// This was our default assumption
		} else if (os.indexOf("nux") >= 0) {
			detectedOS = OSType.LINUX;
		}
		return detectedOS;
	}

}
