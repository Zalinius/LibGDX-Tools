package com.darzalgames.libgdxtools.maingame;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.darzalgames.libgdxtools.platform.GamePlatform;
import com.darzalgames.libgdxtools.ui.input.Input;

class InitializationIT {

	@Test
	void launchGame_doesntThrowAnyExceptions() {
		assertDoesNotThrow(
				() -> TestGame.testLauncher(new String[] {GamePlatform.WINDOWS}, 
						app -> {
							Gdx.app.exit();
						})
				);
	}

	@Test
	void launchGame_pressingEscape_pausesTheGame() {
		TestGame.testLauncher(new String[] {GamePlatform.WINDOWS}, 
				app -> {
					app.inputReceiver.processKeyInput(Input.getInputFromKey(Keys.ESCAPE));
					
					assertTrue(app.pause.isPaused());
					
					Gdx.app.exit();
				});
	}

}
