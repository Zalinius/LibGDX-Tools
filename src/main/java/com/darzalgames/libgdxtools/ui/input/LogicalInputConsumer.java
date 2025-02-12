package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * These are for more logic-heavy objects which still need to be on the input stack,
 * but which don't themselves do much interaction with input. For example, in Quest Giver the WorldStateMachine
 * proceeds to the next state whenever it regains focus (i.e. the previous state released focus)
 * but doesn't itself have any UI or interactions with the player. Another example is the
 * SteamAchievement states which grant an achievement and immediately release focus.
 */
public interface LogicalInputConsumer extends InputConsumer {

		@Override public default void setTouchable(Touchable isTouchable) {}

		@Override public default void selectDefault() {}

		@Override public default void focusCurrent() {}

		@Override public default void consumeKeyInput(Input input) {}

		@Override public default void clearSelected() {}
}
