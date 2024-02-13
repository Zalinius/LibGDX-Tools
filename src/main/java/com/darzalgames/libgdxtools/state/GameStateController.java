package com.darzalgames.libgdxtools.state;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;

public abstract class GameStateController implements InputConsumer {
	
	private GameState state;
	private Group group;
	
	protected GameStateController(final GameState state) {
		super();
		this.state = state;
		this.group = new Group();
	}
	
	protected abstract Actor getView();

	/**
	 * Since a GameStateController owns an {@link Actor} rather than being one,
	 * This is how we put it into a parent group 
	 * @param parentGroup The new parent {@link Actor} for the actor that represents the {@link GameState}
	 */
	public void addSelfToGroup(Group parentGroup)
	{
		parentGroup.addActor(group);
	}

	/**
	 * Handles clearing out any old information from the last time the state was entered,
	 * and freshly initializing the state and visuals
	 */
	public void enterState()
	{
		group.clearChildren();
		state.enterState();
		group.addActor(getView()); // enter the state first since creating the view may require state information
	}

	@Override
	public void setTouchable(Touchable isTouchable) {
		group.setTouchable(isTouchable);
	}
}
