package com.zalinius.libgdxtools.preferencemanagers;

public class MandatoryTutorialPreference {
	
	private static final String TUTORIAL_KEY = "hasDoneTutorial";
	
	private final PreferenceManager preferenceManager;
	
	protected MandatoryTutorialPreference(PreferenceManager preferenceManager) {
		this.preferenceManager = preferenceManager;
	}

	public void markTutorialDone(final boolean isDone) {
		preferenceManager.savePrefValue(TUTORIAL_KEY, isDone);
	}

	public boolean hasDoneTutorial() {
		return preferenceManager.getBooleanPrefValue(TUTORIAL_KEY, false);
	}

}
