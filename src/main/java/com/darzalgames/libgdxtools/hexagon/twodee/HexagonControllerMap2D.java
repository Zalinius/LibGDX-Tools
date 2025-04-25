package com.darzalgames.libgdxtools.hexagon.twodee;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMap;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;

/**
 * Responsible for the visual representation and layout of a {@link HexagonMap}
 * @param <E> The game-specific object associated with each {@link Hexagon} and {@link HexagonController2D}
 */
public class HexagonControllerMap2D<E> extends Group {

	private final HexagonMap<Tuple<E, HexagonController2D>> hexagonMap;

	public HexagonControllerMap2D(HexagonMap<E> hexagonMap, Function<Hexagon, HexagonController2D> hexagonControllerFactory) {
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
	HexagonController2D getControllerOf(Hexagon hexagon) {
		return hexagonMap.getValueAt(hexagon).f;
	}


	void unfocusAll() {
		getAllControllers().forEach(HexagonController2D::clearSelected);
	}

	/**
	 * To be used to apply any visual effects to a hexagon's neighbors
	 * @param hexagon The {@link Hexagon} whose visual neighbors you're looking for
	 * @return A list of the neighboring {@link HexagonController2D} objects
	 */
	public List<HexagonController2D> getControllerNeighborsOf(Hexagon hexagon) {
		Set<Hexagon> hexes = hexagonMap.getHexagonNeighborsOf(hexagon);
		return hexes.stream().map(neighborHexagon -> hexagonMap.getValueAt(neighborHexagon).f).toList();
	}

	void centerSelf() {
		float left = Integer.MAX_VALUE;
		float right = Integer.MIN_VALUE;
		float bottom = Integer.MAX_VALUE;
		float top = Integer.MIN_VALUE;

		Iterator<HexagonController2D> hexagonControllerIterator = getAllControllers().iterator();
		while (hexagonControllerIterator.hasNext()) {
			HexagonController2D controller = hexagonControllerIterator.next();
			addActor(controller);

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

		setSize(right - left, top - bottom);

		float diffX = left;
		float diffY = bottom;
		getAllControllers().forEach(controller -> controller.moveBy(-diffX, -diffY));
		UserInterfaceSizer.makeActorCentered(this);
	}


	private void makeControllerForHexagon(Function<Hexagon, HexagonController2D> hexagonControllerFactory, Hexagon hexagon, E e) {
		HexagonController2D controller = hexagonControllerFactory.apply(hexagon);
		hexagonMap.put(hexagon, new Tuple<>(e, controller));
		addActor(controller);
	}

	private Stream<HexagonController2D> getAllControllers() {
		return hexagonMap.getAllHexagons().stream().map(hex -> hexagonMap.getValueAt(hex).f);
	}

	public void resizeUI() {
		getAllControllers().forEach(HexagonController2D::resizeUI);
		centerSelf();
	}

}
