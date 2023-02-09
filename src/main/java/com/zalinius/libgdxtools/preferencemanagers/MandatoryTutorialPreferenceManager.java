package com.zalinius.libgdxtools.preferencemanagers;

import com.badlogic.gdx.Preferences;

public class MandatoryTutorialPreferenceManager extends PreferenceManager {
	private static final String TUTORIAL_KEY = "hasDoneTutorial";

	public MandatoryTutorialPreferenceManager(final Preferences preferencesFile) {
		super(preferencesFile);
	}

	public void markTutorialDone(final boolean isDone) {
		savePrefValue(TUTORIAL_KEY, isDone);
	}

	public boolean hasDoneTutorial() {
		return getBooleanPrefValue(TUTORIAL_KEY, false);
	}

}
