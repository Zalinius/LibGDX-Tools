package com.darzalgames.libgdxtools.ui.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.darzalcommon.state.Endable;
import com.darzalgames.libgdxtools.ui.input.LogicalInputConsumer;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriorityStack;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;

/**
 * A distinct screen in the game (e.g. the main menu, a particular phase of gameplay) which handles making sure
 * its child actors are visible and can be interacted with, and ensures proper cleanup when the screen is departed from. 
 * 
 * We're ok to implement {@link LogicalInputConsumer} here because a GameScreen is always (so far) a wrapper for another InputConsumer,
 * such as a {@link NavigableListMenu}. This class is just responsible for cleaning up between screens.
 */
public abstract class GameScreen extends Group implements Screen, Endable, LogicalInputConsumer {

	private final Runnable leaveScreenRunnable;
	private final InputPriorityStack inputPriorityStack;

	protected GameScreen(Runnable leaveScreenRunnable, InputPriorityStack inputPriorityStack) {
		this.leaveScreenRunnable = leaveScreenRunnable;
		this.inputPriorityStack = inputPriorityStack;
	}

	@Override
	public void show() {
		InputPriority.claimPriority(this);
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
	public void end() {
		leaveScreenRunnable.run();
	}

	@Override
	public final void dispose() {}

}
