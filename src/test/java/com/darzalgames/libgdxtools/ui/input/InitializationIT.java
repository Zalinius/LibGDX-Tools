package com.darzalgames.libgdxtools.ui.input;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.darzalgames.libgdxtools.maingame.TestGame;
import com.darzalgames.libgdxtools.platform.GamePlatform;

class InitializationIT {
	
	Lwjgl3Application app;

	@BeforeEach
	void launchGame() {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		int width = 1280;
		int height = 720;
		config.setWindowedMode(width, height);
		config.setTitle("TEST UI");
		
		
		app = new Lwjgl3Application(new TestGame(width, height, new String[] {GamePlatform.WINDOWS}), config);
	}
	
	@Test
	void launchGame_doesntThrow_AnyExceptions() {
		assertDoesNotThrow(() -> {});
	}
	
	@AfterEach
	void quitGame() {
		app.exit();
	}
}
