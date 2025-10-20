package com.darzalgames.libgdxtools.ui.input.inputpriority;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;

class BlankBaseInputConsumerTest {

	@Test
	void consumeKeyInput_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(() -> base.consumeKeyInput(Input.ACCEPT));
	}

	@Test
	void setTouchable_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(() -> base.setTouchable(Touchable.disabled));
	}

	@Test
	void focusCurrent_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(base::focusCurrent);
	}

	@Test
	void clearSelected_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(base::clearSelected);
	}

	@Test
	void selectDefault_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(base::selectDefault);
	}

	@Test
	void loseFocus_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(base::loseFocus);
	}

	@Test
	void toString_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(base::toString);
	}

	@Test
	void toString_returnsMeaningfulName() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();

		String name = base.toString();

		assertTrue(name.toLowerCase().contains("blank"));
	}

	@Test
	void resizeUI_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(base::resizeUI);
	}

	@Test
	void isDisabled_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(base::isDisabled);
	}

	@Test
	void isBlank_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(base::isBlank);
	}

	@Test
	void setAlignment_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(() -> base.setAlignment(Alignment.CENTER));
	}

	@Test
	void setFocused_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(() -> base.setFocused(true));
	}

	@Test
	void setDisabled_doesNotThrow() {
		BlankBaseInputConsumer base = new BlankBaseInputConsumer();
		assertDoesNotThrow(() -> base.setDisabled(true));
	}
}
