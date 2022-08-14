package com.zalinius.libgdxtools.preferencemanagers;

public class MandatoryTutorialPreferenceManager extends PreferenceManager {
	private static final String TUTORIAL_KEY = "hasDoneTutorial";


	public static void markTutorialDone(final boolean isDone) {
		savePrefValue(TUTORIAL_KEY, isDone);
	}

	public static boolean hasDoneTutorial() {
		return getBooleanPrefValue(TUTORIAL_KEY, false);
	}

}
