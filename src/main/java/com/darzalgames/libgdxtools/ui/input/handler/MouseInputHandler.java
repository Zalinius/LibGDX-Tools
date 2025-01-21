package com.darzalgames.libgdxtools.ui.input.handler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public class MouseInputHandler extends InputHandler {

	/**
	 * Listens for click events and changes into mouse mode correspondingly
	 */
	public MouseInputHandler(InputStrategyManager inputStrategyManager) {
		super(inputStrategyManager);
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				updateLatestInputMethod();
				inputStrategyManager.setToMouseStrategy();
				return false;
			}
		});
		this.setFillParent(true);
		this.setTouchable(Touchable.enabled);
	}

	@Override
	public Texture getGlyphForInput(Input input) {
		return null;
	}

}
