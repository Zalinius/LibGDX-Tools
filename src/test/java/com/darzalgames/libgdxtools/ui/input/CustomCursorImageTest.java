package com.darzalgames.libgdxtools.ui.input;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;
import com.darzalgames.libgdxtools.ui.input.strategy.KeyboardInputStrategy;
import com.darzalgames.libgdxtools.ui.input.strategy.MouseInputStrategy;

class CustomCursorImageTest {

	@Test
	void isVisible_duringMouseStrategy_returnsTrue() throws Exception {
		InputStrategyManager inputStrategyManager = new InputStrategyManager(new MouseInputStrategy(), new KeyboardInputStrategy());
		CustomCursorImage customCursor = new CustomCursorImage(() -> true, null, inputStrategyManager);
		
		inputStrategyManager.setToMouseStrategy();

		assertTrue(customCursor.isVisible());
	}
	
	@Test
	void isVisible_duringKeyboardStrategy_returnsFalse() throws Exception {
		InputStrategyManager inputStrategyManager = new InputStrategyManager(new MouseInputStrategy(), new KeyboardInputStrategy());
		CustomCursorImage customCursor = new CustomCursorImage(() -> true, null, inputStrategyManager);
		
		inputStrategyManager.setToKeyboardStrategy();

		assertFalse(customCursor.isVisible());
	}
}
