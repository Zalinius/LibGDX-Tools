package com.darzalgames.libgdxtools.hexagon;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMap;

/**
 * Responsible for the visual representation and layout of a {@link HexagonMap}
 * @param <E> The game-specific object associated with each {@link Hexagon} and {@link HexagonController}
 */
public class HexagonControllerMap<E> extends Group {

	private final HexagonMap<Tuple<E, HexagonController>> hexagonMap;

	public HexagonControllerMap(HexagonMap<E> hexagonMap, Function<Hexagon, HexagonController> hexagonControllerFactory) {
		this.hexagonMap = new HexagonMap<>();

		hexagonMap.getAllHexagons().forEach(hexagon -> makeControllerForHexagon(hexagonControllerFactory, hexagon, hexagonMap.getValueAt(hexagon)));
	}

	/**
	 * @param hexagon
	 * @return Whether or not this map has a visual representation for the given hexagon
	 */
	boolean containsHexagon(Hexagon hexagon) {
		return hexagonMap.getAllHexagons().contains(hexagon);
	}

	/**
	 * @param hexagon The {@link Hexagon} whose visual representation you're looking for. Call {@link #containsHexagon} first to check it exists.
	 * @return The given controller, or null if the hexagon is not present on this map
	 */
	HexagonController getControllerOf(Hexagon hexagon) {
		return hexagonMap.getValueAt(hexagon).f;
	}


	void unfocusAll() {
		getAllControllers().forEach(HexagonController::clearSelected);
	}

	/**
	 * To be used to apply any visual effects to a hexagon's neighbors
	 * @param hexagon The {@link Hexagon} whose visual neighbors you're looking for
	 * @return A list of the neighboring {@link HexagonController} objects
	 */
	public List<HexagonController> getControllerNeighborsOf(Hexagon hexagon) {
		Set<Hexagon> hexes = hexagonMap.getHexagonNeighborsOf(hexagon);
		return hexes.stream().map(neighborHexagon -> hexagonMap.getValueAt(neighborHexagon).f).collect(Collectors.toList());
	}

	void centerSelf() {
		float left = Integer.MAX_VALUE;
		float right = Integer.MIN_VALUE;
		float bottom = Integer.MAX_VALUE;
		float top = Integer.MIN_VALUE;
		
		Iterator<HexagonController> hexagonControllerIterator = getAllControllers().iterator();
		while (hexagonControllerIterator.hasNext()) {
			HexagonController controller = hexagonControllerIterator.next();
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
		getAllControllers().forEach(controller -> controller.moveBy(-diffX, -diffY));
	}

	
	private void makeControllerForHexagon(Function<Hexagon, HexagonController> hexagonControllerFactory, Hexagon hexagon, E e) {
		HexagonController controller = hexagonControllerFactory.apply(hexagon);
		hexagonMap.put(hexagon, new Tuple<>(e, controller));
		addActor(controller);
	}
	
	private Stream<HexagonController> getAllControllers() {
		return hexagonMap.getAllHexagons().stream().map(hex -> hexagonMap.getValueAt(hex).f);
	}

}
