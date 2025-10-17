package com.darzalgames.libgdxtools.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.utils.Align;

class AlignmentTest {

	@Test
	void getAlignment_onAll_ReturnsTheCorrectInteger()  {
		assertEquals(Align.left, Alignment.LEFT.getAlignment());
		assertEquals(Align.right, Alignment.RIGHT.getAlignment());
		assertEquals(Align.top, Alignment.TOP.getAlignment());
		assertEquals(Align.topLeft, Alignment.TOP_LEFT.getAlignment());
		assertEquals(Align.topRight, Alignment.TOP_RIGHT.getAlignment());
		assertEquals(Align.bottom, Alignment.BOTTOM.getAlignment());
		assertEquals(Align.bottomLeft, Alignment.BOTTOM_LEFT.getAlignment());
		assertEquals(Align.bottomRight, Alignment.BOTTOM_RIGHT.getAlignment());
	}
}
