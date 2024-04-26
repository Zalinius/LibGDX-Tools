package com.darzalgames.libgdxtools.ui.input.keyboard;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.handler.InputHandler;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public class MouseDetector extends InputHandler {

	/**
	 * Listens for click events and changes into mouse mode correspondingly
	 */
	public MouseDetector(InputStrategyManager inputStrategyManager) {
		super(inputStrategyManager, InputMethod.MOUSE);
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				setLatestInputMethod(MouseDetector.this.inputMethod);
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
