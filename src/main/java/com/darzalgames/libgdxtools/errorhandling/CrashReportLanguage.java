package com.darzalgames.libgdxtools.errorhandling;

import java.util.Set;

public enum CrashReportLanguage {
	ENGLISH(CrashLocalization.ENGLISH_CRASH_LOCALIZATION),
	FRENCH(CrashLocalization.FRENCH_CRASH_LOCALIZATION);
	
	private final CrashLocalization localization;
	
	private CrashReportLanguage(CrashLocalization localization) {
		this.localization = localization;
	}

	public CrashLocalization getLocalization() {return localization;}
	
	
	public static CrashReportLanguage getLanguageFromCode(String languageCode) {
		Set<String> frenchLanguageCodes = Set.of("fr", "fra", "fre");
		
		CrashReportLanguage reportLanguage;
		if(frenchLanguageCodes.contains(languageCode)) {
			reportLanguage = CrashReportLanguage.FRENCH;
		}
		else {
			reportLanguage = CrashReportLanguage.ENGLISH;
		}
		
		return reportLanguage;
	}
}
