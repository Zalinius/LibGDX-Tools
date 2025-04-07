package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.TestBasicButton;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

public class NavigableListTest {
	
	// TODO test skipping spacers & disabled buttons, final button, etc.
	
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
