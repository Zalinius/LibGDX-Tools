package com.darzalgames.libgdxtools.hexagon.twodee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.darzalgames.darzalcommon.hexagon.HexagonDirection;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputOnHexagonGrid;

class InputOnHexagonGridTest {

	private static Stream<Arguments> inputToDirection() {
		return Stream.of(
				Arguments.of(InputOnHexagonGrid.UP_RELEASED, HexagonDirection.TOP),
				Arguments.of(Input.UP, HexagonDirection.TOP),
				Arguments.of(InputOnHexagonGrid.UP_LEFT, HexagonDirection.TOP_LEFT),
				Arguments.of(InputOnHexagonGrid.UP_RIGHT, HexagonDirection.TOP_RIGHT),
				Arguments.of(InputOnHexagonGrid.DOWN_RELEASED, HexagonDirection.BOTTOM),
				Arguments.of(Input.DOWN, HexagonDirection.BOTTOM),
				Arguments.of(InputOnHexagonGrid.DOWN_LEFT, HexagonDirection.BOTTOM_LEFT),
				Arguments.of(InputOnHexagonGrid.DOWN_RIGHT, HexagonDirection.BOTTOM_RIGHT)
		);
	}

	@ParameterizedTest
	@MethodSource("inputToDirection")
	void getDirectionFromInput_givenInput_returnsCorrespondingDirection(Input input, HexagonDirection expectedDirection) {
		HexagonDirection direction = InputOnHexagonGrid.getDirectionFromInput(input);

		assertEquals(expectedDirection, direction);
	}

	@Test
	void hashCode_onUpAndUpReleased_isEqual() {
		assertEquals(Input.UP.hashCode(), InputOnHexagonGrid.UP_RELEASED.hashCode());
	}

	@Test
	void hashCode_onDownAndDownReleased_isEqual() {
		assertEquals(Input.DOWN.hashCode(), InputOnHexagonGrid.DOWN_RELEASED.hashCode());
	}

	@Test
	void hashCode_onUpAndDownReleased_isNotEqual() {
		assertNotEquals(Input.UP.hashCode(), InputOnHexagonGrid.DOWN_RELEASED.hashCode());
	}

}