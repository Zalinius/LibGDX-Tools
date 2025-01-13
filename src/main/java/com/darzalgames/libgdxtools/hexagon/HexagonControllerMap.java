package com.darzalgames.libgdxtools.hexagon;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.darzalgames.darzalcommon.data.BiMap;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMap;

/**
 * Responsible for the visual representation and layout of a {@link HexagonMap}
 * @author DarZal
 * @param <E>
 */
public class HexagonControllerMap<E> extends Group {

	private final HexagonMap<E> hexagonMap;
	private final BiMap<Hexagon, HexagonController> controllers;
	private final Function<Hexagon, HexagonController> makeHexagonControllerFunction;

	public HexagonControllerMap(HexagonMap<E> hexagonMap, Function<Hexagon, HexagonController> makeHexagonControllerFunction) {
		this.hexagonMap = hexagonMap;
		this.makeHexagonControllerFunction = makeHexagonControllerFunction;

		controllers = new BiMap<>();
		hexagonMap.getAllHexagons().forEach(hexagon -> makeControllerForHexagon(hexagon));
	}

	/**
	 * @param hexagon
	 * @return Whether or not this map has a visual representation for the given hexagon
	 */
	boolean containsHexagon(Hexagon hexagon) {
		return controllers.containsFirstValue(hexagon);
	}

	/**
	 * @param hexagon The {@link Hexagon} whose visual representation you're looking for. Call {@link #containsHexagon} first to check it exists.
	 * @return The given controller, or null if the hexagon is not present on this map
	 */
	HexagonController getControllerOf(Hexagon hexagon) {
		return controllers.getSecondValue(hexagon);
	}

	/**
	 * To be used to apply any visual effects to a hexagon's neighbors
	 * @param hexagon The {@link Hexagon} whose visual neighbors you're looking for
	 * @return A list of the neighboring {@link HexagonController} objects
	 */
	public List<HexagonController> getControllerNeighborsOf(Hexagon hexagon) {
		Set<Hexagon> hexes = hexagonMap.getHexagonNeighborsOf(hexagon);
		return hexes.stream().map(neighborHexagon -> controllers.getSecondValue(neighborHexagon)).toList();
	}

	void centerSelf() {
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
		
		float diffX = left; 
		float diffY = bottom; 
		controllers.getSecondKeyset().forEach(controller -> controller.moveBy(-diffX, -diffY));
	}

	
	private void makeControllerForHexagon(Hexagon hexagon) {
		HexagonController controller = makeHexagonControllerFunction.apply(hexagon);
		controllers.addPair(hexagon, controller);
		addActor(controller);
	}

}
