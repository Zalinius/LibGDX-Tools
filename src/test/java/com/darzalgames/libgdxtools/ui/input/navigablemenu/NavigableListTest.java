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
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

class NavigableListTest {

	@BeforeAll
	public static void setup() {
		TestWithTable.setUpBeforeAll();
	}

	@Test
	void getView_onConstruction_exists() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);

		assertNotNull(navigableList.getView());
	}

	@Test
	void setTouchable_appliesToMainTableAndAllEntries() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);

		navigableList.setTouchable(Touchable.disabled);

		assertEquals(Touchable.disabled, navigableList.getView().getTouchable());
		assertEquals(Touchable.disabled, buttonOne.getView().getTouchable());
		assertEquals(Touchable.disabled, buttonTwo.getView().getTouchable());
	}

	@Test
	void selectDefault_onlyFirstButtonIsFocused() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);

		navigableList.selectDefault();

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void constructor_withDefaultInteractabilityFilterOnSpacerAndDisabledButtons_filtersThem() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		buttonOne.setDisabled(true);
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer testSpacer = makeTestSpacer();
		entries.add(testSpacer);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);

		List<VisibleInputConsumer> interactableEntries = navigableList.interactableEntries;

		assertEquals(1, interactableEntries.size());
		assertFalse(interactableEntries.contains(buttonOne));
		assertTrue(interactableEntries.contains(buttonTwo));
		assertFalse(interactableEntries.contains(testSpacer));
	}

	@Test
	void setInteractabilityFilter_refiltersInteractableEntriesAsExpected() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		buttonOne.setDisabled(true);
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer testSpacer = makeTestSpacer();
		entries.add(testSpacer);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);

		navigableList.setInteractabilityFilter(entry -> !entry.isBlank()); // test allowing disabled buttons, for example if they have tooltips
		List<VisibleInputConsumer> interactableEntries = navigableList.interactableEntries;

		assertEquals(2, interactableEntries.size());
		assertTrue(interactableEntries.contains(buttonOne));
		assertTrue(interactableEntries.contains(buttonTwo));
		assertFalse(interactableEntries.contains(testSpacer));
	}

	@Test
	void setFinalButton_toValidButton_setsIt() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(makeTestButton());
		entries.add(makeTestButton());
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		VisibleInputConsumer finalButton = makeTestButton();

		navigableList.setFinalButton(finalButton);

		assertTrue(navigableList.interactableEntries.contains(finalButton));
	}

	@Test
	void setFinalButton_toBlankButton_doesNotSetIt() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(makeTestButton());
		entries.add(makeTestButton());
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		VisibleInputConsumer finalButton = makeTestSpacer();

		navigableList.setFinalButton(finalButton);

		assertFalse(navigableList.interactableEntries.contains(finalButton));
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
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.setFinalButton(makeTestButton());
		navigableList.setMenuLoops(false);

		navigableList.selectDefault();

		assertEquals(expected, navigableList.canUseInput(input));
	}

	@Test
	void canUseInput_backwardWhenNotOnFirstEntry_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.HORIZONTAL, entries);
		navigableList.setFinalButton(makeTestButton());

		navigableList.goTo(buttonTwo);

		assertTrue(navigableList.canUseInput(Input.LEFT));
	}

	@Test
	void canUseInput_loopingBackwards_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.setFinalButton(makeTestButton());

		navigableList.selectDefault();

		assertTrue(navigableList.canUseInput(Input.UP));
	}

	@Test
	void canUseInput_loopingForwards_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.HORIZONTAL, entries);
		navigableList.goTo(buttonTwo);

		assertTrue(navigableList.canUseInput(Input.RIGHT));
	}

	@Test
	void canUseInput_acceptWithoutFocusedButton_returnsFalse() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		NavigableList navigableList = new NavigableList(MenuOrientation.HORIZONTAL, entries);
		navigableList.clearSelected();

		assertFalse(navigableList.canUseInput(Input.ACCEPT));
	}

	@Test
	void canUseInput_backWithoutFinalButton_returnsFalse() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		NavigableList navigableList = new NavigableList(MenuOrientation.HORIZONTAL, entries);

		assertFalse(navigableList.canUseInput(Input.BACK));
	}

	@Test
	void consumeKeyInput_downFromTop_focusesSecond() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_downFromTopThenUpAgain_focusesFirst() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.UP);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_downFromTopTwice_loopsBackToFocusFirst() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.DOWN);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_downFromTopTwiceOnNonLoopingMenu_keepsFocusOnLastButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.setMenuLoops(false);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_horizontalListGoRight_focusesSecond() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.HORIZONTAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.RIGHT);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_horizontalListGoDown_doesntMoveFocus() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.HORIZONTAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_downTwiceFromTop_focusesThird() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertTrue(buttonThree.isOver());
	}

	@Test
	void goTo_specificButton_focusesCorrect() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		boolean changed = navigableList.goTo(buttonTwo);

		assertTrue(changed);
		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
	}

	@Test
	void goTo_currentButton_maintainsFocusAndReturnsFalse() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		boolean changed = navigableList.goTo(buttonOne);

		assertFalse(changed);
		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
	}

	@Test
	void goTo_invalidButton_doesntMoveFocus() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		boolean changed = navigableList.goTo(makeTestButton());

		assertFalse(changed);
		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
	}

	@Test
	void returnToFirst_afterDownTwiceFromTop_focusesFirst() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.DOWN);
		boolean changed = navigableList.returnToFirst();

		assertTrue(changed);
		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
	}

	@Test
	void returnToLast_withNoInput_focusesLast() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		boolean changed = navigableList.returnToLast();

		assertTrue(changed);
		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertTrue(buttonThree.isOver());
	}

	@Test
	void returnToLast_withFinalButton_focusesFinalButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();
		VisibleInputConsumer finalButton = makeTestButton();
		navigableList.setFinalButton(finalButton);

		boolean changed = navigableList.returnToLast();

		assertTrue(changed);
		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
		assertTrue(finalButton.isOver());
	}

	@Test
	void returnToSecondLast_withEnoughEntries_focusesSecondLast() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();
		TestButton finalButton = new TestButton();
		finalButton.setBlank(false);
		navigableList.setFinalButton(finalButton);

		boolean changed = navigableList.returnToSecondLast();

		assertTrue(changed);
		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertTrue(buttonThree.isOver());
		assertFalse(finalButton.isOver());
	}

	@Test
	void returnToSecondLast_withOneEntry_focusesOnlyEntry() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		boolean changed = navigableList.returnToSecondLast();

		assertFalse(changed);
		assertTrue(buttonOne.isOver());
	}

	@Test
	void returnToSecondLast_withNoEntries_doesntCrash() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		assertDoesNotThrow(navigableList::returnToSecondLast);
	}

	@Test
	void consumeKeyInput_upFromTop_loopsAround() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestButton());
		VisibleInputConsumer buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.UP);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonThree.isOver());
	}

	@Test
	void consumeKeyInput_upFromTopOnNonLoopingMenu_doesntMove() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();
		navigableList.setMenuLoops(false);

		navigableList.consumeKeyInput(Input.UP);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void replaceContents_endsWithCorrectNumberOfEntries() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(makeTestButton());
		List<VisibleInputConsumer> replacementEntries = new ArrayList<>();
		replacementEntries.add(makeTestButton());
		replacementEntries.add(makeTestButton());
		replacementEntries.add(makeTestButton());
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.setRefreshPageRunnable(Runnables.nullRunnable());

		navigableList.replaceContents(replacementEntries);

		assertEquals(3, navigableList.allEntries.size());
	}

	@Test
	void replaceContents_withSpacer_filtersTheSpacerInInteractableEntities() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestSpacer());
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);

		assertEquals(3, navigableList.allEntries.size());
		assertEquals(2, navigableList.interactableEntries.size());
	}

	@Test
	void consumeInput_downWithSpacer_skipsSpacer() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestSpacer());
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
	}

	@Test
	void consumeInput_downPastSpacer_wrapsAround() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestSpacer());
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.DOWN);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void consumeInput_downWithDisabledButton_skipsDisabledButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		buttonTwo.setDisabled(true);
		entries.add(buttonTwo);
		VisibleInputConsumer buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertTrue(buttonThree.isOver());
	}

	@Test
	void consumeKeyInput_back_pressesFinalButton() {
		AtomicBoolean finalButtonPressed = new AtomicBoolean();
		finalButtonPressed.set(false);
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(makeTestButton());
		entries.add(makeTestButton());
		TestButton finalButton = new TestButton(() -> finalButtonPressed.set(true));
		finalButton.setBlank(false);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.setFinalButton(finalButton);

		navigableList.consumeKeyInput(Input.BACK);

		assertTrue(finalButtonPressed.get());
	}

	@Test
	void consumeKeyInput_accept_pressesCurrentButton() {
		AtomicBoolean testButtonPressed = new AtomicBoolean();
		testButtonPressed.set(false);
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer testButton = new TestButton(() -> testButtonPressed.set(true));
		entries.add(testButton);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.ACCEPT);

		assertTrue(testButtonPressed.get());
	}

	@Test
	void consumeKeyInput_inputWithoutCurrentButton_doesntCrash() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);

		assertDoesNotThrow(() -> navigableList.consumeKeyInput(Input.ACCEPT));
	}

	@Test
	void consumeKeyInput_backWithoutFinalButton_pressesNothing() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.BACK);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
	}

	@Test
	void consumeKeyInput_changingButtonsWithPressButtonOnEntryChangedSetTrue_pressesIt() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		AtomicBoolean buttonSpy = new AtomicBoolean(false);
		TestButton testButton = new TestButton(() -> buttonSpy.set(true));
		testButton.setBlank(false);
		entries.add(testButton);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.setPressButtonOnEntryChanged(true);
		navigableList.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(testButton.isOver());
		assertTrue(buttonSpy.get());
	}

	@Test
	void consumeKeyInput_changingButtonsWithPressButtonOnEntryChangedSetFalse_doesNotPressIt() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		AtomicBoolean buttonSpy = new AtomicBoolean(false);
		TestButton testButton = new TestButton(() -> buttonSpy.set(true));
		testButton.setBlank(false);
		entries.add(testButton);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.setPressButtonOnEntryChanged(false);
		navigableList.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(testButton.isOver());
		assertFalse(buttonSpy.get());
	}

	@Test
	void focusCurrent_focusesCurrentButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.focusCurrent();

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void clearSelected_leavesNoButtonFocused() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);
		navigableList.selectDefault();

		navigableList.focusCurrent();
		navigableList.clearSelected();

		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void isBlank_withNoEntries_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);

		assertTrue(navigableList.isBlank());
	}

	@Test
	void isBlank_withSomeEntries_returnsFalse() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);

		assertFalse(navigableList.isBlank());
	}

	@Test
	void isBlank_withOnlyASpacer_returnsFalse() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(makeTestSpacer());
		NavigableList navigableList = new NavigableList(MenuOrientation.VERTICAL, entries);

		assertFalse(navigableList.isBlank());
	}

	private static TestButton makeTestButton() {
		TestButton testButton = new TestButton();
		testButton.setBlank(false);
		return testButton;
	}

	private static VisibleInputConsumer makeTestSpacer() {
		VisibleInputConsumer spacer = new TestButton();
		spacer.setDisabled(true);
		assertTrue(VisibleInputConsumer.isSpacer(spacer));
		return spacer;
	}

}
