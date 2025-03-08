package com.darzalgames.libgdxtools.errorhandling;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CrashReportLanguageTest {
	
	@Test
	void getLocalization_returnsCorrectLocalization() throws Exception {
		assertEquals(CrashLocalization.ENGLISH_CRASH_LOCALIZATION, CrashReportLanguage.ENGLISH.getLocalization());
		assertEquals(CrashLocalization.FRENCH_CRASH_LOCALIZATION, CrashReportLanguage.FRENCH.getLocalization());
	}
	
	@Test
	void getLocaleLanguage_supportedLanguage_returnsCorrectLanguage() throws Exception {
		assertEquals(CrashReportLanguage.ENGLISH, CrashReportLanguage.getLanguageFromCode("en"));
		assertEquals(CrashReportLanguage.ENGLISH, CrashReportLanguage.getLanguageFromCode("eng"));
		
		assertEquals(CrashReportLanguage.FRENCH, CrashReportLanguage.getLanguageFromCode("fr"));
		assertEquals(CrashReportLanguage.FRENCH, CrashReportLanguage.getLanguageFromCode("fra"));
		assertEquals(CrashReportLanguage.FRENCH, CrashReportLanguage.getLanguageFromCode("fre"));
	}

	@Test
	void getLocaleLanguage_missingLanguage_returnsEnglish() throws Exception {
		assertEquals(CrashReportLanguage.ENGLISH, CrashReportLanguage.getLanguageFromCode(""));
		assertEquals(CrashReportLanguage.ENGLISH, CrashReportLanguage.getLanguageFromCode("ja"));
		assertEquals(CrashReportLanguage.ENGLISH, CrashReportLanguage.getLanguageFromCode("jpn"));
	}

}
