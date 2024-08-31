package com.darzalgames.libgdxtools.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.save.DesktopSaveManager;
import com.darzalgames.libgdxtools.save.SaveManager;

public class LinuxGamePlatform extends GenericDesktopGamePlatform {

	@Override
	public boolean supportsBorderlessFullscreen() {
		return false;
	}

	@Override
	public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
		return Gdx.files.external(".local/share/" + fullGameAndSaveName);
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
