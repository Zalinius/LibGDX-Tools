package com.darzalgames.libgdxtools.hexagon;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonDirection;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.*;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputObserver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

/**
 * Allows for navigation around a {@link HexagonControllerMap} using keyboard or gamepad
 * @author DarZal
 * @param <E>
 */
public class NavigableHexagonMap<E> extends Container<HexagonControllerMap<E>> implements InputConsumer, InputObserver {

	private final HexagonControllerMap<E> hexagonControllerMap;
	private Hexagon currentHexagon;

	/**
	 * Be sure to register this object with the {@link InputStrategySwitcher}
	 * @param hexagonControllerMap The map of hexagon controllers to be navigated
	 */
	public NavigableHexagonMap(HexagonControllerMap<E> hexagonControllerMap) {
		this.hexagonControllerMap = hexagonControllerMap;
		this.setActor(hexagonControllerMap);
	}

	@Override
	public void consumeKeyInput(Input input) {
		if (isInputAllowed()) {
			if (input.equals(Input.ACCEPT)) {
				getCurrentHexagonController().press();
			} else {
				HexagonDirection direction = InputOnHexagonGrid.getDirectionFromInput(input);
				navigateToNeighborInDirection(direction);
			}
		}
	}

	private void navigateToNeighborInDirection(HexagonDirection direction) {
		if (direction != null) {
			Hexagon neighborHexagon = HexagonDirection.getNeighborHexagon(currentHexagon, direction);
			if (hexagonControllerMap.containsHexagon(neighborHexagon)) {
				unfocusCurrentHexagon();
				currentHexagon = neighborHexagon;
				focusCurrent();
			}
		}
	}

	@Override
	public void gainFocus() {
		selectDefault();
		unfocusCurrentHexagon();
		hexagonControllerMap.centerSelf();
		centerSelf();
	}

	@Override
	public void regainFocus() {
		selectDefault();
	}

	@Override
	public void focusCurrent() {
		if (isInputAllowed()) {
			getCurrentHexagonController().setButtonFocused(true);
		}
	}

	@Override
	public void clearSelected() {
		unfocusCurrentHexagon();
	}

	private void unfocusCurrentHexagon() {
		getCurrentHexagonController().setButtonFocused(false);
	}

	@Override
	public void selectDefault() {
		currentHexagon = new Hexagon(0, 0);
		focusCurrent();
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategySwitcher) {
		if (inputStrategySwitcher.showMouseExclusiveUI()) {
			hexagonControllerMap.setTouchable(Touchable.enabled);
		} else {
			focusCurrent();	
		}
	}


	@Override
	public boolean shouldBeUnregistered() {
		return this.getStage() == null;
	}

	private boolean isInputAllowed() {
		return UniversalInputStage.isInTouchableBranch(this);
	}

	/**
	 * @return The visual representation of the currently selected hexagon (keyboard and gamepad)
	 */
	public HexagonController getCurrentHexagonController() {
		return hexagonControllerMap.getControllerOf(currentHexagon);
	}

	private void centerSelf() {
		this.setPosition(GameInfo.getWidth() / 2f - this.getWidth() / 2, GameInfo.getHeight() / 2f - this.getHeight() / 2);
	}

}
