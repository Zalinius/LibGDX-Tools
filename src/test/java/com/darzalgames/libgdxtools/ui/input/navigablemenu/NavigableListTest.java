package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

class NavigableListTest {

	@Test
	void selectDefault_onlyFirstButtonIsFocused() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);


		navigableList.selectDefault();

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	@Test
	void consumeKeyInput_downFromTop_focusesSecond() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(false, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.RIGHT);

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
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.goTo(buttonTwo);

		assertFalse(buttonOne.isOver());
		assertTrue(buttonTwo.isOver());
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
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.goTo(makeTestButton());

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
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.returnToFirst();

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
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.returnToLast();

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
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		TestButton finalButton = new TestButton();
		finalButton.setBlank(false);
		//		VisibleInputConsumer finalButton = new TestButton(() -> "back", inputStrategySwitcher, Runnables.nullRunnable());
		navigableList.setFinalButton(finalButton);

		navigableList.returnToLast();

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
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		TestButton finalButton = new TestButton();
		finalButton.setBlank(false);
		navigableList.setFinalButton(finalButton);

		navigableList.returnToSecondLast();

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
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.returnToSecondLast();

		assertTrue(buttonOne.isOver());
	}

	@Test
	void returnToSecondLast_withNoEntries_doesntCrash() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		assertDoesNotThrow(() -> navigableList.returnToSecondLast());
	}


	@Test
	void consumeKeyInput_upFromTop_loopsAround() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestButton());
		VisibleInputConsumer buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);


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
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.ACCEPT);

		assertTrue(testButtonPressed.get());
	}

	@Test
	void consumeKeyInput_inputWithoutCurrentButton_doesntCrash() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		NavigableList navigableList = new NavigableList(true, entries);

		assertDoesNotThrow(() -> navigableList.consumeKeyInput(Input.ACCEPT));
	}

	@Test
	void consumeKeyInput_accept_triggersExtraKeyListeners() {
		AtomicBoolean listenerNotifiedPressed = new AtomicBoolean();
		listenerNotifiedPressed.set(false);
		List<VisibleInputConsumer> entries = new ArrayList<>();
		entries.add(makeTestButton());
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		navigableList.addExtraListener(input -> {
			if (input.equals(Input.ACCEPT)) {
				listenerNotifiedPressed.set(true);
			}
		});

		navigableList.consumeKeyInput(Input.ACCEPT);

		assertTrue(listenerNotifiedPressed.get());
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
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.BACK);

		assertTrue(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
		assertFalse(buttonThree.isOver());
	}

	@Test
	void focusCurrent_focusesCurrentButton() {
		List<VisibleInputConsumer> entries = new ArrayList<>();
		VisibleInputConsumer buttonOne = makeTestButton();
		entries.add(buttonOne);
		VisibleInputConsumer buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
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
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.focusCurrent();
		navigableList.clearSelected();

		assertFalse(buttonOne.isOver());
		assertFalse(buttonTwo.isOver());
	}

	private static VisibleInputConsumer makeTestButton() {
		return new TestButton();
	}

	private static VisibleInputConsumer makeTestSpacer() {
		VisibleInputConsumer spacer = new TestButton();
		spacer.setDisabled(true);
		assertTrue(VisibleInputConsumer.isSpacer(spacer));
		return spacer;
	}

}
