package com.darzalgames.libgdxtools.platform;

import javax.swing.filechooser.FileSystemView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class WindowsGamePlatform extends GenericDesktopGamePlatform {

	@Override
	public boolean supportsBorderlessFullscreen() {
		return true;
	}

	@Override
	public FileHandle getOldSaveFileLocation(String fullGameAndSaveName) {
		return Gdx.files.absolute(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/My Games/"+ fullGameAndSaveName);
	}

	@Override
	public FileHandle getNewSaveFileLocation(String fullGameAndSaveName) {
		String appDataLocalPath = System.getenv("LOCALAPPDATA");
		return Gdx.files.absolute(appDataLocalPath + "/" + fullGameAndSaveName);
	}
	
	@Override
	public String getPlatformName() {
		return GamePlatform.WINDOWS;
	}
}
