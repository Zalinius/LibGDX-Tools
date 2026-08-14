package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;

public class TestWithTable {

	/**
	 * A @BeforeAll setup which is necessary when testing code that initializes a LibGDX Table
	 */
	public static void setUpBeforeAll() {
		// Remarkably, this is needed to avoid a stack overflow when creating a LibGDX Table, which my menus do.
		Gdx.files = new Lwjgl3Files();

		// Creating a ScrollPane requires Gdx.app to be non-null deep *deep* in its initialization
		Gdx.app = new HeadlessApplication(new ApplicationAdapter() {
		});
	}

}
