package com.darzalgames.libgdxtools.ui.input.handler;

import com.badlogic.gdx.graphics.Texture;
import com.darzalgames.libgdxtools.ui.input.Input;

public class GlyphFactory {

	private static InputHandler latestInputHandler;

	static void setLatestInputHandler(InputHandler latestInputHandler) {
		GlyphFactory.latestInputHandler = latestInputHandler;
	}

	public static Texture getGlyphForInput(Input input) {
		return latestInputHandler.getGlyphForInput(input);
	}

	// TODO Quest Giver needs to know this to show the keyboard controls on the main menu...
	public static boolean isCurrentlyKeyboardInput() {
		return latestInputHandler instanceof KeyboardInputHandler;
	}
	
}
