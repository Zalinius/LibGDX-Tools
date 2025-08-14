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
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.darzalcommon.functional.Suppliers;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.MouseOnlyButton;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.MyTextButton;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;

class MouseOnlyButtonTest {

	private static UniversalButton button;
	private static InputStrategySwitcher inputStrategySwitcher;

	@Test
	void isVisible_duringMouseStrategy_returnsTrue() throws Exception {

		inputStrategySwitcher.setToMouseStrategy();

		assertTrue(button.getView().isVisible());
	}

	@Test
	void isVisible_duringKeyboardStrategy_returnsFalse() throws Exception {

		inputStrategySwitcher.setToKeyboardAndGamepadStrategy();

		assertFalse(button.getView().isVisible());
	}

	@BeforeAll
	public static void setUp() throws Exception {
		Gdx.gl20 = Gdx.gl30 = Gdx.gl31 = Gdx.gl32 = Mockito.mock(GL32.class);
		Gdx.gl = Gdx.gl20;
		Gdx.graphics = new MockGraphics();
		Gdx.files = Mockito.mock(Files.class);

		inputStrategySwitcher = new InputStrategySwitcher();
		inputStrategySwitcher.setToMouseStrategy();
		TextButtonStyle textButtonStyle =  new TextButtonStyle();
		MyTextButton textButton = new MyTextButton("", textButtonStyle);
		button = new MouseOnlyButton(textButton, Suppliers.emptyString(), Runnables.nullRunnable(), inputStrategySwitcher, Runnables.nullRunnable())  {
			@Override
			public boolean shouldBeUnregistered() {
				return false;
			}
		};
	}
}
