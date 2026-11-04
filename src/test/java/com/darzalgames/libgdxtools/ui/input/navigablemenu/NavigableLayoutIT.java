package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.maingame.*;
import com.darzalgames.libgdxtools.ui.input.inputpriority.DarkScreenForTesting;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriorityStack;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

class NavigableLayoutIT {

	private StageBest mainStage;
	private StageBest optionsStage;
	private NavigableLayout optionsMenu;

	@BeforeAll
	public static void setup() {
		TestWithTable.setUpBeforeAll();
	}

	@BeforeEach
	public void setUpStackAndStages() {
		StagesAndStack stagesAndStack = makeStack();
		mainStage = stagesAndStack.mainStage;
		optionsStage = stagesAndStack.optionsStage;
		optionsMenu = stagesAndStack.optionsMenu;
	}

	@Test
	void loseFocus_toNavigableLayoutAbove_aboveLayoutHasScrollFocus() {
		NavigableLayout layoutBelow = NavigableLayoutTest.makeTestLayout();
		mainStage.addActor(layoutBelow);
		NavigableLayout layoutAbove = NavigableLayoutTest.makeTestLayout();
		mainStage.addActor(layoutAbove);

		InputPriority.claimPriority(layoutBelow, mainStage.getName());
		InputPriority.claimPriority(layoutAbove, mainStage.getName());

		assertEquals(layoutAbove.scrollPane, mainStage.getScrollFocus());
		assertNotEquals(layoutAbove.scrollPane, layoutBelow.scrollPane);
	}

	@Test
	void regainFocus_fromNavigableLayoutAbove_belowLayoutHasScrollFocus() {
		NavigableLayout layoutBelow = NavigableLayoutTest.makeTestLayout();
		mainStage.addActor(layoutBelow);
		NavigableLayout layoutAbove = NavigableLayoutTest.makeTestLayout();
		mainStage.addActor(layoutAbove);

		InputPriority.claimPriority(layoutBelow, mainStage.getName());
		InputPriority.claimPriority(layoutAbove, mainStage.getName());
		InputPriority.releasePriority(layoutAbove);

		assertEquals(layoutBelow.scrollPane, mainStage.getScrollFocus());
	}

	@Test
	void loseFocus_toOptionsMenu_optionsMenuStageHasOptionsMenuLayoutAsScrollFocusAndLowerStageHasNullScrollFocus() {
		NavigableLayout layoutBelow = NavigableLayoutTest.makeTestLayout();
		mainStage.addActor(layoutBelow);

		InputPriority.claimPriority(layoutBelow, mainStage.getName());
		InputPriority.claimPriority(optionsMenu, optionsStage.getName());

		assertEquals(optionsMenu.scrollPane, optionsStage.getScrollFocus());
		assertNull(mainStage.getScrollFocus());
	}

	@Test
	void regainFocusFromOptionsMenu_optionsMenuStageHasNullScrollFocusAndLowerStageHasLayoutAsScrollFocus() {
		NavigableLayout layoutBelow = NavigableLayoutTest.makeTestLayout();
		mainStage.addActor(layoutBelow);

		InputPriority.claimPriority(layoutBelow, mainStage.getName());
		InputPriority.claimPriority(optionsMenu, optionsStage.getName());
		InputPriority.releasePriority(optionsMenu);

		assertEquals(layoutBelow.scrollPane, mainStage.getScrollFocus());
		assertNull(optionsStage.getScrollFocus());
	}

	private static StagesAndStack makeStack() {
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		StageBest dummyMainStage = DummyStageBestFactory.make();
		dummyMainStage.getRoot().setName(MultipleStage.MAIN_STAGE_NAME);
		StageBest dummyOptionsStage = DummyStageBestFactory.make();
		dummyOptionsStage.getRoot().setName(MultipleStage.OPTIONS_STAGE_NAME);
		allStagesInOrderForInput.add(dummyMainStage);
		allStagesInOrderForInput.add(dummyOptionsStage);

		NavigableLayout optionsMenu = NavigableLayoutTest.makeTestLayout();
		dummyOptionsStage.addActor(optionsMenu);
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		InputPriorityStack stack = new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, new DarkScreenForTesting());

		return new StagesAndStack(dummyMainStage, dummyOptionsStage, optionsMenu, stack);
	}

	private record StagesAndStack(StageBest mainStage, StageBest optionsStage, NavigableLayout optionsMenu, InputPriorityStack stack) {}

}
