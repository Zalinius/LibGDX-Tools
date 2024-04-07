package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.Arrays;
import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.SteamController.ActionOrigin;
import com.darzalgames.darzalcommon.data.BiMap;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public abstract class SteamGamepadInputHandler extends GamepadInputHandler {

	private final String actionsSetHandleKey;

	private SteamController steamController;
	private SteamControllerHandle activeController;

	private SteamControllerActionSetHandle actionsSetHandle;
	private BiMap<SteamControllerDigitalActionHandle, Input> buttonMappings;
	protected abstract BiMap<SteamControllerDigitalActionHandle, Input> makeButtonMappings(SteamController steamController);

	private boolean justDisconnected;

	protected float currentX = 0;
	protected float currentY = 0;
	protected static final float INPUT_REPEAT_DELAY = 0.25f;
	protected float inputRepeatTimer = INPUT_REPEAT_DELAY;

	protected SteamGamepadInputHandler(InputStrategyManager inputStrategyManager, String actionsSetHandleKey) {
		super(inputStrategyManager);
		SteamControllerManager.initialize(this);
		justDisconnected = false;

		// TODO maybe some day add support for multiple action sets
		this.actionsSetHandleKey = actionsSetHandleKey;

		Gdx.app.log("GamepadInputHandler", "Using STEAM gamepad input handling.");
	}

	public void setSteamController(SteamController steamController) {
		this.steamController = steamController;
		this.actionsSetHandle = steamController.getActionSetHandle(actionsSetHandleKey);	
		buttonMappings = makeButtonMappings(steamController);

	}


	@Override
	public void act(float delta) {
		super.act(delta);
		checkForControllerConnected();
		updateButtonsState();
		updateAxisState();
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
				for (SteamControllerDigitalActionHandle handle : buttonMappings.getFirstKeySet()) {
					SteamControllerDigitalActionData digitalActionData = new SteamControllerDigitalActionData();
					if (steamControllerHandle != null) {
						steamController.getDigitalActionData(steamControllerHandle, handle, digitalActionData);
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
		for (SteamControllerDigitalActionHandle actionHandle : buttonMappings.getFirstKeySet()) {
			SteamControllerDigitalActionData digitalActionData = new SteamControllerDigitalActionData();
			if (steamControllerHandle != null) {
				steamController.getDigitalActionData(steamControllerHandle, actionHandle, digitalActionData);
				boolean isCurrentlyPressedAccordingToPoling = digitalActionData.getState();
				Input input = buttonMappings.getSecondValue(actionHandle);
				ButtonState latestState = buttonStates.get(input);
				if (isCurrentlyPressedAccordingToPoling) {
					if (latestState.equals(ButtonState.NOT_HELD_DOWN)) {
						justPressed(input);
						changeButtonState(actionHandle, ButtonState.HELD_DOWN);
					}
				} else {
					if (latestState.equals(ButtonState.HELD_DOWN)) {
						justReleased(input);
						changeButtonState(actionHandle, ButtonState.NOT_HELD_DOWN);
					} 
				}
			}
		}
	}

	private void updateAxisState() {
		inputRepeatTimer += Gdx.graphics.getDeltaTime();
		SteamControllerHandle steamControllerHandle = activeController;
		if (steamControllerHandle != null) {
			SteamControllerAnalogActionData analogActionData = new SteamControllerAnalogActionData();
			SteamControllerAnalogActionHandle analogActionHandle = steamController.getAnalogActionHandle("Move");
			steamController.getAnalogActionData(steamControllerHandle, analogActionHandle, analogActionData);
			currentX = analogActionData.getX();
			currentY = analogActionData.getY();
			sendAxisInput();		
		}
	}

	private void changeButtonState(SteamControllerDigitalActionHandle handle, ButtonState buttonState) {
		buttonStates.put(buttonMappings.getSecondValue(handle), buttonState);
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
	

	protected abstract void sendAxisInput();
	

	@Override
	public Texture getGlyphForInput(Input input) {
		SteamControllerDigitalActionHandle handle = buttonMappings.getFirstValue(input);
		ActionOrigin[] originsOut = new ActionOrigin[10];
		
		if (activeController == null) {
			return null;
		}
		
		steamController.getDigitalActionOrigins(activeController,
				   actionsSetHandle,
				   handle,
				   originsOut);
		
		String absolutePath = steamController.getGlyphForActionOrigin(originsOut[0]);
		AssetDescriptor<Texture> descriptor = new AssetDescriptor<>(absolutePath, Texture.class);
		// TODO memory leak?
		return getTextureFromDescriptor(descriptor);
	}
}
