package com.darzalgames.libgdxtools.ui.input;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.darzalgames.libgdxtools.ui.input.handler.InputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.InputHandler.InputMethod;

public class GlyphFactory {

	private static Map<InputMethod, InputHandler> handlers;

	public static void initialize(InputHandler ... allHandlers) {
		handlers = new EnumMap<>(InputMethod.class);

		Arrays.asList(allHandlers).forEach(handler -> handlers.put(handler.getInputMethod(), handler));
	}

	public static Texture getGlyphForInput(Input input) {
		InputMethod latest = InputHandler.getLatestInputMethod();
		
		InputHandler inputHandler = handlers.get(latest); 
		if (inputHandler != null) {
 			return inputHandler.getGlyphForInput(input);
		}
		
		Gdx.app.error("GlyphFactory", "Missing glyph setup for: " + " " + latest);
		return null;
	}

}
