package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.darzalgames.libgdxtools.ui.input.Input;

public class GlyphFactory {

	private Map<InputMethod, InputHandler> handlers;
	private InputHandler latestInputHandler;
	
	public void initialize(List<InputHandler> allHandlers) {
		handlers = new EnumMap<>(InputMethod.class);
		latestInputHandler = InputMethod.MOUSE;

		allHandlers.forEach(handler -> handlers.put(handler.getInputMethod(), handler));
	}
	
	void setLatestInputMethod(InputHandler latestInputHandler) {
		this.latestInputHandler = latestInputHandler;
	}

	public Texture getGlyphForInput(Input input) {		
		InputHandler inputHandler = handlers.get(latestInputHandler); 
		if (inputHandler != null) {
 			return inputHandler.getGlyphForInput(input);
		}
		
		Gdx.app.error("GlyphFactory", "Missing glyph setup for: " + " " + latestInputHandler);
		return null;
	}

}
