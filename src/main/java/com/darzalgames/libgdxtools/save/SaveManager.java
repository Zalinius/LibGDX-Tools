package com.darzalgames.libgdxtools.save;

import java.util.Locale;

import javax.swing.filechooser.FileSystemView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.steam.SteamConnection;

public interface SaveManager {

	public void save();
	public boolean load();

	
	default FileHandle getSaveFile(String gameName) {
		return getOperatingSystemType().getSave(gameName);
	}

	default FileHandle getBackupSaveFile(String gameName) {
		return getOperatingSystemType().getBackupSave(gameName);
	}

	public enum OSType { 
		WINDOWS,
		LINUX,
		;
		
		private String getSaveName(boolean isBackup, String gameName) {
			String suffix = isBackup ? "-backup" : "";
			return gameName.replace(" ", "") + "Save" + suffix + ".json";
		}

		public FileHandle getBackupSave(String gameName) {
			return getSaveFile(getSaveName(true, gameName), gameName);
		}

		public FileHandle getSave(String gameName) {
			return getSaveFile(getSaveName(false, gameName), gameName);
		}

		private FileHandle getSaveFile(String saveFileName, String gameName) {
			switch (this) {
			case WINDOWS:
				return Gdx.files.absolute(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/My Games/"+ gameName + "/" + SteamConnection.getSteamID() + "/" + saveFileName);
			case LINUX:
				return Gdx.files.external(".local/share/Quest Giver/" + SteamConnection.getSteamID() + "/" + saveFileName);
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
