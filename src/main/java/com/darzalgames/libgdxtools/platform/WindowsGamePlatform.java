package com.darzalgames.libgdxtools.platform;

import javax.swing.filechooser.FileSystemView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.save.DesktopSaveManager;
import com.darzalgames.libgdxtools.save.SaveManager;

public class WindowsGamePlatform extends GenericDesktopGamePlatform {

	@Override
	public boolean supportsBorderlessFullscreen() {
		return true;
	}

	@Override
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		return Gdx.files.absolute(FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/My Games/"+ fullGameAndSaveName);
	}
	
	@Override
	public SaveManager makeSaveManager() {
		return new DesktopSaveManager() {
			
			@Override
			public void save() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean load() {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}
}
