package com.darzalgames.libgdxtools.errorhandling;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CrashLocalizationTest {

	@Test
	void getLocaleLanguage_supportedLocale_returnsCorrectLanguage() {
		assertEquals(CrashLocalization.ENGLISH_CRASH_LOCALIZATION, CrashLocalization.getLocalizationFromCode("en"));
		assertEquals(CrashLocalization.ENGLISH_CRASH_LOCALIZATION, CrashLocalization.getLocalizationFromCode("eng"));

		assertEquals(CrashLocalization.FRENCH_CRASH_LOCALIZATION, CrashLocalization.getLocalizationFromCode("fr"));
		assertEquals(CrashLocalization.FRENCH_CRASH_LOCALIZATION, CrashLocalization.getLocalizationFromCode("fra"));
		assertEquals(CrashLocalization.FRENCH_CRASH_LOCALIZATION, CrashLocalization.getLocalizationFromCode("fre"));
	}

	@Test
	void getLocaleLanguage_missingLocale_returnsEnglish() {
		assertEquals(CrashLocalization.ENGLISH_CRASH_LOCALIZATION, CrashLocalization.getLocalizationFromCode(""));
		assertEquals(CrashLocalization.ENGLISH_CRASH_LOCALIZATION, CrashLocalization.getLocalizationFromCode("ja"));
		assertEquals(CrashLocalization.ENGLISH_CRASH_LOCALIZATION, CrashLocalization.getLocalizationFromCode("jpn"));
	}

}
