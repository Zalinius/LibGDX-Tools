package com.darzalgames.libgdxtools.assetloading;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;

class FileNameCaseValidatorTest {

	@Test
	void accept_exactCaseMatchBetweenFileNameAndAssetDescriptor_doesNotThrow() {
		Gdx.files = new Lwjgl3Files();
		AssetDescriptor<Texture> textureDescriptor = new AssetDescriptor<>("src/test/resources/com/darzalgames/libgdxtools/gfx/test.png", Texture.class);
		FileNameCaseValidator fileNameCaseValidator = new FileNameCaseValidator();

		assertDoesNotThrow(() -> fileNameCaseValidator.accept(textureDescriptor));
	}

	@Test
	void accept_mismatchedCaseNames_throwsIllegalArgumentException() {
		// This test is especially for windows, hitting the "file name comparison" failure branch
		Gdx.files = new Lwjgl3Files();
		AssetDescriptor<Texture> textureDescriptor = new AssetDescriptor<>("src/test/resources/com/darzalgames/libgdxtools/gfx/teSt.png", Texture.class);
		FileNameCaseValidator fileNameCaseValidator = new FileNameCaseValidator();

		assertThrows(IllegalArgumentException.class, () -> fileNameCaseValidator.accept(textureDescriptor));
	}

	@Test
	void accept_nonexistantFile_throwsIllegalArgumentException() {
		// This test is especially for non-windows operating systems, hitting the "non-existant file" failure branch
		Gdx.files = new Lwjgl3Files();
		AssetDescriptor<Model> textureDescriptor = new AssetDescriptor<>("src/test/resources/com/darzalgames/libgdxtools/doesNotExist/solarPanel.generated", Model.class);
		FileNameCaseValidator fileNameCaseValidator = new FileNameCaseValidator();

		assertThrows(IllegalArgumentException.class, () -> fileNameCaseValidator.accept(textureDescriptor));
	}

	@Test
	void accept_nonexistantFile_withIgnoredExtension_doesNotThrow() {
		Gdx.files = new Lwjgl3Files();
		AssetDescriptor<Model> textureDescriptor = new AssetDescriptor<>("src/test/resources/com/darzalgames/libgdxtools/doesNotExist/solarPanel.generated", Model.class);
		FileNameCaseValidator fileNameCaseValidator = new FileNameCaseValidator("generated");

		assertDoesNotThrow(() -> fileNameCaseValidator.accept(textureDescriptor));
	}

	@Test
	void accept_mismatchedCaseNamesWithIrrelevantIgnoredExtensions_throwsIllegalArgumentException() {
		Gdx.files = new Lwjgl3Files();
		AssetDescriptor<Texture> textureDescriptor = new AssetDescriptor<>("src/test/resources/com/darzalgames/libgdxtools/gfx/teSt.png", Texture.class);
		FileNameCaseValidator fileNameCaseValidator = new FileNameCaseValidator("jpg");

		assertThrows(IllegalArgumentException.class, () -> fileNameCaseValidator.accept(textureDescriptor));
	}

}
