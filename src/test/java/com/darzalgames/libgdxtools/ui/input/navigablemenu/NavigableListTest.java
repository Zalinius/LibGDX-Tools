package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.TestBasicButton;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

public class NavigableListTest {

	@Test
	void selectDefault_onlyFirstButtonIsFocused() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
		
		
		navigableList.selectDefault();
		
		assertTrue(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
	}

	@Test
	void consumeKeyInput_downFromTop_focusesSecond() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.consumeKeyInput(Input.DOWN);
		
		assertFalse(buttonOne.getButton().isOver());
		assertTrue(buttonTwo.getButton().isOver());
	}
	
	@Test
	void consumeKeyInput_downFromTopThenUpAgain_focusesFirst() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.UP);
		
		assertTrue(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
	}
	
	@Test
	void consumeKeyInput_downFromTopTwice_loopsBackToFocusFirst() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.DOWN);
		
		assertTrue(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
	}

	@Test
	void consumeKeyInput_downFromTopTwiceOnNonLoopingMenu_keepsFocusOnLastButton() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.setMenuLoops(false);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.DOWN);
		
		assertFalse(buttonOne.getButton().isOver());
		assertTrue(buttonTwo.getButton().isOver());
	}

	@Test
	void consumeKeyInput_horizontalListGoRight_focusesSecond() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(false, entries);
		navigableList.selectDefault();
		
		navigableList.consumeKeyInput(Input.RIGHT);
		
		assertFalse(buttonOne.getButton().isOver());
		assertTrue(buttonTwo.getButton().isOver());
	}

	@Test
	void consumeKeyInput_horizontalListGoDown_doesntMoveFocus() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.consumeKeyInput(Input.RIGHT);
		
		assertTrue(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
	}
	
	@Test
	void consumeKeyInput_downTwiceFromTop_focusesThird() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		UniversalButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.DOWN);
		
		assertFalse(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
		assertTrue(buttonThree.getButton().isOver());
	}
	
	@Test
	void goTo_specificButton_focusesCorrect() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		UniversalButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.goTo(buttonTwo);
		
		assertFalse(buttonOne.getButton().isOver());
		assertTrue(buttonTwo.getButton().isOver());
		assertFalse(buttonThree.getButton().isOver());
	}
	
	@Test
	void goTo_invalidButton_doesntMoveFocus() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		UniversalButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.goTo(makeTestButton());
		
		assertTrue(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
		assertFalse(buttonThree.getButton().isOver());
	}
	
	@Test
	void returnToFirst_afterDownTwiceFromTop_focusesFirst() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		UniversalButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.returnToFirst();
		
		assertTrue(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
		assertFalse(buttonThree.getButton().isOver());
	}
	
	@Test
	void returnToLast_withNoInput_focusesLast() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		UniversalButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.returnToLast();
		
		assertFalse(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
		assertTrue(buttonThree.getButton().isOver());
	}
	
	@Test
	void returnToLast_withFinalButton_focusesFinalButton() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		UniversalButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		UniversalButton finalButton = new UniversalButton(new TestBasicButton(), () -> "back", inputStrategySwitcher, Runnables.nullRunnable());
		navigableList.setFinalButton(finalButton);
		
		navigableList.returnToLast();
		
		assertFalse(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
		assertFalse(buttonThree.getButton().isOver());
		assertTrue(finalButton.getButton().isOver());
	}
	
	@Test
	void returnToSecondLast_withEnoughEntries_focusesSecondLast() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		UniversalButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		UniversalButton finalButton = new UniversalButton(new TestBasicButton(), () -> "back", inputStrategySwitcher, Runnables.nullRunnable());
		navigableList.setFinalButton(finalButton);
		
		navigableList.returnToSecondLast();
		
		assertFalse(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
		assertTrue(buttonThree.getButton().isOver());
		assertFalse(finalButton.getButton().isOver());
	}

	@Test
	void returnToSecondLast_withOneEntry_focusesOnlyEntry() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.returnToSecondLast();
		
		assertTrue(buttonOne.getButton().isOver());
	}

	@Test
	void returnToSecondLast_withNoEntries_doesntCrash() {
		List<UniversalButton> entries = new ArrayList<>();
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		assertDoesNotThrow(() -> navigableList.returnToSecondLast());
	}
	
	
	@Test
	void consumeKeyInput_upFromTop_loopsAround() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestButton());
		UniversalButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.consumeKeyInput(Input.UP);
		
		assertFalse(buttonOne.getButton().isOver());
		assertTrue(buttonThree.getButton().isOver());
	}	
	
	@Test
	void consumeKeyInput_upFromTopOnNonLoopingMenu_doesntMove() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		navigableList.setMenuLoops(false);
		
		navigableList.consumeKeyInput(Input.UP);
		
		assertTrue(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
	}
	
	@Test
	void replaceContents_endsWithCorrectNumberOfEntries() {
		List<UniversalButton> entries = new ArrayList<>();
		entries.add(makeTestButton());
		List<UniversalButton> replacementEntries = new ArrayList<>();
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
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestSpacer());
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
		

		assertEquals(3, navigableList.allEntries.size());
		assertEquals(2, navigableList.interactableEntries.size());
	}	
	
	
	
	@Test
	void consumeInput_downWithSpacer_skipsSpacer() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestSpacer());
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.getButton().isOver());
		assertTrue(buttonTwo.getButton().isOver());
	}		
	
	@Test
	void consumeInput_downPastSpacer_wrapsAround() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		entries.add(makeTestSpacer());
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);
		navigableList.consumeKeyInput(Input.DOWN);

		assertTrue(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
	}	
	
	@Test
	void consumeInput_downWithDisabledButton_skipsDisabledButton() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		buttonTwo.setDisabled(true);
		entries.add(buttonTwo);
		UniversalButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();

		navigableList.consumeKeyInput(Input.DOWN);

		assertFalse(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
		assertTrue(buttonThree.getButton().isOver());
	}
	
	@Test
	void consumeKeyInput_back_pressesFinalButton() {
		AtomicBoolean finalButtonPressed = new AtomicBoolean();
		finalButtonPressed.set(false);
		List<UniversalButton> entries = new ArrayList<>();
		entries.add(makeTestButton());
		entries.add(makeTestButton());
		UniversalButton finalButton = new UniversalButton(new TestBasicButton(), () -> "back", inputStrategySwitcher, () -> finalButtonPressed.set(true));
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.setFinalButton(finalButton);
		
		navigableList.consumeKeyInput(Input.BACK);
		
		assertTrue(finalButtonPressed.get());
	}

	@Test
	void consumeKeyInput_accept_pressesCurrentButton() {
		AtomicBoolean testButtonPressed = new AtomicBoolean();
		testButtonPressed.set(false);
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton testButton = new UniversalButton(new TestBasicButton(), () -> "test", inputStrategySwitcher, () -> testButtonPressed.set(true));
		entries.add(testButton);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.consumeKeyInput(Input.ACCEPT);
		
		assertTrue(testButtonPressed.get());
	}
	
	@Test
	void consumeKeyInput_inputWithoutCurrentButton_doesntCrash() {
		List<UniversalButton> entries = new ArrayList<>();
		NavigableList navigableList = new NavigableList(true, entries);
		
		assertDoesNotThrow(() -> navigableList.consumeKeyInput(Input.ACCEPT));
	}
	
	@Test
	void consumeKeyInput_accept_triggersExtraKeyListeners() {
		AtomicBoolean listenerNotifiedPressed = new AtomicBoolean();
		listenerNotifiedPressed.set(false);
		List<UniversalButton> entries = new ArrayList<>();
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
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		UniversalButton buttonThree = makeTestButton();
		entries.add(buttonThree);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.consumeKeyInput(Input.BACK);
		
		assertTrue(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
		assertFalse(buttonThree.getButton().isOver());
	}
	
	@Test
	void focusCurrent_focusesCurrentButton() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.focusCurrent();
		
		assertTrue(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
	}	
	
	@Test
	void clearSelected_leavesNoButtonFocused() {
		List<UniversalButton> entries = new ArrayList<>();
		UniversalButton buttonOne = makeTestButton();
		entries.add(buttonOne);
		UniversalButton buttonTwo = makeTestButton();
		entries.add(buttonTwo);
		NavigableList navigableList = new NavigableList(true, entries);
		navigableList.selectDefault();
		
		navigableList.focusCurrent();
		navigableList.clearSelected();
		
		assertFalse(buttonOne.getButton().isOver());
		assertFalse(buttonTwo.getButton().isOver());
	}


	private static InputStrategySwitcher inputStrategySwitcher;
	
	@BeforeAll
	public static void setUp() {
		inputStrategySwitcher = makeInputStrategySwitcher();
	}

	private static UniversalButton makeTestButton() {
		return new UniversalButton(new TestBasicButton(), () -> "", inputStrategySwitcher, Runnables.nullRunnable());
	}
	
	private static UniversalButton makeTestSpacer() {
		UniversalButton spacer = new UniversalButton(new TestBasicButton(), () -> "", inputStrategySwitcher, Runnables.nullRunnable());
		spacer.setDisabled(true);
		return spacer;
	}
	
	private static InputStrategySwitcher makeInputStrategySwitcher() {
		return new InputStrategySwitcher(null, null) {
			@Override
			public boolean shouldFlashButtons() {
				return true;
			}
		};
	}
	
	
}
