package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.graphics.Texture;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;

public class GlyphFactory {

	private static GamepadInputHandler gamepadInputHandler;
	
	public static void initialize(GamepadInputHandler gamepadInputHandler) {
		GlyphFactory.gamepadInputHandler = gamepadInputHandler;
	}
	
	public static Texture getGlyphForInput(Input input) {
		return gamepadInputHandler.getGlyphForInput(input);
	}

}
