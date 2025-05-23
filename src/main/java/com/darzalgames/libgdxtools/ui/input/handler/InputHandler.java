package com.darzalgames.libgdxtools.ui.input.handler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

/**
 * The base class for all kinds of input handlers (mouse, keyboard, gamepad)
 */

public abstract class InputHandler extends Actor implements DoesNotPause {

	protected final InputStrategySwitcher inputStrategySwitcher;

	protected InputHandler(InputStrategySwitcher inputStrategySwitcher) {
		this.inputStrategySwitcher = inputStrategySwitcher;
	}

	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}

	public abstract Texture getGlyphForInput(Input input);

	void updateLatestInputMethod() {
		GlyphFactory.setLatestInputHandler(this);
	}

}
