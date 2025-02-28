package com.darzalgames.libgdxtools.ui.input;

import java.util.HashMap;
import java.util.Map;

import com.darzalgames.darzalcommon.hexagon.HexagonDirection;

/**
 * The six directional inputs for a flat-top hexagon grid
 */
public class InputOnHexagonGrid extends Input {

	private static final Map<Input, HexagonDirection> inputToDirection = new HashMap<>();

	public static final InputOnHexagonGrid UP_RELEASED = new InputOnHexagonGrid(-1, "UP_RELEASED", HexagonDirection.TOP);
	public static final InputOnHexagonGrid DOWN_RELEASED = new InputOnHexagonGrid(-1, "DOWN_RELEASED", HexagonDirection.BOTTOM);
	public static final InputOnHexagonGrid UP_LEFT = new InputOnHexagonGrid(-1, "UP_LEFT", HexagonDirection.TOP_LEFT);
	public static final InputOnHexagonGrid UP_RIGHT = new InputOnHexagonGrid(-1, "UP_RIGHT", HexagonDirection.TOP_RIGHT);
	public static final InputOnHexagonGrid DOWN_LEFT = new InputOnHexagonGrid(-1, "DOWN_LEFT", HexagonDirection.BOTTOM_LEFT);
	public static final InputOnHexagonGrid DOWN_RIGHT = new InputOnHexagonGrid(-1, "DOWN_RIGHT", HexagonDirection.BOTTOM_RIGHT);

	private InputOnHexagonGrid(int key, String actionName, HexagonDirection direction) {
		super(key, actionName);
		inputToDirection.put(this, direction);
	}

	/**
	 * @param input The player's game input (e.g. "LEFT" from the keyboard or gamepad)
	 * @return The corresponding HexagonDirection for a flat-top grid
	 */
	public static HexagonDirection getDirectionFromInput(Input input) {
		return inputToDirection.get(input);
	}
	
	@Override
	public boolean equals(Object obj) {
		// These are needed for menu navigation!
		if ((this == InputOnHexagonGrid.UP_RELEASED && obj == Input.UP)
				|| (this == Input.UP && obj == InputOnHexagonGrid.UP_RELEASED)) {
			return true;
		}
		
		if ((this == InputOnHexagonGrid.DOWN_RELEASED && obj == Input.DOWN)
				|| (this == Input.DOWN && obj == InputOnHexagonGrid.DOWN_RELEASED)) {
			return true;
		}
		
		return super.equals(obj);
	}
}