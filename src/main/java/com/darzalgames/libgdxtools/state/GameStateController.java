package com.darzalgames.libgdxtools.state;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.zalinius.questgiver.core.ui.GameObjectView;
import com.zalinius.questgiver.core.ui.input.InputConsumer;

public abstract class GameStateController implements GameObjectView, InputConsumer {
	
	private GameState state;
	private Group group;
	private boolean allowsEndingsAfter;
	
	public GameStateController(final GameState state) {
		this(state, false);
	}

	public GameStateController(GameState state, boolean allowsEndingsAfter) {
		super();
		this.state = state;
		this.allowsEndingsAfter = allowsEndingsAfter;
		this.group = new Group();
	}


	public void addSelfToGroup(Group parentGroup)
	{
		parentGroup.addActor(group);
	}

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
	

	public boolean allowsEndingsAfter() { return allowsEndingsAfter; }

}
