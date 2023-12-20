package com.darzalgames.libgdxtools.ui.input.handler;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.darzalcommon.misc.DoesNotPause;

public abstract class InputHandler extends Table implements DoesNotPause {
	
	public enum InputMethod {
		MOUSE,
		KEYBOARD,
		GAMEPAD,
	}
	
	protected static InputMethod latestInputMethod = InputMethod.MOUSE; 
	
	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}

	public static InputMethod getLatestInputMethod() {
		return latestInputMethod;
	}

}
