package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.*;
import com.darzalgames.libgdxtools.ui.input.Input;

public class SteamGamepadInputHandler extends GamepadInputHandler {

	private static SteamController steamController;
	private static SteamControllerHandle activeController;

	private SteamControllerActionSetHandle actionsSetHandle;
	private final Map<SteamControllerDigitalActionHandle, Input> buttonMappings;

	private boolean justDisconnected;


	public SteamGamepadInputHandler(SteamController steamController) {
		buttonMappings = new HashMap<>();
		SteamGamepadInputHandler.steamController = steamController;
		justDisconnected = false;

		actionsSetHandle = steamController.getActionSetHandle("MenuControls");

		buttonMappings.put(steamController.getDigitalActionHandle("menu_select"), Input.ACCEPT);
		buttonMappings.put(steamController.getDigitalActionHandle("menu_cancel"), Input.BACK);
		buttonMappings.put(steamController.getDigitalActionHandle("menu_pause"), Input.PAUSE);
		buttonMappings.put(steamController.getDigitalActionHandle("menu_skip"), Input.SKIP);


		buttonMappings.put(steamController.getDigitalActionHandle("menu_up"), Input.UP);
		buttonMappings.put(steamController.getDigitalActionHandle("menu_down"), Input.DOWN);
		buttonMappings.put(steamController.getDigitalActionHandle("menu_left"), Input.LEFT);
		buttonMappings.put(steamController.getDigitalActionHandle("menu_right"), Input.RIGHT);

		Gdx.app.log("GamepadInputHandler", "Using STEAM gamepad input handling.");
	}


	@Override
	public void act(float delta) {
		super.act(delta);
		checkForControllerConnected();
		updateButtonsState();
	}

	private void checkForControllerConnected() {
		SteamControllerHandle[] handlesOut = new SteamControllerHandle[SteamController.STEAM_CONTROLLER_MAX_COUNT];
		steamController.getConnectedControllers(handlesOut);

		for (int i = 0; i < handlesOut.length; i++) {
			SteamControllerHandle steamControllerHandle = handlesOut[i];
			if (steamControllerHandle != null) {
				steamController.activateActionSet(steamControllerHandle, actionsSetHandle); // TODO do this on controller connect instead of in act()
			}
		}

		if (activeController != null 
				&& !Arrays.asList(handlesOut).contains(activeController)
				&& !justDisconnected) {
			this.controllerDisconnected();
			justDisconnected = true;
			activeController = null;
		} else {
			justDisconnected = false;
		}

		if (Arrays.asList(handlesOut).stream().anyMatch(handle -> handle != null)) {
			for (int i = 0; i < handlesOut.length; i++) {
				SteamControllerHandle steamControllerHandle = handlesOut[i];
				for(Entry<SteamControllerDigitalActionHandle, Input> entry : buttonMappings.entrySet()) {
					SteamControllerDigitalActionData digitalActionData = new SteamControllerDigitalActionData();
					if (steamControllerHandle != null) {
						steamController.getDigitalActionData(steamControllerHandle, entry.getKey(), digitalActionData);
						boolean isCurrentlyPressedAccordingToPoling = digitalActionData.getState();
						if (isCurrentlyPressedAccordingToPoling) {
							activeController = steamControllerHandle;
							break;
						}
					}
				}
			}
		}
	}


	private void updateButtonsState() {
		SteamControllerHandle steamControllerHandle = activeController;
		for(Entry<SteamControllerDigitalActionHandle, Input> entry : buttonMappings.entrySet()) {
			SteamControllerDigitalActionHandle actionHandle = entry.getKey();
			SteamControllerDigitalActionData digitalActionData = new SteamControllerDigitalActionData();
			if (steamControllerHandle != null) {
				steamController.getDigitalActionData(steamControllerHandle, entry.getKey(), digitalActionData);
				boolean isCurrentlyPressedAccordingToPoling = digitalActionData.getState();
				ButtonState latestState = buttonStates.get(buttonMappings.get(actionHandle));
				if (isCurrentlyPressedAccordingToPoling) {
					if (latestState.equals(ButtonState.NOT_HELD_DOWN)) {
						justPressed(entry.getValue());
						changeButtonState(actionHandle, ButtonState.HELD_DOWN);
					}
				} else {
					if (latestState.equals(ButtonState.HELD_DOWN)) {
						justReleased(entry.getValue());
						changeButtonState(actionHandle, ButtonState.NOT_HELD_DOWN);
					} 
				}
			}
		}

	}

	private void changeButtonState(SteamControllerDigitalActionHandle handle, ButtonState buttonState) {
		buttonStates.put(buttonMappings.get(handle), buttonState);
	}

	public static boolean isAControllerConnected() {
		if (steamController != null) {
			SteamControllerHandle[] handlesOut = new SteamControllerHandle[SteamController.STEAM_CONTROLLER_MAX_COUNT];
			steamController.getConnectedControllers(handlesOut);
			return Arrays.asList(handlesOut).stream().anyMatch(handle -> handle != null);
		}
		return false;
	}

	public static void openControlsOverlay() {
		if (steamController != null && isAControllerConnected() && activeController != null) {
			steamController.showBindingPanel(activeController);
		}
	}
}
