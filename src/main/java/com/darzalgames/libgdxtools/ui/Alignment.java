package com.darzalgames.libgdxtools.ui;

import com.badlogic.gdx.utils.Align;

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

	public int getAlignment() {
		return alignmentInteger;
	}
}
