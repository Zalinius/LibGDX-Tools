package com.darzalgames.libgdxtools.save;

import java.util.Locale;

import javax.swing.filechooser.FileSystemView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import com.darzalgames.libgdxtools.i18n.TextSupplier;
import com.darzalgames.libgdxtools.steam.SteamConnection;

abstract class SaveManager {

	private String language;
	private boolean textShouldBeInstant;

	public static SaveManager instance;
	
	protected abstract void saveInternal();
	protected abstract void loadInternal(SaveManager loadedSave);
	protected abstract void respondToFailedLoad();

	protected SaveManager() {	}

	public static void initialize(SaveManager instance) {
		SaveManager.instance = instance;
	}

	public static void save() {
		Json json = new Json();
		json.setUsePrototypes(false);

		// write old data to a backup
		FileHandle backup = getBackupSaveFile();
		backup.writeString(json.toJson(instance), false);

		instance.saveInternal();
		
		instance.language = TextSupplier.getLocale().getLanguage();

		FileHandle file = getSaveFile();
		file.writeString(json.toJson(instance), false);
	}

	public static boolean load() {
		FileHandle file = getSaveFile();
		if (file.exists()) {
			Json json = new Json();

			// good since I'll be adding/removing various values in the future, a deleted
			// value will be ignored and the overwritten by the next save
			json.setIgnoreUnknownFields(true);

			try {
				SaveManager loadedSave = json.fromJson(SaveManager.class, file);
				instance.loadInternal(loadedSave);
				return true;

			} catch (SerializationException|NullPointerException e) {
				json.setUsePrototypes(false);
				FileHandle corruptedfile = getBackupSaveFile();
				corruptedfile.writeString(file.readString(), false);
				Gdx.app.log("SaveManager", "Failed to load save (" + e.getClass() + "), creating new save instead");
				e.printStackTrace();
				instance.respondToFailedLoad();
				return false;
			} 
		} else {
			instance.respondToFailedLoad();
			return false;
		}
	}


	private static FileHandle getSaveFile() {
		return getOperatingSystemType().getSave();
	}

	private static FileHandle getBackupSaveFile() {
		return getOperatingSystemType().getBackupSave();
	}

	public enum OSType { 
		Windows,
		Linux,
		;

		private transient final static String saveName = "QuestGiverSave.json";
		private transient final String backupSaveName = "QuestGiverSave-backup.json";

		public FileHandle getBackupSave() {
			return getSave(backupSaveName);
		}

		public FileHandle getSave() {
			return getSave(saveName);
		}

		private FileHandle getSave(String name) {
			switch (this) {
			case Windows:
				return Gdx.files.absolute(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/My Games/Quest Giver/" + SteamConnection.getSteamID() + "/" + name);
			case Linux:
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
	 * @returns - the operating system detected
	 */
	public static OSType getOperatingSystemType() {
		OSType detectedOS = OSType.Windows;
		String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
			// detectedOS = OSType.MacOS;
		} else if (OS.indexOf("win") >= 0) {
			detectedOS = OSType.Windows;
		} else if (OS.indexOf("nux") >= 0) {
			detectedOS = OSType.Linux;
		}
		return detectedOS;
	}

	public static boolean doInstantText() {
		return instance.textShouldBeInstant;
	}

	public static void setTextSpeed(boolean shouldBeInstant) {
		instance.textShouldBeInstant = shouldBeInstant;
		save();
	}

}
