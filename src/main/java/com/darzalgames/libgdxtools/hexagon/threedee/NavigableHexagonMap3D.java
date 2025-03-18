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
	private HexagonControllerMap3D<E> hexagonControllerMap;

	/**
	 * Be sure to register this object with the {@link InputStrategySwitcher}
	 * @param hexagonControllerMap The map of hexagon controllers to be navigated
	 */
	public NavigableHexagonMap3D(HexagonControllerMap3D<E> hexagonControllerMap, InputStrategySwitcher inputStrategySwitcher) {
		this.hexagonControllerMap = hexagonControllerMap;
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
		if (!inputStrategySwitcher.isMouseMode()) {
			focusCurrent();	
		}
	}


	@Override
	public boolean shouldBeUnregistered() {
		// TODO  this
		return false;
//		return this.getStage() == null;
	}

	private boolean isInputAllowed() {
		// TODO  this
		return true;
//		return UniversalInputStage.isInTouchableBranch(this);
	}

	/**
	 * @return The visual representation of the currently selected hexagon (keyboard and gamepad)
	 */
	public HexagonController3D getCurrentHexagonController() {
		return hexagonControllerMap.getControllerOf(currentHexagon);
	}

	@Override
	public void resizeUI() {
		hexagonControllerMap.resizeUI();
	}

	@Override
	public void setTouchable(Touchable isTouchable) {
		// TODO Auto-generated method stub
	}

}
