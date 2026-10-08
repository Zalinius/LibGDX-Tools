package com.darzalgames.libgdxtools.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.Color;

class TemporaryStylerTest {

	@Test
	void colorInAllWordVariants_withVariantsAsSubstrings_correctlyColorsLongestVariantsFirstAndDoesNotDoubleColorInnerVariantSubstring() {
		String testString = "Whenever spawns a spawning symbol spawn then spawned.";
		String expectedOutput = "Whenever [(label)][#daa520ff]spawns[ label] a [(label)][#daa520ff]spawning[ label] symbol [(label)][#daa520ff]spawn[ label] then [(label)][#daa520ff]spawned[ label].";

		Set<String> variants = Set.of("spawn", "spawning", "spa", "spawned", "spawns");

		String result = TemporaryStyler.colorInAllWordVariants(Color.GOLDENROD, testString, variants);

		assertEquals(expectedOutput, result);
	}

}
