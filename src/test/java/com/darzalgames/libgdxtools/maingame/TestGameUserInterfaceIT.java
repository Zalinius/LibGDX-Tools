package com.darzalgames.libgdxtools.maingame;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.darzalgames.libgdxtools.platform.GamePlatform;

class TestGameUserInterfaceIT {
	
	private static final int SCREEN_MIDDLE_X = 640;
	
	private static final int SELECT_BOX_BUTTON_Y = 350;
	private static final int POPUP_BUTTON_X = 990;
	private static final int POPUP_BUTTON_Y = 240;
	private static final int QUIT_BUTTON_Y = 700;

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
	void pressingEscapeTwice_pausesThenUnpausesTheGame() {
		TestGame.testLauncher(new String[] {GamePlatform.WINDOWS}, 
				app -> {
					pressKey(Keys.ESCAPE);
					assertTrue(app.pause.isPaused());
					pressKey(Keys.ESCAPE);
					assertFalse(app.pause.isPaused());

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
	
	@Test
	void openingAPopup_putsTheMenuOutOfFocus() {
		TestGame.testLauncher(new String[] {GamePlatform.WINDOWS}, 
				app -> {
					clickMouse(SCREEN_MIDDLE_X, SELECT_BOX_BUTTON_Y); // Opens the popup away from the mouse
					app.popUpStage.act(3); // Give time for the popup to slide in
					
					// Check that the menu is NOT back in focus by hovering over the quit button
					app.stage.mouseMoved(SCREEN_MIDDLE_X, QUIT_BUTTON_Y);
					app.stage.act(1/60f);
					assertFalse(app.quitButton.getView().isOver());

					Gdx.app.exit();
				});
	}
	
	@Test
	void openingAPopup_thenClickingTheDarkScreen_closesThePopup() {
		TestGame.testLauncher(new String[] {GamePlatform.WINDOWS}, 
				app -> {
					clickMouse(SCREEN_MIDDLE_X, SELECT_BOX_BUTTON_Y); // Opens the popup away from the mouse
					app.stage.act(3); // Give time for the popup to slide in
					clickMouse(SCREEN_MIDDLE_X, SELECT_BOX_BUTTON_Y); // Clicks on the dark screen to dismiss the popup

					// Check that the menu is back in focus by hovering over the quit button
					app.stage.mouseMoved(SCREEN_MIDDLE_X, QUIT_BUTTON_Y);
					app.stage.act(1/60f);
					assertTrue(app.quitButton.getView().isOver());

					Gdx.app.exit();
				});
	}
	
	@Test
	void openingAPopup_thenRightClickingIt_closesThePopup() {
		TestGame.testLauncher(new String[] {GamePlatform.WINDOWS}, 
				app -> {
					clickMouse(SCREEN_MIDDLE_X, SELECT_BOX_BUTTON_Y); // Opens the popup away from the mouse
					app.popUpStage.act(3); // Give time for the popup to slide in
					rightClickMouse(POPUP_BUTTON_X, POPUP_BUTTON_Y); // Right click on the popup to dismiss it
					
					// Check that the menu is back in focus by hovering over the quit button
					app.stage.mouseMoved(SCREEN_MIDDLE_X, QUIT_BUTTON_Y);
					app.stage.act(1/60f);
					assertTrue(app.quitButton.getView().isOver());

					Gdx.app.exit();
				});
	}

	private void pressKey(int key) {
		Gdx.input.getInputProcessor().keyDown(key);
		Gdx.input.getInputProcessor().keyUp(key);
	}

	private void clickMouse(int x, int y) {
		Gdx.input.getInputProcessor().touchDown(x, y, 0, Buttons.LEFT);
		Gdx.input.getInputProcessor().touchUp(x, y, 0, Buttons.LEFT);
	}

	private void rightClickMouse(int x, int y) {
		Gdx.input.getInputProcessor().touchDown(x, y, 0, Buttons.RIGHT);
		Gdx.input.getInputProcessor().touchUp(x, y, 0, Buttons.RIGHT);
	}

}
