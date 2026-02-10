package com.darzalgames.libgdxtools.save;

public interface SaveManager {

	void save();

	/**
	 * Loads the Computer Specific Options required for initizalization
	 * @return true if loading went fine, false if the save failed to load and/or a new save had to be created
	 */
	boolean loadOptions();

	/**
	 * @return true if loading went fine, false if the save failed to load and/or a new save had to be created
	 */
	boolean loadGame();

}
