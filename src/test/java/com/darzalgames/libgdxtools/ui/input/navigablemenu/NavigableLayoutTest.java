package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.darzalcommon.functional.Consumers;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UserInterfaceFactory;

class NavigableLayoutTest {

	@BeforeAll
	public static void setup() {
		TestWithTable.setUpBeforeAll();
	}

	@Test
	void hasFinalButton_withoutFinalButton_returnsFalse() {
		NavigableLayout layout = makeTestLayout();

		assertFalse(layout.hasFinalButton());
	}

	@Test
	void hasFinalButton_withFinalButton_returnsTrue() {
		NavigableLayout layout = makeTestLayout();
		TestButton finalButton = makeTestButton();
		layout.setFinalButton(finalButton);

		assertTrue(layout.hasFinalButton());
	}

	@Test
	void setFinalButton_toValidButton_setsIt() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(NavigableLayoutTest.makeTestButton());
		entries.add(NavigableLayoutTest.makeTestButton());
		NavigableLayout layout = makeTestLayout();
		TestButton finalButton = makeTestButton();

		layout.setFinalButton(finalButton);

		assertEquals(finalButton, layout.finalButton);
		assertTrue(layout.filterInteractableEntries().contains(finalButton));
	}

	@Test
	void setFinalButton_toBlankButton_doesNotSetIt() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(NavigableLayoutTest.makeTestButton());
		entries.add(NavigableLayoutTest.makeTestButton());
		NavigableLayout layout = makeTestLayout();
		TestButton finalButton = NavigableLayoutTest.makeTestSpacer();

		layout.setFinalButton(finalButton);

		assertEquals(null, layout.finalButton);
		assertFalse(layout.filterInteractableEntries().contains(finalButton));
	}

	@Test
	void clearSelected_leavesNoButtonFocused() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableLayout layout = makeTestLayout();
		layout.selectDefault();

		layout.focusCurrent();
		layout.clearSelected();

		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertEquals(null, layout.getCurrentButton());
	}

	@Test
	void isBlank_whenTotallyEmpty_returnsTrue() {
		NavigableLayout layout = makeTestLayout();

		assertTrue(layout.isBlank());
	}

	@Test
	void isBlank_withSpacers_returnsTrue() {
		NavigableLayout layout = makeTestLayout();
		layout.replaceContents(List.of(makeTestSpacer(), makeTestSpacer()));

		assertTrue(layout.isBlank());
	}

	@Test
	void isBlank_withOnlyFinalButton_returnsFalse() {
		NavigableLayout layout = makeTestLayout();
		layout.setFinalButton(makeTestButton());

		assertFalse(layout.isBlank());
	}

	@Test
	void isDisabled_withOnlyEnabledFinalButton_returnsFalse() {
		NavigableLayout layout = makeTestLayout();
		layout.setFinalButton(makeTestButton());

		assertFalse(layout.isDisabled());
	}

	@Test
	void isDisabled_withOnlyDisabledFinalButton_returnsTrue() {
		NavigableLayout layout = makeTestLayout();
		layout.setFinalButton(makeTestButton());
		layout.finalButton.setDisabled(true);

		assertTrue(layout.isDisabled());
	}

	@Test
	void isDisabled_withOnlyAnEnabledEntry_returnsFalse() {
		NavigableLayout layout = makeTestLayout();
		layout.replaceContents(List.of(makeTestButton()));

		assertFalse(layout.isDisabled());
	}

	@Test
	void isDisabled_withOnlyDisabledEntry_returnsTrue() {
		NavigableLayout layout = makeTestLayout();
		TestButton testButton = makeTestButton();
		layout.replaceContents(List.of(testButton));
		testButton.setDisabled(true);

		assertTrue(layout.isDisabled());
	}

	@Test
	void setDisabled_setsAllEntriesAndFinalButtonDisabled() {
		NavigableLayout layout = makeTestLayout();
		TestButton testButton = makeTestButton();
		TestButton finalButton = makeTestButton();
		layout.replaceContents(List.of(testButton), finalButton);

		layout.setDisabled(true);

		assertTrue(layout.isDisabled());
		assertTrue(testButton.isDisabled());
		assertTrue(finalButton.isDisabled());
		assertTrue(layout.filterInteractableEntries().isEmpty());
	}

	@Test
	void resizeUI_withTestButtons_doesNotThrow() {
		NavigableLayout layout = makeTestLayout();
		layout.replaceContents(List.of(makeTestButton()), makeTestButton());

		assertDoesNotThrow(layout::resizeUI);
	}

	@Test
	void populateButtons_clearsAllEntryCells() {
		NavigableLayout layout = makeTestLayout();
		layout.allEntryCells.put(makeTestButton(), new Cell<>());

		layout.populateButtons();

		assertTrue(layout.allEntryCells.isEmpty());
	}

	@Test
	void setAlignment_forBothAtOnce_setsBoth() {
		NavigableLayout layout = makeTestLayout();
		layout.setAlignment(Alignment.BOTTOM_RIGHT);

		assertEquals(Alignment.BOTTOM_RIGHT, layout.entryAlignment);
		assertEquals(Alignment.BOTTOM_RIGHT, layout.tableAlignment);
	}

	@Test
	void setAlignment_individually_setsBothToProvidedValues() {
		NavigableLayout layout = makeTestLayout();
		layout.setAlignment(Alignment.BOTTOM_RIGHT, Alignment.CENTER);

		assertEquals(Alignment.BOTTOM_RIGHT, layout.entryAlignment);
		assertEquals(Alignment.CENTER, layout.tableAlignment);
	}

	public static TestButton makeTestSpacer() {
		TestButton spacer = new TestButton();
		spacer.setDisabled(true);
		spacer.getView().setName(UserInterfaceFactory.SPACER_NAME);
		spacer.setBlank(true);
		assertTrue(VisibleInputConsumer.isSpacer(spacer));
		return spacer;
	}

	public static TestButton makeTestButton() {
		return new TestButton();
	}

	private static NavigableLayout makeTestLayout() {
		return new NavigableLayout() {
			List<VisibleInputConsumer> entries = new ArrayList<>();

			@Override
			public void consumeKeyInput(Input input) {}

			@Override
			public boolean goTo(VisibleInputConsumer visibleInputConsumer) {
				return false;
			}

			@Override
			protected void setUpTable() {}

			@Override
			protected boolean returnToFirst() {
				return false;
			}

			@Override
			protected Collection<VisibleInputConsumer> getAllEntries() {
				return entries;
			}

			@Override
			protected void findCurrentButton() {}

			@Override
			protected Consumer<Cell<Actor>> getSpacingPolicy() {
				return Consumers.nullConsumer();
			}

			@Override
			protected void populateInnerTableWithButtons(Table innerTable) {}

			@Override
			protected void clearIndices() {}
		};
	}
}
