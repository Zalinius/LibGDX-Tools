package com.darzalgames.libgdxtools.graphics.resolution;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ResolutionPresetTest {

	@ParameterizedTest
	@EnumSource(ResolutionPreset.class)
	void name_forEachPreset_matchesTheStoredWidthAndHeight(ResolutionPreset resolutionPreset) {
		String expectedName = "X_" + resolutionPreset.width + "X" + resolutionPreset.height;
		assertEquals(expectedName, resolutionPreset.name());
	}

}
