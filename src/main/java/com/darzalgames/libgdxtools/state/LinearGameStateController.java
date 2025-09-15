package com.darzalgames.libgdxtools.state;

/**
 * This class is a more simple GameStateController which doesn't really involve the player interacting with UI
 */
public abstract class LinearGameStateController extends GameStateController {

	protected LinearGameStateController(GameState state) {
		super(state);
	}

	@Override
	public final void focusCurrent() {}

	@Override
	public final void clearSelected() {}

	@Override
	public void selectDefault() {}

}
