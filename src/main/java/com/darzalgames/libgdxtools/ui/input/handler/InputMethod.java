package com.darzalgames.libgdxtools.ui.input.handler;

/**
 * The various input methods that we support.
 * Some strategies will share an input method, e.g. the Steam gamepad versus the LibGDX fallback gamepad handling 
 */
public enum InputMethod {
	MOUSE,
	KEYBOARD,
	GAMEPAD,
}