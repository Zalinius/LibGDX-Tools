package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.Alignment;

/**
 * These are for more logic-heavy objects which still need to be on the input stack,
 * but which don't themselves do much interaction with input. For example, in Quest Giver the WorldStateMachine
 * proceeds to the next state whenever it regains focus (i.e. the previous state released focus)
 * but doesn't itself have any UI or interactions with the player. Another example is the
 * SteamAchievement states which grant an achievement and immediately release focus.
 */
public interface LogicalInputConsumer extends InputConsumer {

	@Override
	default void setTouchable(Touchable isTouchable) {}

	@Override
	default void selectDefault() {}

	@Override
	default void focusCurrent() {}

	@Override
	default void consumeKeyInput(Input input) {}

	@Override
	default void clearSelected() {}

	@Override
	default void setAlignment(Alignment alignment) {}

	@Override
	default void setFocused(boolean focused) {}

	@Override
	default void setDisabled(boolean disabled) {}

	@Override
	default boolean isDisabled() {
		return false;
	}

	@Override
	default boolean isBlank() {
		return false;
	}
}
