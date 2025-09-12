package com.darzalgames.libgdxtools.ui.input.handler;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.codedisaster.steamworks.*;
import com.codedisaster.steamworks.SteamController.ActionOrigin;
import com.darzalgames.darzalcommon.data.BiMap;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputReceiver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public abstract class SteamGamepadInputHandler extends GamepadInputHandler {

	private final Supplier<String> currentActionsSet;

	private SteamController steamController;
	private SteamControllerHandle activeController;

	private SteamControllerActionSetHandle actionSetHandle;
	private final String joystickActionName;
	private BiMap<SteamControllerDigitalActionHandle, Input> buttonMappings;

	protected abstract BiMap<SteamControllerDigitalActionHandle, Input> makeButtonMappings(SteamController steamController);

	private boolean justDisconnected;

	protected float currentX = 0;
	protected float currentY = 0;
	protected static final float INPUT_REPEAT_DELAY = 0.25f;
	protected float inputRepeatTimer = INPUT_REPEAT_DELAY;

	protected boolean darkMode = false;

	private final Map<AssetDescriptor<Texture>, Texture> existingGlyphs;

	protected SteamGamepadInputHandler(InputStrategySwitcher inputStrategySwitcher, InputReceiver inputReceiver, Supplier<String> startActionSet, String joystickActionName) {
		super(inputStrategySwitcher, inputReceiver);
		SteamControllerManager.initialize(this);
		justDisconnected = false;

		currentActionsSet = startActionSet;
		this.joystickActionName = joystickActionName;

		existingGlyphs = new HashMap<>();

		Gdx.app.log("GamepadInputHandler", "Using STEAM gamepad input handling.");
	}

	public void setSteamController(SteamController steamController) {
		this.steamController = steamController;
		setActionSet(currentActionsSet);
		buttonMappings = makeButtonMappings(steamController);
	}

	@Override
	public void setActionSet(Supplier<String> newActionSetKeySupplier) {
		actionSetHandle = steamController.getActionSetHandle(newActionSetKeySupplier.get());
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
				steamController.activateActionSet(steamControllerHandle, actionSetHandle);
			}
		}
	}

	private void checkForControllerJustDisconnected(SteamControllerHandle[] handlesOut) {
		if (activeController != null && !Arrays.asList(handlesOut).contains(activeController) && !justDisconnected) {
			controllerDisconnected();
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
					if (ButtonState.NOT_HELD_DOWN.equals(latestState)) {
						justPressed(input);
						changeButtonState(actionHandle, ButtonState.HELD_DOWN);
					}
				} else if (ButtonState.HELD_DOWN.equals(latestState)) {
					justReleased(input);
					changeButtonState(actionHandle, ButtonState.NOT_HELD_DOWN);
				}
			}
		}
	}

	private void updateAxisState() {
		inputRepeatTimer += Gdx.graphics.getDeltaTime();
		SteamControllerHandle steamControllerHandle = activeController;
		if (steamControllerHandle != null) {
			SteamControllerAnalogActionData analogActionData = new SteamControllerAnalogActionData();
			SteamControllerAnalogActionHandle analogActionHandle = steamController.getAnalogActionHandle(joystickActionName);
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
		ActionOrigin[] originsOut = new ActionOrigin[50]; // I think people would be hard pressed to map the same action to 50 different buttons

		if (activeController == null) {
			return null;
		}

		steamController.getDigitalActionOrigins(activeController, actionSetHandle, handle, originsOut);

		ActionOrigin action = originsOut[0];

		if (action == null) { // Check for analog joystick movement
			SteamControllerAnalogActionHandle handle2 = steamController.getAnalogActionHandle(joystickActionName);
			steamController.getAnalogActionOrigins(activeController, actionSetHandle, handle2, originsOut);

			// Prioritize left stick when several joysticks/dpads are available
			List<ActionOrigin> origins = Arrays.asList(originsOut);
			origins = origins.stream().filter(o -> o != null).collect(Collectors.toCollection(ArrayList::new));
			Comparator<ActionOrigin> comparator = Comparator.<ActionOrigin, Boolean>comparing(s -> s.toString().toLowerCase().contains("left") && s.toString().contains("stick")).reversed().thenComparing(Comparator.naturalOrder());
			origins.sort(comparator);

			if (origins.isEmpty()) {
				// no action for this, likely it's not bound for the current action set
				// this happens when in a menu but the game in the background still wants to show glyphs
				return null;
			}
			action = origins.get(0);
		}

		String absolutePath = steamController.getGlyphForActionOrigin(action);

		String direction = "";
		if (input.equals(Input.UP)) {
			direction = "_up";
		} else if (input.equals(Input.RIGHT)) {
			direction = "_right";
		} else if (input.equals(Input.DOWN)) {
			direction = "_down";
		} else if (input.equals(Input.LEFT)) {
			direction = "_left";
		}
		String ending = "_sz.png"; // all glyphs end in a size "sm"/"md"/"lg", and the direction goes before that
		if (!absolutePath.contains(direction)) {
			absolutePath = new StringBuilder(absolutePath).insert(absolutePath.length() - ending.length(), direction).toString();
		}

		if (darkMode) {
			absolutePath = absolutePath.replace("\\light\\", "\\dark\\");
			absolutePath = absolutePath.replace("\\knockout\\", "\\dark\\");
		}

		// color_button is the full-color buttons (xbox 360), color_outlined_button is dark with a colorful outline (xbox one)
		absolutePath = absolutePath.replace("color_button", "color_outlined_button");

		AssetDescriptor<Texture> descriptor = new AssetDescriptor<>(absolutePath, Texture.class);
		existingGlyphs.computeIfAbsent(descriptor, this::getTextureFromDescriptor);
		return existingGlyphs.get(descriptor);
	}
}
