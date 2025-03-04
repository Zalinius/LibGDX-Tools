package com.darzalgames.libgdxtools.hexagon;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonDirection;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.*;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputObserver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

/**
 * Allows for navigation around a {@link HexagonControllerMap} using keyboard or gamepad
 * @param <E> The game-specific object associated with each {@link Hexagon} and {@link HexagonController}
 */
public class NavigableHexagonMap<E> extends Container<HexagonControllerMap<E>> implements InputConsumer, InputObserver {

	private Hexagon currentHexagon;

	/**
	 * Be sure to register this object with the {@link InputStrategySwitcher}
	 * @param hexagonControllerMap The map of hexagon controllers to be navigated
	 */
	public NavigableHexagonMap(HexagonControllerMap<E> hexagonControllerMap) {
		this.setActor(hexagonControllerMap);
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
		getActor().centerSelf();
		centerSelf();
	}

	@Override
	public void regainFocus() {
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
		return this.getStage() == null;
	}

	private boolean isInputAllowed() {
		return UniversalInputStage.isInTouchableBranch(this);
	}

	/**
	 * @return The visual representation of the currently selected hexagon (keyboard and gamepad)
	 */
	public HexagonController getCurrentHexagonController() {
		return getActor().getControllerOf(currentHexagon);
	}

	private void centerSelf() {
		this.setPosition(UserInterfaceSizer.getCurrentWidth() / 2f - this.getWidth() / 2, UserInterfaceSizer.getCurrentHeight() / 2f - this.getHeight() / 2);
	}

	@Override
	public void resizeUI() {
		// TODO Auto-generated method stub
		
	}

}
