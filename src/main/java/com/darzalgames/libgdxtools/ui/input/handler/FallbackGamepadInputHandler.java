package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputReceiver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public abstract class FallbackGamepadInputHandler extends GamepadInputHandler implements ControllerListener {

	private final Map<Function<Controller, Integer>, Input> buttonMappings;
	protected abstract Map<Function<Controller, Integer>, Input> makeButtonMappings();
	private final Map<Input, AssetDescriptor<Texture>> glyphMappings;
	protected abstract Map<Input, AssetDescriptor<Texture>> makeGlyphMappings();

	protected FallbackGamepadInputHandler(InputStrategySwitcher inputStrategySwitcher, InputReceiver inputReceiver) {
		super(inputStrategySwitcher, inputReceiver);
		Controllers.addListener(this); // receives events from all controllers
		buttonMappings = makeButtonMappings();
		glyphMappings = makeGlyphMappings();

		Gdx.app.log("GamepadInputHandler", "Using FALLBACK gamepad input handling.");
	}

	@Override
	public void connected(Controller controller) {
		inputStrategySwitcher.setToKeyboardAndGamepadStrategy();
	}

	@Override
	public void disconnected(Controller controller) {
		this.controllerDisconnected();
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		for(Entry<Function<Controller, Integer>, Input> entry : buttonMappings.entrySet()) {
			if (entry.getKey().apply(controller) == buttonCode) {
				justPressed(entry.getValue());
			}
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		for(Entry<Function<Controller, Integer>, Input> entry : buttonMappings.entrySet()) {
			if (entry.getKey().apply(controller) == buttonCode) {
				justReleased(entry.getValue());
			}
		}
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// TODO Set this up to handle diagonal axis input?
		Input inputKey = Input.NONE;			
		if (isXAxis(axisCode, controller)) {
			inputKey = (value > 0 ? Input.RIGHT : Input.LEFT);
		}

		if (isYAxis(axisCode, controller)) {
			inputKey = (value > 0 ? Input.DOWN : Input.UP);
		}

		if (inputKey != Input.NONE) {
			if (Math.abs(value) >= 1.0f) {
				justPressed(inputKey);
			} else if (Math.abs(value) <= .1f) { // A homemade deadzone, as long as the implementation works for us developers (and the itch demo?) I suppose it's okay
				justReleased(inputKey);
			}
		}

		return false;
	}

	public static boolean isYAxis(int axisCode, Controller controller) {
		return axisCode == controller.getMapping().axisLeftY
				|| axisCode == controller.getMapping().axisRightY;
	}

	public static boolean isXAxis(int axisCode, Controller controller) {
		return axisCode == controller.getMapping().axisLeftX
				|| axisCode == controller.getMapping().axisRightX;
	}

	@Override
	public Texture getGlyphForInput(Input input) {
		if (glyphMappings.containsKey(input)) {
			return getTextureFromDescriptor(glyphMappings.get(input));
		}
		return null;
	}

}
