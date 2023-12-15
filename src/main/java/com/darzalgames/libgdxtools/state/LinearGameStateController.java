package com.darzalgames.libgdxtools.state;

/**
 * This class is a more simple GameStateController Which doesn't really involve the player interacting with UI
 * 
 * @author DarZal
 *
 */
public abstract class LinearGameStateController extends GameStateController {

	public LinearGameStateController(GameState state) {
		this(state, false);
	}

	public LinearGameStateController(GameState state, boolean allowsEndingsAfter) {
		super(state, allowsEndingsAfter);
	}
	
	@Override
	final public void focusCurrent() {}
	@Override
	final public void clearSelected() {}
	@Override
	public void selectDefault() {}

}
