package com.darzalgames.libgdxtools.hexagon.twodee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMap;
import com.darzalgames.darzalcommon.hexagon.gridfactory.HexagonGridRectangular;
import com.darzalgames.libgdxtools.edition.GameEdition;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.maingame.SharesGameInformation;
import com.darzalgames.libgdxtools.os.GameOperatingSystem;
import com.darzalgames.libgdxtools.preferences.PreferenceManager;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.steam.agnostic.PlatformStrategy;
import com.darzalgames.libgdxtools.ui.input.InputOnHexagonGrid;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UserInterfaceFactory;

class NavigableHexagonControllerMapTest {

	private NavigableHexagonMap2D<String> navigableHexagonMap;
	private HexagonControllerMap2D<String> hexagonControllerMap;

	@BeforeEach
	void setup() {
		GameInfo.setMainGame(new SharesGameInformation() {
			@Override
			public SaveManager getSaveManager() {
				return null;
			}

			@Override
			public PreferenceManager getPreferenceManager() {
				return null;
			}

			@Override
			public GameOperatingSystem getOperatingSystem() {
				return null;
			}

			@Override
			public PlatformStrategy getPlatformStrategy() {
				return null;
			}

			@Override
			public String getGameName() {
				return null;
			}

			@Override
			public String getGameVersion() {
				return null;
			}

			@Override
			public UserInterfaceFactory getUserInterfaceFactory() {
				return null;
			}

			@Override
			public GameEdition getGameEdition() {
				return null;
			}
		});

		HexagonMap<String> hexagonMap = new HexagonMap<>();
		HexagonGridRectangular.makeGrid(3, 3).forEach(hex -> hexagonMap.put(hex, hex.toString()));
		hexagonControllerMap = new HexagonControllerMap2D<>(hexagonMap, hex -> new HexagonController2D(hex, null, hexagonController -> new BlankHexagonController()));
		navigableHexagonMap = new NavigableHexagonMap2D<>(hexagonControllerMap);
	}

	@Test
	void focusCurrent_withDefault_focusesHexagon0x0() {

		navigableHexagonMap.gainFocus();
		navigableHexagonMap.focusCurrent();

		assertEquals(0, navigableHexagonMap.getCurrentHexagonController().hexagon.q());
		assertEquals(0, navigableHexagonMap.getCurrentHexagonController().hexagon.r());
	}

	@ParameterizedTest
	@MethodSource("directionToCoordinates")
	void consumeInput_withAllDirectionInputs_focusesCorrectHexagon(InputOnHexagonGrid input, int q, int r) {

		navigableHexagonMap.gainFocus();
		navigableHexagonMap.consumeKeyInput(input);

		assertEquals(q, navigableHexagonMap.getCurrentHexagonController().hexagon.q());
		assertEquals(r, navigableHexagonMap.getCurrentHexagonController().hexagon.r());
	}

	@Test
	void getControllerNeighbors_of0x0_returnsCorrectHexagons() {
		List<Hexagon> expectedNeighbors = List.of(new Hexagon(-1, 0), new Hexagon(-1, 1), new Hexagon(0, -1), new Hexagon(0, 1), new Hexagon(1, -1), new Hexagon(1, 0));

		List<HexagonController2D> neighborControllers = hexagonControllerMap.getControllerNeighborsOf(new Hexagon(0, 0));
		List<Hexagon> neighborHexagons = neighborControllers.stream().map(controller -> controller.hexagon).toList();

		assertEquals(expectedNeighbors.size(), neighborHexagons.size());
		assertTrue(neighborHexagons.containsAll(expectedNeighbors));
	}

	private static Stream<Arguments> directionToCoordinates() {
		return Stream.of(
				Arguments.of(InputOnHexagonGrid.UP_RELEASED, 0, -1),
				Arguments.of(InputOnHexagonGrid.UP_LEFT, -1, 0),
				Arguments.of(InputOnHexagonGrid.UP_RIGHT, 1, -1),
				Arguments.of(InputOnHexagonGrid.DOWN_RELEASED, 0, 1),
				Arguments.of(InputOnHexagonGrid.DOWN_LEFT, -1, 1),
				Arguments.of(InputOnHexagonGrid.DOWN_RIGHT, 1, 0)
		);
	}
}