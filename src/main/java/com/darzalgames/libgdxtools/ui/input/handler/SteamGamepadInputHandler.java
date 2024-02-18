package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.*;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public abstract class SteamGamepadInputHandler extends GamepadInputHandler {

	private SteamController steamController;
	private SteamControllerHandle activeController;

	private SteamControllerActionSetHandle actionsSetHandle;
	private final Map<SteamControllerDigitalActionHandle, Input> buttonMappings;
	protected abstract Map<SteamControllerDigitalActionHandle, Input> makeButtonMappings(SteamController steamController);

	private boolean justDisconnected;

	protected SteamGamepadInputHandler(InputStrategyManager inputStrategyManager, String actionsSetHandleKey) {
		super(inputStrategyManager);
		SteamControllerManager.initialize(this);
		GameInfo.getSteamStrategy().acceptController(this);
		justDisconnected = false;
		// TODO maybe some day add support for multiple action sets
		this.actionsSetHandle = steamController.getActionSetHandle(actionsSetHandleKey);	
		buttonMappings = makeButtonMappings(steamController);

		Gdx.app.log("GamepadInputHandler", "Using STEAM gamepad input handling.");
	}
	
	public void setSteamController(SteamController steamController) {
		this.steamController = steamController;
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

		updateActionSets(handlesOut);

		checkForControllerJustDisconnected(handlesOut);

		checkForActiveController(handlesOut);
	}

	private void updateActionSets(SteamControllerHandle[] handlesOut) {
		for (int i = 0; i < handlesOut.length; i++) {
			SteamControllerHandle steamControllerHandle = handlesOut[i];
			if (steamControllerHandle != null) {
				steamController.activateActionSet(steamControllerHandle, actionsSetHandle);
			}
		}
	}

	private void checkForControllerJustDisconnected(SteamControllerHandle[] handlesOut) {
		if (activeController != null 
				&& !Arrays.asList(handlesOut).contains(activeController)
				&& !justDisconnected) {
			this.controllerDisconnected();
			justDisconnected = true;
			activeController = null;
		} else {
			justDisconnected = false;
		}
	}

	private void checkForActiveController(SteamControllerHandle[] handlesOut) {
		if (Arrays.asList(handlesOut).stream().anyMatch(Objects::nonNull)) {
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

	public boolean isAControllerConnected() {
		if (steamController != null) {
			SteamControllerHandle[] handlesOut = new SteamControllerHandle[SteamController.STEAM_CONTROLLER_MAX_COUNT];
			steamController.getConnectedControllers(handlesOut);
			return Arrays.asList(handlesOut).stream().anyMatch(Objects::nonNull);
		}
		return false;
	}

	public void openControlsOverlay() {
		if (steamController != null && isAControllerConnected() && activeController != null) {
			steamController.showBindingPanel(activeController);
		}
	}
}
