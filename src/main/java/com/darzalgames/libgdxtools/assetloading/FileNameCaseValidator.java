package com.darzalgames.libgdxtools.assetloading;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;

public class FileNameCaseValidator implements Consumer<AssetDescriptor<?>> {

	@Override
	public void accept(AssetDescriptor<?> assetDescriptor) {
		// Check that the path listed in this file exactly matches the real path for each asset since file handling in Windows is not case sensitive, but both Linux and Steam are.
		// Linux is using ext4 (which is case sensitive) vs Windows using NTFS (which is not case sensitive) https://stackoverflow.com/questions/34603505/java-file-exists-case-sensitive-jpg-and-jpg
		try {
			String specifiedPath = System.getProperty("user.dir") + File.separator + assetDescriptor.fileName;
			specifiedPath = specifiedPath.replace('/', '\\');
			String realPath = Gdx.files.internal(assetDescriptor.fileName).file().getCanonicalPath().replace("desktop", "assets");
			if (!realPath.equals(specifiedPath)) {
				throw new IllegalArgumentException("\n\nFile not found, possibly a mismatch in upper/lower case?\n" + realPath + "\n     VERSUS\n" + specifiedPath + "\nLook closely!\n\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
