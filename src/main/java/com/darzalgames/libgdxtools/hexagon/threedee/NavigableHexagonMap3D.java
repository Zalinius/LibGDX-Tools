package com.darzalgames.libgdxtools.hexagon.threedee;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonDirection;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.InputOnHexagonGrid;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputObserver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

/**
 * Allows for navigation around a {@link HexagonControllerMap3D} using keyboard or gamepad
 * @param <E> The game-specific object associated with each {@link Hexagon} and {@link HexagonController3D}
 */
public class NavigableHexagonMap3D<E> implements InputConsumer, InputObserver {

	private Hexagon currentHexagon;
	private final HexagonControllerMap3D<E> hexagonControllerMap;
	private final Runnable focusCurrentHexagonUnderMouse;
	private boolean isMouseMode;
	private boolean isFocused;

	/**
	 * Be sure to register this object with the {@link InputStrategySwitcher}
	 * @param hexagonControllerMap The map of hexagon controllers to be navigated
	 */
	public NavigableHexagonMap3D(HexagonControllerMap3D<E> hexagonControllerMap, InputStrategySwitcher inputStrategySwitcher,
			Runnable focusCurrentHexagonUnderMouse) {
		this.hexagonControllerMap = hexagonControllerMap;
		this.focusCurrentHexagonUnderMouse = focusCurrentHexagonUnderMouse;
		inputStrategySwitcher.register(this);
	}

	@Override
	public void consumeKeyInput(Input input) {
		if (isInputAllowed()) {
			HexagonDirection direction = InputOnHexagonGrid.getDirectionFromInput(input);
			if (direction != null) {
				navigateToNeighborInDirection(direction);
			} else {
				getCurrentHexagonController().consumeKeyInput(input);
			}
		}
	}

	private void navigateToNeighborInDirection(HexagonDirection direction) {
		Hexagon neighborHexagon = HexagonDirection.getNeighborHexagon(currentHexagon, direction);
		if (hexagonControllerMap.containsHexagon(neighborHexagon)) {
			clearSelected();
			currentHexagon = neighborHexagon;
			focusCurrent();
		}
	}

	@Override
	public void gainFocus() {
		selectDefault();
		regainFocus();
	}
	
	@Override
	public void loseFocus() {
		isFocused = false;
	}
	
	@Override
	public void regainFocus() {
		isFocused = true;
	}

	@Override
	public void focusCurrent() {
		if (isInputAllowed()) {
			getCurrentHexagonController().focusCurrent();
		}
	}

	@Override
	public void clearSelected() {
		getCurrentHexagonController().clearSelected();
	}

	@Override
	public void selectDefault() {
		currentHexagon = new Hexagon(0, 0);
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategySwitcher) {
		hexagonControllerMap.unfocusAll();
		isMouseMode = inputStrategySwitcher.isMouseMode();
		if (!isMouseMode) {
			focusCurrent();	
		}
	}


	@Override
	public boolean shouldBeUnregistered() {
		// TODO  this... used to be : return this.getStage() == null;
		return false;
	}

	private boolean isInputAllowed() {
		return isFocused;
	}

	/**
	 * @return The visual representation of the currently selected hexagon (keyboard and gamepad)
	 */
	public HexagonController3D getCurrentHexagonController() {
		return hexagonControllerMap.getControllerOf(currentHexagon);
	}

	@Override
	public void resizeUI() {
		if (isMouseMode && isInputAllowed()) {
			hexagonControllerMap.unfocusAll();
			focusCurrentHexagonUnderMouse.run();
		}
		hexagonControllerMap.resizeUI();
	}

	@Override
	public void setTouchable(Touchable isTouchable) {
		// TODO Auto-generated method stub
	}

}
