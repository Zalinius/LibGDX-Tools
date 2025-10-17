package com.darzalgames.libgdxtools.ui.input.inputpriority;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.libgdxtools.maingame.MultipleStage;
import com.darzalgames.libgdxtools.maingame.StageLikeRenderable;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

class InputPriorityStackTest {

	@Test
	void constructor_doesNotThrow() {
		assertDoesNotThrow((ThrowingSupplier<InputPriorityStack>) InputPriorityStackTest::makeStack);
	}

	@Test
	void claimPriority_forNonPopUp_putsInputConsumerInFocus() {
		AtomicBoolean spy = new AtomicBoolean(false);
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void gainFocus() {
				spy.set(true);
				super.gainFocus();
			}

			@Override
			public void loseFocus() {
				spy.set(false);
				super.loseFocus();
			}
		};

		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);

		assertTrue(spy.get());
	}

	@Test
	void claimPriority_forSameConsumerTwice_putsInputConsumerInFocusOnlyOnce() {
		AtomicInteger spy = new AtomicInteger(0);
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void gainFocus() {
				spy.set(spy.get() + 1);
				super.gainFocus();
			}
		};

		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);

		assertEquals(1, spy.get());
	}

	@Test
	void claimPriority_forPopUp_putsPopUpInFocus() {
		AtomicBoolean spy = new AtomicBoolean(false);
		InputPriorityStack stack = makeStack();
		InputConsumerPopUpForTesting consumer = new InputConsumerPopUpForTesting() {
			@Override
			public void gainFocus() {
				spy.set(true);
				super.gainFocus();
			}

			@Override
			public void loseFocus() {
				spy.set(false);
				super.loseFocus();
			}
		};

		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);

		assertTrue(spy.get());
	}

	@Test
	void claimPriority_forPopUp_callsFadeInOnDarkScreenBehindIt() {
		AtomicBoolean spy = new AtomicBoolean(false);
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerPopUpForTesting();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		DarkScreenForTesting darkScreen = new DarkScreenForTesting() {
			@Override
			public void fadeIn(Actor actor, boolean canDismiss, StageLikeRenderable stageLikeRenderable) {
				spy.set(true);
				super.fadeIn(actor, canDismiss, stageLikeRenderable);
			}
		};
		InputPriorityStack stack = new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, darkScreen);
		InputConsumerPopUpForTesting consumer = new InputConsumerPopUpForTesting();

		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);

		assertTrue(spy.get());
	}

	@Test
	void claimPriority_withNonexistantStageName_throwsIllegalArgumentException() {
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting();

		assertThrows(IllegalArgumentException.class, () -> stack.claimPriority(consumer, "ng"));
	}

	private static InputPriorityStack makeStack() {
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerPopUpForTesting();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		return new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, new DarkScreenForTesting());
	}

}
