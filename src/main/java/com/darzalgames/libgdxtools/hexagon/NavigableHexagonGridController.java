package com.darzalgames.libgdxtools.hexagon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.darzalcommon.data.BiMap;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonDirection;
import com.darzalgames.darzalcommon.hexagon.HexagonMap;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.*;
import com.darzalgames.libgdxtools.ui.input.keyboard.stage.KeyboardStage;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public class NavigableHexagonGridController<E> extends Group implements InputConsumer, InputObserver {

	private final HexagonMap<E> hexagonMap;
	private final Function<Hexagon, HexagonController> makeHexagonControllerFunction;
	private final BiMap<Hexagon, HexagonController> controllers;
	private HexagonController currentHexagonController;

	public NavigableHexagonGridController(HexagonMap<E> hexagonMap, InputStrategyManager inputStrategyManager,
			Function<Hexagon, HexagonController> makeHexagonControllerFunction) {
		this.hexagonMap = hexagonMap;
		this.makeHexagonControllerFunction = makeHexagonControllerFunction;
		inputStrategyManager.register(this);

		controllers = new BiMap<>();
		this.setSize(GameInfo.getWidth(), GameInfo.getHeight());
		this.setPosition(0, 0);
	}

	/**
	 * To be used to apply any visual effects to a hexagon's neighbors
	 * @param hexagon The {@link Hexagon} whose visual neighbors you're looking for
	 * @return A list of the neighboring {@link HexagonController} objects
	 */
	List<HexagonController> getControllerNeighborsOf(Hexagon hexagon) {
		Set<Hexagon> hexes = hexagonMap.getHexagonNeighborsOf(hexagon);
		List<HexagonController> neighbors = new ArrayList<>();
		for (Hexagon neighborHexagon : hexes) {
			neighbors.add(controllers.getSecondValue(neighborHexagon));
		}
		return neighbors;
	}

	@Override
	public void consumeKeyInput(Input input) {
		if (isInputAllowed()) {
			if (input.equals(Input.ACCEPT)) {
				InputPriorityManager.claimPriority(currentHexagonController);
			} else {
				HexagonDirection direction = InputOnHexagonGrid.getDirectionFromInput(input);
				if (direction != null) {
					HexagonController navigateTo = controllers.getSecondValue(HexagonDirection.getNeighborHexagon(getCurrentHexagon(), direction));
					if (navigateTo != null) {
						currentHexagonController.setButtonFocused(false);
						currentHexagonController = navigateTo;
						currentHexagonController.setButtonFocused(true);
					}
				}
			}
		}
	}

	@Override
	public void gainFocus() {
		this.debug();
		for (Hexagon hexagon : hexagonMap.getAllHexagons()) {
			HexagonController controller = makeHexagonControllerFunction.apply(hexagon);
			controllers.addPair(hexagon, controller);
			addActor(controller);
		}
		selectDefault();
		clearSelected();
		sizeAndPositionSelfAroundCurrentBoard();
	}

	@Override
	public void focusCurrent() {
		if (isInputAllowed()) {
			currentHexagonController.setButtonFocused(true);
		}
	}

	@Override
	public void clearSelected() {
		currentHexagonController.setButtonFocused(false);
	}

	@Override
	public void selectDefault() {
		currentHexagonController = controllers.getSecondValue(new Hexagon(0, 0)); // TODO this used to be getMiddleHexagon(), but now we only return the middle hexagon value (not its coordinates)
		focusCurrent();
	}

	@Override
	public void inputStrategyChanged(InputStrategyManager inputStrategyManager) {
		if (inputStrategyManager.showMouseExclusiveUI()) {
			controllers.getSecondKeyset().forEach(hexagon -> hexagon.setTouchable(Touchable.enabled));
		} else if (isInputAllowed()) {
			currentHexagonController.setButtonFocused(true);	
		}
	}


	@Override
	public boolean shouldBeUnregistered() {
		return this.getStage() == null;
	}

	protected boolean isInputAllowed() {
		return KeyboardStage.isInTouchableBranch(this);
	}

	private void sizeAndPositionSelfAroundCurrentBoard() {
		float left = Integer.MAX_VALUE;
		float right = Integer.MIN_VALUE;
		float bottom = Integer.MAX_VALUE;
		float top = Integer.MIN_VALUE;
		for (HexagonController controller : controllers.getSecondKeyset()) {
			this.addActor(controller);

			if (controller.getX() < left) {
				left = controller.getX();
			}
			if (controller.getRight() > right) {
				right = controller.getRight();
			}
			if (controller.getY() < bottom) {
				bottom = controller.getY();
			}
			if (controller.getTop() > top) {
				top = controller.getTop();
			}
		}

		this.setSize(right - left, top - bottom);
		this.setPosition(GameInfo.getWidth() / 2f - this.getWidth() / 2, GameInfo.getHeight() / 2f - this.getHeight() / 2);
		float diff = (GameInfo.getHeight() - this.getHeight());
		controllers.getSecondKeyset().forEach(controller -> controller.moveBy(0, -diff));
	}
	
	private Hexagon getCurrentHexagon() {
		return controllers.getFirstValue(currentHexagonController);
	}
	

}
