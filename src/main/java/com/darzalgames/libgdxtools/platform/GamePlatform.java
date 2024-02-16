package com.darzalgames.libgdxtools.platform;

import java.util.Arrays;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public enum GamePlatform { 
	WINDOWS,
	LINUX,
	MAC,
	WEB
	;
	
	/**
	 * @return True if the active game platform supports borderless fullsreen
	 */
	public boolean supportsBorderlessFullscreen() {
		if(this == LINUX) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Return the correct directory for save files based on the platform
	 * @param fullGameAndSaveName The subpath and name of the desired save file
	 * @return A libgdx file handle to the save file
	 */
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		switch (this) {
		case WINDOWS:
			return Gdx.files.absolute(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/My Games/"+ fullGameAndSaveName);
		case LINUX:
			return Gdx.files.external(".local/share/" + fullGameAndSaveName);
		default:
			return null;
		}
	}

	/**
	 * Determines the correct platform based on launch arguments
	 * @param args java main arguments
	 * @return The corresponding platform
	 */
	public static GamePlatform getTypeFromArgs(String[] args) {
		List<String> argsList = Arrays.asList(args);
		
		if(argsList.contains("windows")) {
			return GamePlatform.WINDOWS;
		}
		else if(argsList.contains("linux")) {
			return GamePlatform.LINUX;
		}
		else if(argsList.contains("mac")) {
			return GamePlatform.MAC;
		}
		else {
			return null;
		}
	}
	
}