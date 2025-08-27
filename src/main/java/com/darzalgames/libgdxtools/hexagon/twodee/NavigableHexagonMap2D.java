package com.darzalgames.libgdxtools.hexagon.twodee;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonDirection;
import com.darzalgames.libgdxtools.maingame.StageBest;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.InputOnHexagonGrid;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputStrategyObserver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

/**
 * Allows for navigation around a {@link HexagonControllerMap2D} using keyboard or gamepad
 * @param <E> The game-specific object associated with each {@link Hexagon} and {@link HexagonController2D}
 */
public class NavigableHexagonMap2D<E> extends Container<HexagonControllerMap2D<E>> implements InputConsumer, InputStrategyObserver {

	private Hexagon currentHexagon;

	/**
	 * Be sure to register this object with the {@link InputStrategySwitcher}
	 * @param hexagonControllerMap The map of hexagon controllers to be navigated
	 */
	public NavigableHexagonMap2D(HexagonControllerMap2D<E> hexagonControllerMap) {
		setActor(hexagonControllerMap);
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
		if (getActor().containsHexagon(neighborHexagon)) {
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
		getActor().unfocusAll();
		if (inputStrategySwitcher.isMouseMode()) {
			getActor().setTouchable(Touchable.enabled);
		} else {
			getActor().setTouchable(Touchable.disabled);
			focusCurrent();
		}
	}


	@Override
	public boolean shouldBeUnregistered() {
		return getStage() == null;
	}

	private boolean isInputAllowed() {
		return StageBest.isInTouchableBranch(this);
	}

	/**
	 * @return The visual representation of the currently selected hexagon (keyboard and gamepad)
	 */
	public HexagonController2D getCurrentHexagonController() {
		return getActor().getControllerOf(currentHexagon);
	}

	@Override
	public void resizeUI() {
		getActor().resizeUI();
	}

	@Override
	public boolean isDisabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBlank() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAlignment(Alignment alignment) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocused(boolean focused) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDisabled(boolean disabled) {
		// TODO Auto-generated method stub

	}

}
