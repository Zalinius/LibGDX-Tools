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

}
