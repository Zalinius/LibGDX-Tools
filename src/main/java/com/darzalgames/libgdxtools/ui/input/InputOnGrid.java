package com.darzalgames.libgdxtools.ui.input;

/**
 * The eight directional inputs for a grid
 */
public class InputOnGrid extends Input {

	public static final InputOnGrid UP_RELEASED = new InputOnGrid(-1, "UP_RELEASED");
	public static final InputOnGrid DOWN_RELEASED = new InputOnGrid(-1, "DOWN_RELEASED");
	public static final InputOnGrid LEFT_RELEASED = new InputOnGrid(-1, "LEFT_RELEASED");
	public static final InputOnGrid RIGHT_RELEASED = new InputOnGrid(-1, "RIGHT_RELEASED");
	public static final InputOnGrid UP_LEFT = new InputOnGrid(-1, "UP_LEFT");
	public static final InputOnGrid UP_RIGHT = new InputOnGrid(-1, "UP_RIGHT");
	public static final InputOnGrid DOWN_LEFT = new InputOnGrid(-1, "DOWN_LEFT");
	public static final InputOnGrid DOWN_RIGHT = new InputOnGrid(-1, "DOWN_RIGHT");

	private InputOnGrid(int key, String actionName) {
		super(key, actionName);
	}

	@Override
	public int hashCode() {
		if (this == InputOnGrid.UP_RELEASED) {
			return Input.UP.hashCode();
		} else if (this == InputOnGrid.DOWN_RELEASED) {
			return Input.DOWN.hashCode();
		} else if (this == InputOnGrid.LEFT_RELEASED) {
			return Input.LEFT.hashCode();
		} else if (this == InputOnGrid.RIGHT_RELEASED) {
			return Input.RIGHT.hashCode();
		}
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// These are needed for menu navigation!
		if ((this == InputOnGrid.UP_RELEASED && obj == Input.UP)
				|| (this == Input.UP && obj == InputOnGrid.UP_RELEASED)) {
			return true;
		}

		if ((this == InputOnGrid.DOWN_RELEASED && obj == Input.DOWN)
				|| (this == Input.DOWN && obj == InputOnGrid.DOWN_RELEASED)) {
			return true;
		}

		if ((this == InputOnGrid.LEFT_RELEASED && obj == Input.LEFT)
				|| (this == Input.LEFT && obj == InputOnGrid.LEFT_RELEASED)) {
			return true;
		}
		if ((this == InputOnGrid.RIGHT_RELEASED && obj == Input.RIGHT)
				|| (this == Input.RIGHT && obj == InputOnGrid.RIGHT_RELEASED)) {
			return true;
		}

		return super.equals(obj);
	}
}