package com.darzalgames.libgdxtools.preferences;

import com.badlogic.gdx.Gdx;

public class PreferenceManagerFactory {

	private PreferenceManagerFactory() {}

	public static PreferenceManager create(String packageName) {
		try {
			return new PreferenceManager(packageName);
		} catch (Exception e) {
			try {
				Gdx.app.log("PreferenceManagerFactory", "Error accessing .prefs file, using in-memory preferences. (" + packageName + ")");
			} catch (Exception loggingException) {
				// it's ok if we can't log, e.g. in a test
			}
			return new InMemoryPreferenceManager();
		}
	}

}
