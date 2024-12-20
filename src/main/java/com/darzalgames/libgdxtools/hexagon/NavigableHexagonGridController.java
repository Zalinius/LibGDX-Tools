package com.darzalgames.libgdxtools.hexagon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.darzalcommon.data.BiMap;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonDirection;
import com.darzalgames.darzalcommon.hexagon.HexagonGrid;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.*;
import com.darzalgames.libgdxtools.ui.input.keyboard.stage.KeyboardStage;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public class NavigableHexagonGridController extends Group implements InputConsumer, InputObserver {

	private final HexagonGrid hexagonGrid;
	private final Function<Hexagon, HexagonController> makeHexagonControllerFunction;
	protected final BiMap<Hexagon, HexagonController> controllers;
	private HexagonController currentHexagonController;

	public NavigableHexagonGridController(HexagonGrid hexagonGrid, InputStrategyManager inputStrategyManager, Function<Hexagon, HexagonController> makeHexagonControllerFunction) {
		this.hexagonGrid = hexagonGrid;
		this.makeHexagonControllerFunction = makeHexagonControllerFunction;
		inputStrategyManager.register(this);

		controllers = new BiMap<>();
	}

	@Override
	public void consumeKeyInput(Input input) {
		if (isInputAllowed()) {
			if (input.equals(Input.ACCEPT)) {
				InputPriorityManager.claimPriority(currentHexagonController);
			} else {
				HexagonDirection direction = InputOnHexagonGrid.getDirectionFromInput(input);
				if (direction != null) {
					HexagonController navigateTo = controllers.getSecondValue(hexagonGrid.getNeighborInDirection(getCurrentHexagon(), direction));
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
		for (Hexagon hexagon : hexagonGrid.getAllHexagons()) {
			HexagonController controller = makeHexagonControllerFunction.apply(hexagon);
			controllers.addPair(hexagon, controller);
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
		currentHexagonController = controllers.getSecondValue(hexagonGrid.getMiddleHexagon());
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

	private Hexagon getCurrentHexagon() {
		return controllers.getFirstValue(currentHexagonController);
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

	/**
	 * To be used within a {@link HexagonController} (the only thing outside of the {@link PuzzleController} who knows about their {@link Hexagon} object)
	 * to apply any visual effects to the neighbors
	 * @param hexagon The {@link Hexagon} whose visual neighbors you're looking for
	 * @return A list of the neighboring {@link HexagonController} objects
	 */
	public List<HexagonController> getControllerNeighborsOf(Hexagon hexagon) {
		List<Hexagon> hexes = hexagonGrid.getNeighborsOf(hexagon);
		List<HexagonController> neighbors = new ArrayList<>();
		for (Hexagon neighborHexagon : hexes) {
			neighbors.add(controllers.getSecondValue(neighborHexagon));
		}
		return neighbors;
	}
	

}
