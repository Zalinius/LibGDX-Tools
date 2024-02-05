package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.InputObserver;


/**
 * @author DarZal
 * A {@link KeyboardButton} which will only be visible and intractable in mouse mode
 */
public class MouseOnlyButton extends KeyboardButton implements InputObserver {

	public MouseOnlyButton(TextButton textButton, Runnable runnable) {
		super(textButton, runnable);
		GameInfo.getInputStrategyManager().register(this);
		setVisibilityBasedOnCurrentInputStrategy();
	}

	@Override
	public void inputStrategyChanged() {
		setVisibilityBasedOnCurrentInputStrategy();
	}
	
	private void setVisibilityBasedOnCurrentInputStrategy() {
		this.getView().setVisible(GameInfo.getInputStrategyManager().showMouseExclusiveUI());
	}

	@Override
	public boolean shouldBeUnregistered() {
		return this.getView().getStage() == null;
	}
	
}
