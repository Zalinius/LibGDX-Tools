package com.darzalgames.libgdxtools.ui.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.darzalcommon.state.Endable;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriorityStack;
import com.darzalgames.libgdxtools.ui.input.inputpriority.Priority;

/**
 * @author DarZal
 * A distinct screen in the game (e.g. the main menu, a particular phase of gameplay) which handles making sure
 * its child actors are visible and can be interacted with, and ensures proper cleanup when the screen is left from. 
 */
public abstract class GameScreen extends Group implements Screen, Endable, InputConsumer {

	private final Runnable leaveScreenRunnable;
	private final InputPriorityStack inputPriorityStack;

	protected GameScreen(Runnable leaveScreenRunnable, InputPriorityStack inputPriorityStack) {
		this.leaveScreenRunnable = leaveScreenRunnable;
		this.inputPriorityStack = inputPriorityStack;
	}

	@Override
	public void show() {
		Priority.claimPriority(this);
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
		inputPriorityStack.clearChildren();
		releasePriority();
	}
	@Override
	public void selectDefault() {}
	@Override
	public void clearSelected() {}
	@Override
	public void focusCurrent() {}
	@Override
	public void consumeKeyInput(Input input) {}

	@Override
	public void end() {
		leaveScreenRunnable.run();
	}

	@Override
	public final void dispose() {}

}
