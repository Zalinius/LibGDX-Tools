package com.darzalgames.libgdxtools.ui.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.darzalcommon.state.Endable;
import com.darzalgames.libgdxtools.ui.input.InputConsumerWrapper;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;

/**
 * @author DarZal
 * A distinct screen in the game (e.g. the main menu, a particular phase of gameplay) which handles making sure
 * its child actors are visible and can be interacted with, and insures proper cleanup when the screen is left from. 
 */
public abstract class GameScreen extends Group implements Screen, Endable, InputConsumerWrapper {

	private final Runnable leaveScreenRunnable;

	protected GameScreen(Runnable leaveScreenRunnable) {
		this.leaveScreenRunnable = leaveScreenRunnable;
	}
	
	@Override
	public void show() {
		InputPriorityManager.addInputActor(this);
		InputPriorityManager.claimPriority(this);
	}
	
	@Override
	public void setTouchable(Touchable touchable) {
		super.setTouchable(Touchable.childrenOnly);
	}
	
	@Override
	public final void render(float delta) {}

	@Override
	public final void resize(int width, int height) {}

	@Override
	public final void pause() {}

	@Override
	public final void resume() {}

	@Override
	public void hide() {
		InputPriorityManager.clearChildren();
		InputPriorityManager.releasePriority(this);
	}
	
	@Override
	public void end() {
		leaveScreenRunnable.run();
	}

	@Override
	public final void dispose() {}

}
