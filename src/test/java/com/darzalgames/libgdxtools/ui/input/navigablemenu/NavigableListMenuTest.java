package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

class NavigableListMenuTest {

	@BeforeAll
	public static void setup() {
		TestWithTable.setUpBeforeAll();
	}

	private static NavigableListMenu makeTestMenu(MenuOrientation menuOrientation, final List<VisibleInputConsumer> entries) {
		return new NavigableListMenu(menuOrientation, entries) {
			@Override
			protected void setUpTable() {}
		};
	}

	@Test
	void constructor_withoutSuppliedEntries_hasNoEntries() {
		NavigableListMenu navigableListMenu = new NavigableListMenu(MenuOrientation.VERTICAL) {
			@Override
			protected void setUpTable() {}
		};

		assertTrue(navigableListMenu.allEntries.isEmpty());
		assertTrue(navigableListMenu.getAllEntries().isEmpty());
		assertTrue(navigableListMenu.filterInteractableEntries().isEmpty());
	}

	@Test
	void getView_onConstruction_exists() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		assertNotNull(navigableListMenu.getView());
	}

	@Test
	void setTouchable_appliesToMainTableAndAllEntries() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		navigableListMenu.setTouchable(Touchable.disabled);

		assertEquals(Touchable.disabled, navigableListMenu.getView().getTouchable());
		assertEquals(Touchable.disabled, buttonOne.getView().getTouchable());
		assertEquals(Touchable.disabled, buttonTwo.getView().getTouchable());
	}

	@Test
	void selectDefault_onlyFirstButtonIsFocused() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		navigableListMenu.selectDefault();

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertEquals(buttonOne, navigableListMenu.getCurrentButton());
	}

	@Test
	void constructor_withDefaultInteractabilityFilterOnSpacerAndDisabledButtons_filtersThem() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		buttonOne.setDisabled(true);
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer testSpacer = NavigableLayoutTest.makeTestSpacer();
		entries.add(testSpacer);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		List<VisibleInputConsumer> interactableEntries = navigableListMenu.filterInteractableEntries();

		assertEquals(1, interactableEntries.size());
		assertFalse(interactableEntries.contains(buttonOne));
		assertTrue(interactableEntries.contains(buttonTwo));
		assertFalse(interactableEntries.contains(testSpacer));
	}

	@Test
	void setInteractabilityFilter_refiltersInteractableEntriesAsExpected() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		buttonOne.setDisabled(true);
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer testSpacer = NavigableLayoutTest.makeTestSpacer();
		entries.add(testSpacer);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		navigableListMenu.setInteractabilityFilter(entry -> !entry.isBlank()); // test allowing disabled buttons, for example if they have tooltips
		List<VisibleInputConsumer> interactableEntries = navigableListMenu.filterInteractableEntries();

		assertEquals(2, interactableEntries.size());
		assertTrue(interactableEntries.contains(buttonOne));
		assertTrue(interactableEntries.contains(buttonTwo));
		assertFalse(interactableEntries.contains(testSpacer));
	}

	private static Stream<Arguments> canUseInputSource() {
		return Stream.of(
				Arguments.of(Input.UP, false),
				Arguments.of(Input.LEFT, false),
				Arguments.of(Input.RIGHT, false),
				Arguments.of(Input.DOWN, true),
				Arguments.of(Input.ACCEPT, true),
				Arguments.of(Input.BACK, true)
		);
	}

	@ParameterizedTest
	@MethodSource("canUseInputSource")
	void canUseInput_severalInputs_returnsExpectedBooleans(Input input, boolean expected) {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.setFinalButton(NavigableLayoutTest.makeTestButton());
		navigableListMenu.setMenuLoops(false);

		navigableListMenu.selectDefault();

		assertEquals(expected, navigableListMenu.canUseInput(input));
	}

	@Test
	void canUseInput_backwardWhenNotOnFirstEntry_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);
		navigableListMenu.setFinalButton(NavigableLayoutTest.makeTestButton());

		navigableListMenu.goTo(buttonTwo);

		assertTrue(navigableListMenu.canUseInput(Input.LEFT));
	}

	@Test
	void canUseInput_loopingBackwards_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.setFinalButton(NavigableLayoutTest.makeTestButton());

		navigableListMenu.selectDefault();

		assertTrue(navigableListMenu.canUseInput(Input.UP));
	}

	@Test
	void canUseInput_loopingForwards_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);
		navigableListMenu.goTo(buttonTwo);

		assertTrue(navigableListMenu.canUseInput(Input.RIGHT));
	}

	@Test
	void canUseInput_acceptWithoutFocusedButton_returnsFalse() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);
		navigableListMenu.clearSelected();

		assertFalse(navigableListMenu.canUseInput(Input.ACCEPT));
	}

	@Test
	void canUseInput_backWithoutFinalButton_returnsFalse() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);

		assertFalse(navigableListMenu.canUseInput(Input.BACK));
	}

	@Test
	void consumeKeyInput_downFromTop_focusesSecond() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
		assertEquals(buttonTwo, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_downFromTopThenUpAgain_focusesFirst() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);
		navigableListMenu.consumeKeyInput(Input.UP);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertEquals(buttonOne, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_downFromTopTwice_loopsBackToFocusFirst() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);
		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertEquals(buttonOne, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_downFromTopTwiceOnNonLoopingMenu_keepsFocusOnLastButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.setMenuLoops(false);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);
		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
		assertEquals(buttonTwo, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_horizontalListGoRight_focusesSecond() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.RIGHT);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
		assertEquals(buttonTwo, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_horizontalListGoDown_doesntMoveFocus() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertEquals(buttonOne, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_downTwiceFromTop_focusesThird() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = NavigableLayoutTest.makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);
		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertTrue(buttonThree.isOver());
		assertEquals(buttonThree, navigableListMenu.getCurrentButton());
	}

	@Test
	void goTo_specificButton_focusesCorrect() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = NavigableLayoutTest.makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		boolean changed = navigableListMenu.goTo(buttonTwo);

		assertTrue(changed);
		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
		assertEquals(buttonTwo, navigableListMenu.getCurrentButton());
	}

	@Test
	void goTo_currentButton_maintainsFocusAndReturnsFalse() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = NavigableLayoutTest.makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		boolean changed = navigableListMenu.goTo(buttonOne);

		assertFalse(changed);
		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
		assertEquals(buttonOne, navigableListMenu.getCurrentButton());
	}

	@Test
	void goTo_invalidButton_doesntMoveFocus() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = NavigableLayoutTest.makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		boolean changed = navigableListMenu.goTo(NavigableLayoutTest.makeTestButton());

		assertFalse(changed);
		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
		assertEquals(buttonOne, navigableListMenu.getCurrentButton());
	}

	@Test
	void returnToFirst_afterDownTwiceFromTop_focusesFirst() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = NavigableLayoutTest.makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);
		navigableListMenu.consumeKeyInput(Input.DOWN);
		boolean changed = navigableListMenu.returnToFirst();

		assertTrue(changed);
		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
		assertEquals(buttonOne, navigableListMenu.getCurrentButton());
	}

	@Test
	void returnToLast_withNoInput_focusesLast() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = NavigableLayoutTest.makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		boolean changed = navigableListMenu.returnToLast();

		assertTrue(changed);
		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertTrue(buttonThree.isOver());
		assertEquals(buttonThree, navigableListMenu.getCurrentButton());
	}

	@Test
	void returnToLast_withFinalButton_focusesFinalButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = NavigableLayoutTest.makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();
		TestButton finalButton = NavigableLayoutTest.makeTestButton();
		navigableListMenu.setFinalButton(finalButton);

		boolean changed = navigableListMenu.returnToLast();

		assertTrue(changed);
		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
		assertTrue(finalButton.isOver());
		assertEquals(finalButton, navigableListMenu.getCurrentButton());
	}

	@Test
	void returnToSecondLast_withEnoughEntries_focusesSecondLast() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = NavigableLayoutTest.makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();
		TestButton finalButton = new TestButton();
		finalButton.setBlank(false);
		navigableListMenu.setFinalButton(finalButton);

		boolean changed = navigableListMenu.returnToSecondLast();

		assertTrue(changed);
		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertTrue(buttonThree.isOver());
		assertFalse(finalButton.isOver());
		assertEquals(buttonThree, navigableListMenu.getCurrentButton());
	}

	@Test
	void returnToSecondLast_withOneEntry_focusesOnlyEntry() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		boolean changed = navigableListMenu.returnToSecondLast();

		assertFalse(changed);
		assertTrue(buttonOne.isOver());
		assertEquals(buttonOne, navigableListMenu.getCurrentButton());
	}

	@Test
	void returnToSecondLast_withNoEntries_doesntCrash() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		assertDoesNotThrow(navigableListMenu::returnToSecondLast);
		assertEquals(null, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_upFromTop_loopsAround() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		TestButton buttonThree = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		entries.add(buttonTwo);
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.UP);

		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertTrue(buttonThree.isOver());
		assertEquals(buttonThree, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_upFromTopOnNonLoopingMenu_doesntMove() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();
		navigableListMenu.setMenuLoops(false);

		navigableListMenu.consumeKeyInput(Input.UP);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertEquals(buttonOne, navigableListMenu.getCurrentButton());
	}

	@Test
	void replaceContents_endsWithCorrectNumberOfEntries() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(NavigableLayoutTest.makeTestButton());
		List<VisibleInputConsumer> replacementEntries = new ArrayList<>();
		replacementEntries.add(NavigableLayoutTest.makeTestButton());
		replacementEntries.add(NavigableLayoutTest.makeTestButton());
		replacementEntries.add(NavigableLayoutTest.makeTestButton());
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		navigableListMenu.replaceContents(replacementEntries);

		assertEquals(3, navigableListMenu.allEntries.size());
	}

	@Test
	void replaceContents_withSpacer_filtersTheSpacerInInteractableEntities() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		entries.add(NavigableLayoutTest.makeTestSpacer());
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		assertEquals(3, navigableListMenu.allEntries.size());
		assertEquals(2, navigableListMenu.filterInteractableEntries().size());
	}

	@Test
	void consumeInput_downWithSpacer_skipsSpacer() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		entries.add(NavigableLayoutTest.makeTestSpacer());
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
		assertEquals(buttonTwo, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeInput_downTwicePastSpacer_wrapsAround() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		entries.add(NavigableLayoutTest.makeTestSpacer());
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);
		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertEquals(buttonOne, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeInput_downWithDisabledButton_skipsDisabledButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		buttonTwo.setDisabled(true);
		entries.add(buttonTwo);
		TestButton buttonThree = NavigableLayoutTest.makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertTrue(buttonThree.isOver());
		assertEquals(buttonThree, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_back_pressesFinalButton() {
		AtomicBoolean finalButtonPressed = new AtomicBoolean();
		finalButtonPressed.set(false);
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(NavigableLayoutTest.makeTestButton());
		entries.add(NavigableLayoutTest.makeTestButton());
		TestButton finalButton = new TestButton(() -> finalButtonPressed.set(true));
		finalButton.setBlank(false);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.setFinalButton(finalButton);

		navigableListMenu.consumeKeyInput(Input.BACK);

		assertTrue(finalButtonPressed.get());
		assertNotEquals(finalButton, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_accept_pressesCurrentButton() {
		AtomicBoolean testButtonPressed = new AtomicBoolean();
		testButtonPressed.set(false);
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer testButton = new TestButton(() -> testButtonPressed.set(true));
		entries.add(testButton);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.ACCEPT);

		assertTrue(testButtonPressed.get());
		assertEquals(testButton, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_inputWithoutCurrentButton_doesntCrash() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		assertDoesNotThrow(() -> navigableListMenu.consumeKeyInput(Input.ACCEPT));
		assertEquals(null, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_backWithoutFinalButton_pressesNothing() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = NavigableLayoutTest.makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.BACK);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
		assertEquals(buttonOne, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_changingButtonsWithPressButtonOnEntryChangedSetTrue_pressesIt() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		AtomicBoolean buttonSpy = new AtomicBoolean(false);
		TestButton testButton = new TestButton(() -> buttonSpy.set(true));
		testButton.setBlank(false);
		entries.add(testButton);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.setPressButtonOnEntryChanged(true);
		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(testButton.isOver());
		assertTrue(buttonSpy.get());
		assertEquals(testButton, navigableListMenu.getCurrentButton());
	}

	@Test
	void consumeKeyInput_changingButtonsWithPressButtonOnEntryChangedSetFalse_doesNotPressIt() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		AtomicBoolean buttonSpy = new AtomicBoolean(false);
		TestButton testButton = new TestButton(() -> buttonSpy.set(true));
		testButton.setBlank(false);
		entries.add(testButton);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.setPressButtonOnEntryChanged(false);
		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(testButton.isOver());
		assertFalse(buttonSpy.get());
		assertEquals(testButton, navigableListMenu.getCurrentButton());
	}

	@Test
	void focusCurrent_focusesCurrentButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.focusCurrent();

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertEquals(buttonOne, navigableListMenu.getCurrentButton());
	}

	@Test
	void isBlank_withNoEntries_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		assertTrue(navigableListMenu.isBlank());
	}

	@Test
	void isBlank_withSomeEntries_returnsFalse() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		assertFalse(navigableListMenu.isBlank());
	}

	@Test
	void isBlank_withOnlyASpacer_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(NavigableLayoutTest.makeTestSpacer());
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		assertTrue(navigableListMenu.isBlank());
	}

	@Test
	void gainFocus_callsSetUpTable() {
		AtomicBoolean setUpTableSpy = new AtomicBoolean(false);
		NavigableListMenu navigableListMenu = new NavigableListMenu(MenuOrientation.VERTICAL, new ArrayList<>()) {
			@Override
			protected void setUpTable() {
				setUpTableSpy.set(true);
			}
		};

		navigableListMenu.gainFocus();

		assertTrue(setUpTableSpy.get());
	}

	@Test
	void regainFocus_onlyCallsFocusCurrentAndNotSetUpTable() {
		AtomicBoolean setUpTableSpy = new AtomicBoolean(false);
		AtomicBoolean focusCurrentSpy = new AtomicBoolean(false);
		NavigableListMenu navigableListMenu = new NavigableListMenu(MenuOrientation.VERTICAL, new ArrayList<>()) {
			@Override
			protected void setUpTable() {
				setUpTableSpy.set(true);
			}

			@Override
			public void focusCurrent() {
				focusCurrentSpy.set(true);
				super.focusCurrent();
			}
		};

		navigableListMenu.regainFocus();

		assertTrue(focusCurrentSpy.get());
		assertFalse(setUpTableSpy.get());
	}

	@Test
	void populateInnerTableWithButtons_horizontalMenuWithFinalButton_innerTableHasExactlyOneRow() {
		NavigableListMenu menu = makeTestMenu(MenuOrientation.HORIZONTAL, List.of(NavigableLayoutTest.makeTestButton(), NavigableLayoutTest.makeTestButton()));
		menu.setFinalButton(NavigableLayoutTest.makeTestButton());
		Table innerTable = new Table();

		menu.populateInnerTableWithButtons(innerTable);

		// expected row count is 0 because the row count only actually increases when you call row(), which a horizontal menu does not do
		assertEquals(0, innerTable.getRows());
		assertEquals(3, innerTable.getChildren().shrink().length);
	}

	@Test
	void populateInnerTableWithButtons_verticalMenuWithFinalButton_innerTableHasExactlyThreeRows() {
		NavigableListMenu menu = makeTestMenu(MenuOrientation.VERTICAL, List.of(NavigableLayoutTest.makeTestButton(), NavigableLayoutTest.makeTestButton()));
		menu.setFinalButton(NavigableLayoutTest.makeTestButton());
		Table innerTable = new Table();

		menu.populateInnerTableWithButtons(innerTable);

		assertEquals(2, innerTable.getRows());
		assertEquals(3, innerTable.getChildren().shrink().length);
	}

	@Test
	void populateInnerTableWithButtons_storesCellsForAllButtons() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = NavigableLayoutTest.makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = NavigableLayoutTest.makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = NavigableLayoutTest.makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu menu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		TestButton finalButton = NavigableLayoutTest.makeTestButton();
		entries.add(finalButton);
		menu.setFinalButton(finalButton);
		Table innerTable = new Table();

		menu.populateInnerTableWithButtons(innerTable);

		assertEquals(innerTable.getCell(buttonOne.getView()), menu.allEntryCells.get(buttonOne));
		assertEquals(innerTable.getCell(buttonTwo.getView()), menu.allEntryCells.get(buttonTwo));
		assertEquals(innerTable.getCell(buttonThree.getView()), menu.allEntryCells.get(buttonThree));
		assertEquals(innerTable.getCell(finalButton.getView()), menu.allEntryCells.get(finalButton));
	}

	@Test
	void populateInnerTableWithButtons_horizontalMenuWithSpacer_appliesExpandXButNotY() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton spacer = NavigableLayoutTest.makeTestSpacer();
		entries.add(spacer);
		NavigableListMenu menu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);
		Table innerTable = new Table();

		menu.populateInnerTableWithButtons(innerTable);

		assertEquals(1, innerTable.getCell(spacer.getView()).getExpandX());
		assertEquals(0, innerTable.getCell(spacer.getView()).getExpandY());
	}

	@Test
	void populateInnerTableWithButtons_verticalMenuWithSpacer_appliesExpandYButNotX() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton spacer = NavigableLayoutTest.makeTestSpacer();
		entries.add(spacer);
		NavigableListMenu menu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		Table innerTable = new Table();

		menu.populateInnerTableWithButtons(innerTable);

		assertEquals(0, innerTable.getCell(spacer.getView()).getExpandX());
		assertEquals(1, innerTable.getCell(spacer.getView()).getExpandY());
	}

}
