package com.darzalgames.libgdxtools.ui.input.handler;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.darzalcommon.state.DoesNotPause;

public abstract class InputHandler extends Table implements DoesNotPause {
	
	public enum InputMethod {
		MOUSE,
		KEYBOARD,
		GAMEPAD,
	}
	
	private static InputMethod latestInputMethod = InputMethod.MOUSE;
	protected static void setLatestInputMethod(InputMethod inputMethod) {
		latestInputMethod = inputMethod;
	}
	
	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}

	public static InputMethod getLatestInputMethod() {
		return latestInputMethod;
	}

}
