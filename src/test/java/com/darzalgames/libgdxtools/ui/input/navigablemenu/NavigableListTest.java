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
	
	// TODO run coverage tests and fill gaps on NavigableList
	
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


	private static InputStrategySwitcher inputStrategySwitcher;
	
	@BeforeAll
	public static void setUp() {
		inputStrategySwitcher = makeInputStrategySwitcher();
	}
	
	private static UniversalButton makeTestButton() {
		return new UniversalButton(new TestBasicButton(), () -> "", inputStrategySwitcher, Runnables.nullRunnable());
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
