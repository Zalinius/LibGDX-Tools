package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputPrioritizer;

public class FallbackGamepadInputHandler extends GamepadInputHandler implements ControllerListener {

	private final Map<Function<Controller, Integer>, Input> buttonMappings;

	public FallbackGamepadInputHandler() {
		Controllers.addListener(this); // receives events from all controllers
		buttonMappings = new HashMap<>();
		buttonMappings.put(controller -> controller.getMapping().buttonA, Input.ACCEPT);
		buttonMappings.put(controller -> controller.getMapping().buttonB, Input.BACK);
		buttonMappings.put(controller -> controller.getMapping().buttonStart, Input.PAUSE);
		buttonMappings.put(controller -> controller.getMapping().buttonBack, Input.SKIP);

		buttonMappings.put(controller -> controller.getMapping().buttonL1, Input.LEFT);
		buttonMappings.put(controller -> controller.getMapping().buttonR1, Input.RIGHT);
		buttonMappings.put(controller -> controller.getMapping().buttonDpadLeft, Input.LEFT);
		buttonMappings.put(controller -> controller.getMapping().buttonDpadRight, Input.RIGHT);
		buttonMappings.put(controller -> controller.getMapping().buttonDpadUp, Input.UP);
		buttonMappings.put(controller -> controller.getMapping().buttonDpadDown, Input.DOWN);


		Gdx.app.log("GamepadInputHandler", "Using FALLBACK gamepad input handling.");
	}

	@Override
	public void connected(Controller controller) {
		InputPrioritizer.enterKeyboardMode();
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
			} else if (Math.abs(value) <= .1f) { // A homemade deadzone, as long as the implementation works for us developers (and the itch demo?) it's okay
				justReleased(inputKey);
			}
		}

		return false;
	}

	private static boolean isYAxis(int axisCode, Controller controller) {
		return axisCode == controller.getMapping().axisLeftY
				|| axisCode == controller.getMapping().axisRightY;
	}

	private static boolean isXAxis(int axisCode, Controller controller) {
		return axisCode == controller.getMapping().axisLeftX
				|| axisCode == controller.getMapping().axisRightX;
	}

}
