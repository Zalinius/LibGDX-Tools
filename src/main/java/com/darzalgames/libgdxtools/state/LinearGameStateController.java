package com.darzalgames.libgdxtools.state;

/**
 * This class is a more simple GameStateController Which doesn't really involve the player interacting with UI
 * 
 * @author DarZal
 *
 */
public abstract class LinearGameStateController extends GameStateController {

	protected LinearGameStateController(GameState state) {
		this(state, false);
	}

	protected LinearGameStateController(GameState state, boolean allowsEndingsAfter) {
		super(state, allowsEndingsAfter);
	}
	
	@Override
	public final void focusCurrent() {}
	@Override
	public final void clearSelected() {}
	@Override
	public void selectDefault() {}

}
