package com.darzalgames.libgdxtools.ui.input.inputpriority;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.maingame.MultipleStage;
import com.darzalgames.libgdxtools.maingame.StageLikeRenderable;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

class InputPriorityTest {

	@Test
	void claimPriority_calledStatically_callsEquivalentMethodOnStack() {
		AtomicBoolean spy = new AtomicBoolean(false);
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerPopUpForTesting();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		DarkScreenForTesting darkScreen = new DarkScreenForTesting();
		new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, darkScreen) {
			@Override
			void claimPriority(InputConsumer inputConsumer, String nameOfStageLikeRenderable) {
				spy.set(true);
				super.claimPriority(inputConsumer, nameOfStageLikeRenderable);
			}
		};
		InputConsumerPopUpForTesting consumer = new InputConsumerPopUpForTesting();

		InputPriority.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);

		assertTrue(spy.get());
	}

	@Test
	void releasePriority_calledStatically_callsEquivalentMethodOnStack() {
		AtomicBoolean spy = new AtomicBoolean(false);
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerPopUpForTesting();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		DarkScreenForTesting darkScreen = new DarkScreenForTesting();
		new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, darkScreen) {
			@Override
			void releasePriority(InputConsumer inputConsumer) {
				spy.set(true);
				super.releasePriority(inputConsumer);
			}
		};
		InputConsumerPopUpForTesting consumer = new InputConsumerPopUpForTesting();

		InputPriority.releasePriority(consumer);

		assertTrue(spy.get());
	}

	@Test
	void setInputPriorityStack_calledStatically_replacesTheOriginalStack() {
		AtomicBoolean spy = new AtomicBoolean(false);
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerPopUpForTesting();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		DarkScreenForTesting darkScreen = new DarkScreenForTesting();
		new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, darkScreen) {
			@Override
			void releasePriority(InputConsumer inputConsumer) {
				spy.set(true);
				super.releasePriority(inputConsumer);
			}
		}; // registers by calling setInputPriorityStack automatically in its constructor
		InputConsumerPopUpForTesting consumer = new InputConsumerPopUpForTesting();

		new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, darkScreen); // registers by calling setInputPriorityStack automatically in its constructor
		InputPriority.releasePriority(consumer);

		assertFalse(spy.get());
	}

}
