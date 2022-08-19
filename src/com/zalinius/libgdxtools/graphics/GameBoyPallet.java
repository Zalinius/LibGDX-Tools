package com.zalinius.libgdxtools.graphics;

import com.badlogic.gdx.graphics.Color;

public class GameBoyPallet {

	public static final Color DARKEST_GREEN = ColorTools.makeColorFromIntComponents(15, 56, 15);
	public static final Color DARK_GREEN = ColorTools.makeColorFromIntComponents(48, 98, 48);
	public static final Color LIGHT_GREEN = ColorTools.makeColorFromIntComponents(139, 172, 15);
	public static final Color LIGHTEST_GREEN = ColorTools.makeColorFromIntComponents(155, 188, 15);

	protected GameBoyPallet() {
		throw new IllegalStateException("Utility class");
	}
}
