package com.darzalgames.libgdxtools.hexagon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.darzalgames.darzalcommon.data.BiMap;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMap;
import com.darzalgames.libgdxtools.maingame.GameInfo;

public class HexagonControllerMap<E> extends Group {

	private final HexagonMap<E> hexagonMap;
	private final BiMap<Hexagon, HexagonController> controllers;
	
	public HexagonControllerMap(HexagonMap<E> hexagonMap, Function<Hexagon, HexagonController> makeHexagonControllerFunction) {
		this.hexagonMap = hexagonMap;
		
		controllers = new BiMap<>();
		for (Hexagon hexagon : hexagonMap.getAllHexagons()) {
			HexagonController controller = makeHexagonControllerFunction.apply(hexagon);
			controllers.addPair(hexagon, controller);
			addActor(controller);
		}

		this.setSize(GameInfo.getWidth(), GameInfo.getHeight());
		this.setPosition(0, 0);
	}
	
	boolean containsHexagon(Hexagon hexagon) {
		return controllers.containsFirstValue(hexagon);
	}
	
	/**
	 * @param hexagon The {@link Hexagon} whose visual representation you're looking for
	 * @return
	 */
	HexagonController getControllerOf(Hexagon hexagon) {
		return controllers.getSecondValue(hexagon);
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
        this.setPosition(GameInfo.getWidth() / 2f - this.getWidth() / 2, GameInfo.getHeight() / 2f - this.getHeight() / 2);
        float diff = (GameInfo.getHeight() - this.getHeight());
        controllers.getSecondKeyset().forEach(controller -> controller.moveBy(0, -diff));
    }

}
