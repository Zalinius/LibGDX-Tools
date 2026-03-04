package com.darzalgames.libgdxtools.edition;

/**
 * Enum for different editions of games, which differ by content and intent
 */
public enum GameEdition {
	/**
	 * An edition of a game with reduced content and features, available publicly and for free
	 */
	DEMO("Demo"),

	/**
	 * A complete edition of a game, available publicly and for purchase
	 */
	FULL("Full"),

	/**
	 * A concentrated edition of a game designed to give a thorough experience very quickly, available privately for play-testing
	 */
	TURBO("Turbo");

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
