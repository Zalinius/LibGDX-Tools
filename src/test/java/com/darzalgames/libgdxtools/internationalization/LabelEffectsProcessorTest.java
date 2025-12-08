package com.darzalgames.libgdxtools.internationalization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LabelEffectsProcessorTest {

	@ParameterizedTest
	@MethodSource("variousSampleStrings")
	void myRegularExpression_capturesOnlySquareBraceEffects(String input, String expectedOutput) {
		String output = LabelEffectsProcessor.process(input);

		assertEquals(expectedOutput, output);
	}

	private static Stream<Arguments> variousSampleStrings() {
		return Stream.of(
				Arguments.of("test hi oooo", "test hi oooo"), // no tag is unchanged
				Arguments.of("test hi [BLACK]oooo[]", "test hi [BLACK]oooo[]"), // only non-textra tag is unchanged
				Arguments.of("test hi [-FIRE]oooo[-ENDFIRE]", "test hi {FIRE}oooo{ENDFIRE}"), // single simple tag
				Arguments.of("test hi [-VAR=SPOOKY]oooo[-VAR=ENDSPOOKY] wow", "test hi {VAR=SPOOKY}oooo{VAR=ENDSPOOKY} wow"), // single var tag
				Arguments.of("test hi [-VAR=SPOOKY]oooo[-VAR=ENDSPOOKY] wow [-FIRE]lit[-ENDFIRE]", "test hi {VAR=SPOOKY}oooo{VAR=ENDSPOOKY} wow {FIRE}lit{ENDFIRE}"), // both kinds of tags
				Arguments.of("[POWER_COLOR]P[-VAR=SPOOKY]o[-VAR=ENDSPOOKY]wer[WHITE][+powerIcon]", "[POWER_COLOR]P{VAR=SPOOKY}o{VAR=ENDSPOOKY}wer[WHITE][+powerIcon]") // mixed with non-textra tags and icons
		);
	}

}
