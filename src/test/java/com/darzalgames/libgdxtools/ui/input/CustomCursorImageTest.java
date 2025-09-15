package com.darzalgames.libgdxtools.ui.input;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.ui.CustomCursorImage;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

class CustomCursorImageTest {

	@Test
	void isVisible_duringMouseStrategy_returnsTrue() throws Exception {
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		CustomCursorImage customCursor = new CustomCursorImage(() -> true, null, inputStrategySwitcher);

		inputStrategySwitcher.setToMouseStrategy();

		assertTrue(customCursor.isVisible());
	}

	@Test
	void isVisible_duringKeyboardStrategy_returnsFalse() throws Exception {
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		CustomCursorImage customCursor = new CustomCursorImage(() -> true, null, inputStrategySwitcher);

		inputStrategySwitcher.setToKeyboardAndGamepadStrategy();

		assertFalse(customCursor.isVisible());
	}
}
