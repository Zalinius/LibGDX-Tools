package com.darzalgames.libgdxtools.maingame;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.darzalgames.libgdxtools.platform.GamePlatform;

class InitializationIT {
	
	private static final int SCREEN_MIDDLE_X = 640;
	private static final int QUIT_BUTTON_Y = 550;

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
	void pressingEscape_pausesTheGame() {
		TestGame.testLauncher(new String[] {GamePlatform.WINDOWS}, 
				app -> {
					pressKey(Keys.ESCAPE);

					assertTrue(app.pause.isPaused());

					Gdx.app.exit();
				});
	}

	@Test
	void clickingTheOptionsButton_pausesTheGame() {
		TestGame.testLauncher(new String[] {GamePlatform.WINDOWS}, 
				app -> {
					clickMouse(10, 10);

					assertTrue(app.pause.isPaused());

					Gdx.app.exit();
				});
	}

	@Test
	void clickingTheQuitButton_quitsTheGame() {
		TestGame.testLauncher(new String[] {GamePlatform.WINDOWS}, 
				app -> {
					clickMouse(SCREEN_MIDDLE_X, QUIT_BUTTON_Y);
				});
		// We made it out of the game
		assertTrue(true);
	}

	@Test
	void hoveringTheQuitButtonWithTheMouse_putsItInFocus() {
		TestGame.testLauncher(new String[] {GamePlatform.WINDOWS}, 
				app -> {
					app.stage.mouseMoved(SCREEN_MIDDLE_X, QUIT_BUTTON_Y);
					app.stage.act(1/60f);

					assertTrue(app.quitButton.getView().isOver());

					Gdx.app.exit();
				});
	}

	@Test
	void navigatingByKeyboard_andPressingTheQuitButton_wrapsAroundTheMenuAndQuitsTheGame() {
		TestGame.testLauncher(new String[] {GamePlatform.WINDOWS}, 
				app -> {
					pressKey(Keys.DOWN); // Switch to keyboard mode
					pressKey(Keys.UP); // Wrap around to the bottom of the menu
					pressKey(Keys.ENTER);
				});
		// We made it out of the game
		assertTrue(true);
	}

	private void pressKey(int key) {
		Gdx.input.getInputProcessor().keyDown(key);
		Gdx.input.getInputProcessor().keyUp(key);
	}

	private void clickMouse(int x, int y) {
		Gdx.input.getInputProcessor().touchDown(x, y, 0, Buttons.LEFT);
		Gdx.input.getInputProcessor().touchUp(x, y, 0, Buttons.LEFT);
	}

}
