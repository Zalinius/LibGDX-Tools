package com.darzalgames.libgdxtools.platform;

/**
 * Enum for Demo and Full versions of a game
 */
public enum GameEdition {
	DEMO("Demo"),
	FULL("Full");

	private final String displayName;

	GameEdition(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return A string suffix to describe a version of a game, based on GameEdition
	 */
	public String getDisplayName() {
		return displayName;
	}
}
