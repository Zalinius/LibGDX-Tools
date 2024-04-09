package com.darzalgames.libgdxtools.ui.input;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

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
		
		if (handlers.containsKey(latest)) {
			return handlers.get(latest).getGlyphForInput(input);
		}
		
		return null;
	}

}
