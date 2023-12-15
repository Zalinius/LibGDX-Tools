package com.darzalgames.libgdxtools.ui.input;

/**
 * This class is for more logic-heavy objects which still need to be on the stack,
 * but which don't themselves do much interaction with input. For example, the WorldStateMachine
 * proceeds to the next state whenever it regains focus (i.e. the previous state released focus)
 * but doesn't itself have any UI or interactions with the player. Another example is the
 * SimplePopUp, which doesn't really have UI to select and instead just closes itself based input.
 * 
 * @author DarZal
 *
 */
public interface InputConsumerWrapper extends InputConsumer {
	@Override
	public default void selectDefault() {}
	@Override
	public default void clearSelected() {}
	@Override
	public default void focusCurrent() {}
	@Override
	public default void consumeKeyInput(Input input) {}
}
