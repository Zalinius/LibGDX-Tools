package com.darzalgames.libgdxtools.save;

public interface SaveManager {
	void save();

	/**
	 * @return true if loading went fine, false if the save failed to load and/or a new save had to be created
	 */
	boolean load();
}
