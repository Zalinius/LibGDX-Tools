package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;

public class TestWithTable {

	public static void setUpBeforeAll() {
		// Remarkably, this is needed to avoid a stack overflow when creating a LibGDX Table, which my menus do.
		Gdx.files = new Lwjgl3Files();
	}

}
