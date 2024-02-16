package com.darzalgames.libgdxtools.state;

/**
 * GameStates are created and managed by the StateMachine. They generally interact with a Phase 
 * and various Menus to make the game do things.
 */
public interface GameState {

	/**
	 * Called each time the GameState starts, useful for refreshing values and reacting to changes
	 * since the state was last active as needed.
	 */
	public abstract void enterState();

}
