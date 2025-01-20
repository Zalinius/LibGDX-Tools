package com.darzalgames.libgdxtools.ui.input.handler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

/**
 * The base class for all kinds of input handlers (mouse, keyboard, gamepad)
 */
public abstract class InputHandler extends Table implements DoesNotPause {
	
	protected final InputStrategyManager inputStrategyManager;
	private final InputMethod inputMethod;
	
	protected InputHandler(InputStrategyManager inputStrategyManager, InputMethod inputMethod) {
		this.inputStrategyManager = inputStrategyManager;
		this.inputMethod = inputMethod;
	}

	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}
	
	public InputMethod getInputMethod() {
		return inputMethod;
	}

	public abstract Texture getGlyphForInput(Input input);
	
	void updateLatestInputMethod() {
		GlyphFactory.setLatestInputMethod(inputMethod);
	}

}
