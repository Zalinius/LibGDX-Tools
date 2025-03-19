package com.darzalgames.libgdxtools.hexagon.threedee;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMap;

/**
 * Responsible for the visual representation and layout of a {@link HexagonMap}
 * @param <E> The game-specific object associated with each {@link Hexagon} and {@link HexagonController3D}
 */
public class HexagonControllerMap3D<E> {

	private final HexagonMap<Tuple<E, HexagonController3D>> hexagonMap;
		
	public HexagonControllerMap3D(HexagonMap<E> hexagonMap, Function<Hexagon, HexagonController3D> hexagonControllerFactory) {
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
	HexagonController3D getControllerOf(Hexagon hexagon) {
		return hexagonMap.getValueAt(hexagon).f;
	}


	void unfocusAll() {
		getAllControllers().forEach(HexagonController3D::clearSelected);
	}

	/**
	 * To be used to apply any visual effects to a hexagon's neighbors
	 * @param hexagon The {@link Hexagon} whose visual neighbors you're looking for
	 * @return A list of the neighboring {@link HexagonController3D} objects
	 */
	public List<HexagonController3D> getControllerNeighborsOf(Hexagon hexagon) {
		Set<Hexagon> hexes = hexagonMap.getHexagonNeighborsOf(hexagon);
		return hexes.stream().map(neighborHexagon -> hexagonMap.getValueAt(neighborHexagon).f).collect(Collectors.toList());
	}
	
	private void makeControllerForHexagon(Function<Hexagon, HexagonController3D> hexagonControllerFactory, Hexagon hexagon, E e) {
		HexagonController3D controller = hexagonControllerFactory.apply(hexagon);
		hexagonMap.put(hexagon, new Tuple<>(e, controller));
	}
	
	private Stream<HexagonController3D> getAllControllers() {
		return hexagonMap.getAllHexagons().stream().map(hex -> hexagonMap.getValueAt(hex).f);
	}

	public void resizeUI() {
		getAllControllers().forEach(HexagonController3D::resizeUI);
	}

}
