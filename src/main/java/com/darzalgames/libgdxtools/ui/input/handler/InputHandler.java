package com.darzalgames.libgdxtools.ui.input.handler;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

/**
 * @author DarZal
 * The base class for all kinds of input handlers (mouse, keyboard, gamepad),
 * wich also tracks the latest input method used
 */
public abstract class InputHandler extends Table implements DoesNotPause {
	
	protected final InputStrategyManager inputStrategyManager;
	
	protected InputHandler(InputStrategyManager inputStrategyManager) {
		this.inputStrategyManager = inputStrategyManager;
	}

	/**
	 * @author DarZal
	 * The various input methods that we support.
	 * Some strategies will share an input method, e.g. the Steam gamepad versus the LibGDX gamepad 
	 */
	public enum InputMethod {
		MOUSE,
		KEYBOARD,
		GAMEPAD,
	}
	
	private static InputMethod latestInputMethod = InputMethod.MOUSE;
	
	/**
	 * Used by a child class to set the latest input method
	 * @param inputMethod
	 */
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
