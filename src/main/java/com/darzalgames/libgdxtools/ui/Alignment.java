package com.darzalgames.libgdxtools.ui;

import com.badlogic.gdx.utils.Align;

/**
 * A new alignment class that's easier to say, and which makes for better method arguments than the
 * LibGDX {@link Align} integers / bit flags (since any old integer could take their place as an argument)
 */
public enum Alignment {

	TOP(Align.top),
	TOP_RIGHT(Align.topRight),
	RIGHT(Align.right),
	BOTTOM_RIGHT(Align.bottomRight),
	BOTTOM(Align.bottom),
	BOTTOM_LEFT(Align.bottomLeft),
	LEFT(Align.left),
	TOP_LEFT(Align.topLeft),
	CENTER(Align.center);
	
	private final int alignmentInteger;

	private Alignment(int alignment) {
		this.alignmentInteger = alignment;
	}

	/**
	 * @return The LibGDX {@link Align} integer, to be used to directly when aligning an actor (Table, Cell, etc.)
	 */
	public int getAlignment() {
		return alignmentInteger;
	}
}
