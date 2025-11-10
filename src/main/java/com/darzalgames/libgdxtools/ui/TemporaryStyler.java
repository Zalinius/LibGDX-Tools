package com.darzalgames.libgdxtools.ui;

import com.badlogic.gdx.graphics.Color;

public class TemporaryStyler {

	public static String make(String string) {
		return "[(label)]" + string + "[ label]";
	}

	public static String makeColored(Color color, String string) {
		return make("[#" + color.toString() + "]" + string);
	}

	private TemporaryStyler() {}

}
