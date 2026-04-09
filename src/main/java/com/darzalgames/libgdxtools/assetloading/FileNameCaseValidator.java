package com.darzalgames.libgdxtools.assetloading;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;

public class FileNameCaseValidator implements Consumer<AssetDescriptor<?>> {

	private final Set<String> fileExtensionsToIgnore;

	/**
	 * Create a validator which processes any and all file extensions to check for identical name case
	 */
	public FileNameCaseValidator() {
		this(Set.of());
	}

	/**
	 * Creates a validator which ignores the specified file extensions, e.g. for generated files which only exist in code and
	 * as such aren't able to have name case conflicts
	 * @param fileExtensionsToIgnore all file extensions that should be ignored in validation, WITHOUT a leading "."
	 */
	public FileNameCaseValidator(String... fileExtensionsToIgnore) {
		this(new HashSet<>(Arrays.asList(fileExtensionsToIgnore)));
	}

	/**
	 * Creates a validator which ignores the specified file extensions, e.g. for generated files which only exist in code and
	 * as such aren't able to have name case conflicts
	 * @param fileExtensionsToIgnore all file extensions that should be ignored in validation, WITHOUT a leading "."
	 */
	public FileNameCaseValidator(Set<String> fileExtensionsToIgnore) {
		this.fileExtensionsToIgnore = fileExtensionsToIgnore;
	}

	@Override
	public void accept(AssetDescriptor<?> assetDescriptor) {
		String extension = Gdx.files.internal(assetDescriptor.fileName).extension();
		if (!fileExtensionsToIgnore.contains(extension)) {
			// Check that the path listed in this file exactly matches the real path for each asset since file handling in Windows is not case sensitive, but both Linux and Steam are.
			// Linux is using ext4 (which is case sensitive) vs Windows using NTFS (which is not case sensitive) https://stackoverflow.com/questions/34603505/java-file-exists-case-sensitive-jpg-and-jpg
			try {
				String specifiedPath = System.getProperty("user.dir") + File.separator + assetDescriptor.fileName;
				specifiedPath = specifiedPath.replace("/", File.separator);
				String realPath = Gdx.files.internal(assetDescriptor.fileName).file().getCanonicalPath().replace("desktop", "assets");
				if (!Gdx.files.internal(assetDescriptor.fileName).file().exists() || !realPath.equals(specifiedPath)) {
					throw new IllegalArgumentException("\n\nFile not found, possibly a mismatch in upper/lower case?\n" + realPath + "\n     VERSUS\n" + specifiedPath + "\nLook closely!\n\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
