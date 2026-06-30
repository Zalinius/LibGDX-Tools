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
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UserInterfaceFactory;

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
	void getView_onConstruction_exists() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		assertNotNull(navigableListMenu.getView());
	}

	@Test
	void setTouchable_appliesToMainTableAndAllEntries() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
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
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		navigableListMenu.selectDefault();

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void constructor_withDefaultInteractabilityFilterOnSpacerAndDisabledButtons_filtersThem() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		buttonOne.setDisabled(true);
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer testSpacer = makeTestSpacer();
		entries.add(testSpacer);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		List<VisibleInputConsumer> interactableEntries = navigableListMenu.interactableEntries;

		assertEquals(1, interactableEntries.size());
		assertFalse(interactableEntries.contains(buttonOne));
		assertTrue(interactableEntries.contains(buttonTwo));
		assertFalse(interactableEntries.contains(testSpacer));
	}

	@Test
	void setInteractabilityFilter_refiltersInteractableEntriesAsExpected() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		buttonOne.setDisabled(true);
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		VisibleInputConsumer testSpacer = makeTestSpacer();
		entries.add(testSpacer);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		navigableListMenu.setInteractabilityFilter(entry -> !entry.isBlank()); // test allowing disabled buttons, for example if they have tooltips
		List<VisibleInputConsumer> interactableEntries = navigableListMenu.interactableEntries;

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
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		TestButton finalButton = makeTestButton();

		navigableListMenu.setFinalButton(finalButton);

		assertTrue(navigableListMenu.interactableEntries.contains(finalButton));
	}

	@Test
	void setFinalButton_toBlankButton_doesNotSetIt() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(makeTestButton());
		entries.add(makeTestButton());
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		TestButton finalButton = makeTestSpacer();

		navigableListMenu.setFinalButton(finalButton);

		assertFalse(navigableListMenu.interactableEntries.contains(finalButton));
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
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.setFinalButton(makeTestButton());
		navigableListMenu.setMenuLoops(false);

		navigableListMenu.selectDefault();

		assertEquals(expected, navigableListMenu.canUseInput(input));
	}

	@Test
	void canUseInput_backwardWhenNotOnFirstEntry_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);
		navigableListMenu.setFinalButton(makeTestButton());

		navigableListMenu.goTo(buttonTwo);

		assertTrue(navigableListMenu.canUseInput(Input.LEFT));
	}

	@Test
	void canUseInput_loopingBackwards_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.setFinalButton(makeTestButton());

		navigableListMenu.selectDefault();

		assertTrue(navigableListMenu.canUseInput(Input.UP));
	}

	@Test
	void canUseInput_loopingForwards_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);
		navigableListMenu.goTo(buttonTwo);

		assertTrue(navigableListMenu.canUseInput(Input.RIGHT));
	}

	@Test
	void canUseInput_acceptWithoutFocusedButton_returnsFalse() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);
		navigableListMenu.clearSelected();

		assertFalse(navigableListMenu.canUseInput(Input.ACCEPT));
	}

	@Test
	void canUseInput_backWithoutFinalButton_returnsFalse() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);

		assertFalse(navigableListMenu.canUseInput(Input.BACK));
	}

	@Test
	void consumeKeyInput_downFromTop_focusesSecond() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_downFromTopThenUpAgain_focusesFirst() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);
		navigableListMenu.consumeKeyInput(Input.UP);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_downFromTopTwice_loopsBackToFocusFirst() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);
		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_downFromTopTwiceOnNonLoopingMenu_keepsFocusOnLastButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.setMenuLoops(false);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);
		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_horizontalListGoRight_focusesSecond() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.RIGHT);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_horizontalListGoDown_doesntMoveFocus() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.HORIZONTAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_downTwiceFromTop_focusesThird() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);
		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertTrue(buttonThree.isOver());
	}

	@Test
	void goTo_specificButton_focusesCorrect() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		boolean changed = navigableListMenu.goTo(buttonTwo);

		assertTrue(changed);
		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
	}

	@Test
	void goTo_currentButton_maintainsFocusAndReturnsFalse() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		boolean changed = navigableListMenu.goTo(buttonOne);

		assertFalse(changed);
		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
	}

	@Test
	void goTo_invalidButton_doesntMoveFocus() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		boolean changed = navigableListMenu.goTo(makeTestButton());

		assertFalse(changed);
		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
	}

	@Test
	void returnToFirst_afterDownTwiceFromTop_focusesFirst() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = makeTestButton();
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
	}

	@Test
	void returnToLast_withNoInput_focusesLast() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		boolean changed = navigableListMenu.returnToLast();

		assertTrue(changed);
		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertTrue(buttonThree.isOver());
	}

	@Test
	void returnToLast_withFinalButton_focusesFinalButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();
		TestButton finalButton = makeTestButton();
		navigableListMenu.setFinalButton(finalButton);

		boolean changed = navigableListMenu.returnToLast();

		assertTrue(changed);
		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
		assertTrue(finalButton.isOver());
	}

	@Test
	void returnToSecondLast_withEnoughEntries_focusesSecondLast() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = makeTestButton();
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
	}

	@Test
	void returnToSecondLast_withOneEntry_focusesOnlyEntry() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		boolean changed = navigableListMenu.returnToSecondLast();

		assertFalse(changed);
		assertTrue(buttonOne.isOver());
	}

	@Test
	void returnToSecondLast_withNoEntries_doesntCrash() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		assertDoesNotThrow(navigableListMenu::returnToSecondLast);
	}

	@Test
	void consumeKeyInput_upFromTop_loopsAround() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestButton());
		TestButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.UP);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonThree.isOver());
	}

	@Test
	void consumeKeyInput_upFromTopOnNonLoopingMenu_doesntMove() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();
		navigableListMenu.setMenuLoops(false);

		navigableListMenu.consumeKeyInput(Input.UP);

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
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		navigableListMenu.replaceContents(replacementEntries);

		assertEquals(3, navigableListMenu.allEntries.size());
	}

	@Test
	void replaceContents_withSpacer_filtersTheSpacerInInteractableEntities() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestSpacer());
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		assertEquals(3, navigableListMenu.allEntries.size());
		assertEquals(2, navigableListMenu.interactableEntries.size());
	}

	@Test
	void consumeInput_downWithSpacer_skipsSpacer() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestSpacer());
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
	}

	@Test
	void consumeInput_downPastSpacer_wrapsAround() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestSpacer());
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);
		navigableListMenu.consumeKeyInput(Input.DOWN);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void consumeInput_downWithDisabledButton_skipsDisabledButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		buttonTwo.setDisabled(true);
		entries.add(buttonTwo);
		TestButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.DOWN);

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
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.setFinalButton(finalButton);

		navigableListMenu.consumeKeyInput(Input.BACK);

		assertTrue(finalButtonPressed.get());
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
	}

	@Test
	void consumeKeyInput_inputWithoutCurrentButton_doesntCrash() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		assertDoesNotThrow(() -> navigableListMenu.consumeKeyInput(Input.ACCEPT));
	}

	@Test
	void consumeKeyInput_backWithoutFinalButton_pressesNothing() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		TestButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.consumeKeyInput(Input.BACK);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
	}

	@Test
	void consumeKeyInput_changingButtonsWithPressButtonOnEntryChangedSetTrue_pressesIt() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
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
	}

	@Test
	void consumeKeyInput_changingButtonsWithPressButtonOnEntryChangedSetFalse_doesNotPressIt() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
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
	}

	@Test
	void focusCurrent_focusesCurrentButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.focusCurrent();

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void clearSelected_leavesNoButtonFocused() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);
		navigableListMenu.selectDefault();

		navigableListMenu.focusCurrent();
		navigableListMenu.clearSelected();

		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
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
		TestButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		TestButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		assertFalse(navigableListMenu.isBlank());
	}

	@Test
	void isBlank_withOnlyASpacer_returnsTrue() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(makeTestSpacer());
		NavigableListMenu navigableListMenu = makeTestMenu(MenuOrientation.VERTICAL, entries);

		assertTrue(navigableListMenu.isBlank());
	}

	private static TestButton makeTestButton() {
		TestButton testButton = new TestButton();
		testButton.setBlank(false);
		return testButton;
	}

	private static TestButton makeTestSpacer() {
		TestButton spacer = new TestButton();
		spacer.setDisabled(true);
		spacer.getView().setName(UserInterfaceFactory.SPACER_NAME);
		assertTrue(VisibleInputConsumer.isSpacer(spacer));
		return spacer;
	}

}
