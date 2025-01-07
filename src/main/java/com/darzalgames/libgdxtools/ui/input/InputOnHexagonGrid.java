package com.darzalgames.libgdxtools.ui.input;

import com.darzalgames.darzalcommon.hexagon.HexagonDirection;

/**
 * The six directional inputs for a flat-top hexagon grid
 * @author DarZal
 */
public class InputOnHexagonGrid extends Input {
	
	public static final InputOnHexagonGrid UP_RELEASED = new InputOnHexagonGrid(-1, "UP_RELEASED");
	public static final InputOnHexagonGrid DOWN_RELEASED = new InputOnHexagonGrid(-1, "DOWN_RELEASED");
	public static final InputOnHexagonGrid UP_LEFT = new InputOnHexagonGrid(-1, "UP_LEFT");
	public static final InputOnHexagonGrid UP_RIGHT = new InputOnHexagonGrid(-1, "UP_RIGHT");
	public static final InputOnHexagonGrid DOWN_LEFT = new InputOnHexagonGrid(-1, "DOWN_LEFT");
	public static final InputOnHexagonGrid DOWN_RIGHT = new InputOnHexagonGrid(-1, "DOWN_RIGHT");

	private InputOnHexagonGrid(int key, String actionName) {
		super(key, actionName);
	}


	/**
	 * @param input The player's game input (e.g. "LEFT" from the keyboard or gamepad)
	 * @return The corresponding HexagonDirection for a flat-top grid
	 */
	public static HexagonDirection getDirectionFromInput(Input input) {
		if (input.equals(UP_RELEASED)) {
			return HexagonDirection.TOP;
		} else if (input.equals(UP_LEFT)) {
			return HexagonDirection.TOP_LEFT;
		} else if (input.equals(UP_RIGHT)) {
			return HexagonDirection.TOP_RIGHT;
		} else if (input.equals(DOWN_RELEASED)) {
			return HexagonDirection.BOTTOM;
		} else if (input.equals(DOWN_LEFT)) {
			return HexagonDirection.BOTTOM_LEFT;
		} else if (input.equals(DOWN_RIGHT)) {
			return HexagonDirection.BOTTOM_RIGHT;
		} else {
			return null;
		} 
	}
	
	@Override
	public boolean equals(Object obj) {
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
