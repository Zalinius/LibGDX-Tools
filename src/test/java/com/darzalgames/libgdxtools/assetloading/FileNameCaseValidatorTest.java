package com.darzalgames.libgdxtools.assetloading;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.graphics.Texture;

class FileNameCaseValidatorTest {

	@Test
	void testExactCaseMatchBetweenFileNameAndAssetDescriptorDoesNotThrow() {
		Gdx.files = new Lwjgl3Files();
		AssetDescriptor<Texture> textureDescriptor = new AssetDescriptor<>("src/test/resources/com/darzalgames/libgdxtools/gfx/test.png", Texture.class);
		FileNameCaseValidator fileNameCaseValidator = new FileNameCaseValidator();

		assertDoesNotThrow(() -> fileNameCaseValidator.accept(textureDescriptor));
	}

	@Test
	void testMismatchedCaseNamesThrowsIllegalArgumentException() {
		Gdx.files = new Lwjgl3Files();
		AssetDescriptor<Texture> textureDescriptor = new AssetDescriptor<>("src/test/resources/com/darzalgames/libgdxtools/gfx/teSt.png", Texture.class);
		FileNameCaseValidator fileNameCaseValidator = new FileNameCaseValidator();

		assertThrows(IllegalArgumentException.class, () -> fileNameCaseValidator.accept(textureDescriptor));
	}
}
