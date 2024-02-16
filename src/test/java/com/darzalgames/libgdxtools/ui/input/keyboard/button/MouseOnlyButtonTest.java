package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;
import com.darzalgames.libgdxtools.ui.input.strategy.KeyboardInputStrategy;
import com.darzalgames.libgdxtools.ui.input.strategy.MouseInputStrategy;

class MouseOnlyButtonTest {
	
	// TODO Should I bother making similar tests for other Keyboard UI? Not sure how many times 
	// we need to test that the InputStrategyManager's observer system works...
	
	private static KeyboardButton button;
	private static InputStrategyManager inputStrategyManager;

	@Test
	void isVisible_duringMouseStrategy_returnsTrue() throws Exception {

		inputStrategyManager.setToMouseStrategy();

		assertTrue(button.getView().isVisible());
	}

	@Test
	void isVisible_duringKeyboardStrategy_returnsFalse() throws Exception {

		inputStrategyManager.setToKeyboardStrategy();

		assertFalse(button.getView().isVisible());
	}

	@BeforeAll
	public static void setUp() throws Exception {
		Gdx.gl20 = Gdx.gl30 = Gdx.gl31 = Gdx.gl32 = Mockito.mock(GL32.class);
		Gdx.gl = Gdx.gl20;
		Gdx.graphics = new MockGraphics();
		Gdx.files = Mockito.mock(Files.class);
		
		inputStrategyManager = new InputStrategyManager(new MouseInputStrategy(), new KeyboardInputStrategy());
		TextButtonStyle textButtonStyle =  new TextButtonStyle();
		textButtonStyle.font = new BitmapFont(new BitmapFontData(), new TextureRegion(), false);
		TextButton textButton = new TextButton("", textButtonStyle);
		button = new MouseOnlyButton(textButton, Runnables.nullRunnable(), inputStrategyManager)  {
			@Override
			public boolean shouldBeUnregistered() {
				return false;
			}	
		};
	}
}
