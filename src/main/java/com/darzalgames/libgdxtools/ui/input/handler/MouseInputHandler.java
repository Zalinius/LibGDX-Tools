package com.darzalgames.libgdxtools.ui.input.handler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class MouseInputHandler extends InputHandler {

	/**
	 * Listens for click events and changes into mouse mode correspondingly
	 */
	public MouseInputHandler(InputStrategySwitcher inputStrategySwitcher) {
		super(inputStrategySwitcher);
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				updateLatestInputMethod();
				inputStrategySwitcher.setToMouseStrategy();
				return false;
			}
		});

		addAction(
				Actions.forever(
						new RunnableActionBest(
								() -> setSize(getStage().getWidth(), getStage().getHeight())
						)
				)
		); // I'm PRETTY CONFIDENT™ that we can count on the stage existing, because otherwise who is calling act() on this?
		setTouchable(Touchable.enabled);
		updateLatestInputMethod();
	}

	@Override
	public Texture getGlyphForInput(Input input) {
		return null;
	}

}
