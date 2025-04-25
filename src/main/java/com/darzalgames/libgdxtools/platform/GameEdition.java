package com.darzalgames.libgdxtools.platform;

/**
 * Enum for Demo and Full versions of a game
 */
public enum GameEdition {
	DEMO("-demo"),
	FULL("");

	private final String versionSuffix;

	GameEdition(String versionSuffix) {
		this.versionSuffix = versionSuffix;
	}

	/**
	 * @return A string suffix to describe a version of a game, based on GameEdition
	 */
	public String getVersionSuffix() {
		return versionSuffix;
	}
}
