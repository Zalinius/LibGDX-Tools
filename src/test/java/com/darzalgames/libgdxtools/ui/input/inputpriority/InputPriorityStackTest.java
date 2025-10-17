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
import com.darzalgames.libgdxtools.ui.input.Input;
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

	@Test
	void releasePriority_withTopmostConsumer_callsRemoveFocus() {
		AtomicBoolean spy = new AtomicBoolean(false);
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void removedFocus() {
				spy.set(true);
				super.removedFocus();
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);

		stack.releasePriority(consumer);

		assertTrue(spy.get());
	}

	@Test
	void releasePriority_withTopmostConsumerButNotTopmostLayer_callsRemoveFocusAnyway() {
		AtomicBoolean spy = new AtomicBoolean(false);
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void removedFocus() {
				spy.set(true);
				super.removedFocus();
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(new InputConsumerPopUpForTesting(), MultipleStage.OPTIONS_STAGE_NAME);

		stack.releasePriority(consumer);

		assertTrue(spy.get());
	}

	@Test
	void releasePriority_withNotTopmostConsumer_doesNotRemoveFocusFromTopmostConsumer() {
		AtomicBoolean bottomConsumerRemovalSpy = new AtomicBoolean(false);
		AtomicBoolean topConsumerRemovalSpy = new AtomicBoolean(false);
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void removedFocus() {
				bottomConsumerRemovalSpy.set(true);
				super.removedFocus();
			}
		};
		InputConsumerForTesting topConsumer = new InputConsumerForTesting() {
			@Override
			public void removedFocus() {
				topConsumerRemovalSpy.set(true);
				super.removedFocus();
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(topConsumer, MultipleStage.MAIN_STAGE_NAME);

		stack.releasePriority(consumer);

		assertTrue(bottomConsumerRemovalSpy.get());
		assertFalse(topConsumerRemovalSpy.get());
	}

	@Test
	void releasePriority_withPopUpBelowRegularConsumer_doesNotRemoveDarkScreen() {
		AtomicBoolean spy = new AtomicBoolean(true);
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.OPTIONS_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerPopUpForTesting();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		DarkScreenForTesting darkScreen = new DarkScreenForTesting() {
			@Override
			public void fadeOutAndRemove() {
				spy.set(false);
				super.fadeOutAndRemove();
			}
		};
		InputPriorityStack stack = new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, darkScreen);
		InputConsumerForTesting consumer = new InputConsumerForTesting();
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(new InputConsumerPopUpForTesting(), MultipleStage.OPTIONS_STAGE_NAME);

		stack.releasePriority(consumer);

		assertTrue(spy.get());
	}

	@Test
	void releasePriority_with2StacedPopUps_doesNotRemoveDarkScreen() {
		AtomicBoolean spy = new AtomicBoolean(true);
		AtomicBoolean isTestReady = new AtomicBoolean(false);
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.OPTIONS_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerPopUpForTesting();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		DarkScreenForTesting darkScreen = new DarkScreenForTesting() {
			@Override
			public void fadeOutAndRemove() {
				if (isTestReady.get()) {
					spy.set(false);
				}
				super.fadeOutAndRemove();
			}

			@Override
			public void fadeIn(Actor actor, boolean canDismiss, StageLikeRenderable stageLikeRenderable) {
				if (isTestReady.get()) {
					spy.set(true);
				}
				super.fadeIn(actor, canDismiss, stageLikeRenderable);
			}
		};
		InputPriorityStack stack = new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, darkScreen);
		InputConsumerForTesting popUp = new InputConsumerPopUpForTesting();
		stack.claimPriority(new InputConsumerPopUpForTesting(), MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(popUp, MultipleStage.MAIN_STAGE_NAME);

		stack.releasePriority(popUp);

		assertTrue(spy.get());
	}

	@Test
	void releasePriority_withPopUpOnTopOfRegularConsumer_doesRemoveDarkScreen() {
		AtomicBoolean spy = new AtomicBoolean(true);
		AtomicBoolean isTestReady = new AtomicBoolean(false);
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.OPTIONS_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerPopUpForTesting();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		DarkScreenForTesting darkScreen = new DarkScreenForTesting() {
			@Override
			public void fadeIn(Actor actor, boolean canDismiss, StageLikeRenderable stageLikeRenderable) {
				if (isTestReady.get()) {
					spy.set(false);
				}
				super.fadeIn(actor, canDismiss, stageLikeRenderable);
			}
		};
		InputPriorityStack stack = new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, darkScreen);
		InputConsumerForTesting popUp = new InputConsumerPopUpForTesting();
		stack.claimPriority(new InputConsumerForTesting(), MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(popUp, MultipleStage.OPTIONS_STAGE_NAME);
		isTestReady.set(true);

		stack.releasePriority(popUp);

		assertTrue(spy.get());
	}

	@Test
	void releasePriority_forStackedConsumers_callsRegainFocusOnTheBottomConsumer() {
		AtomicBoolean regainFocusSpy = new AtomicBoolean(false);
		AtomicBoolean isTestReady = new AtomicBoolean(false);
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void regainFocus() {
				if (isTestReady.get()) {
					regainFocusSpy.set(true);
				}
				super.regainFocus();
			}
		};
		InputConsumerForTesting topConsumer = new InputConsumerForTesting();
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(topConsumer, MultipleStage.OPTIONS_STAGE_NAME);
		isTestReady.set(true);

		stack.releasePriority(topConsumer);

		assertTrue(regainFocusSpy.get());
	}

	@Test
	void releasePriority_forOptionsMenu_callsFocusCurrentButNotRegainFocusForConsumerBelow() {
		AtomicBoolean focusCurrentSpy = new AtomicBoolean(false);
		AtomicBoolean regainFocusSpy = new AtomicBoolean(false);
		AtomicBoolean isTestReady = new AtomicBoolean(false);
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.OPTIONS_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerForTesting();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		InputPriorityStack stack = new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, new DarkScreenForTesting());
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void focusCurrent() {
				if (isTestReady.get()) {
					focusCurrentSpy.set(true);
				}
				super.focusCurrent();
			}

			@Override
			public void regainFocus() {
				if (isTestReady.get()) {
					regainFocusSpy.set(true);
				}
				super.regainFocus();
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(optionsMenu, MultipleStage.OPTIONS_STAGE_NAME);
		isTestReady.set(true);

		stack.releasePriority(optionsMenu);

		assertTrue(focusCurrentSpy.get());
		assertFalse(regainFocusSpy.get());
	}

	@Test
	void sendInputToTop_withoutAnythingClaimed_doesNotThrow() {
		InputPriorityStack stack = makeStack();

		assertDoesNotThrow(() -> stack.sendInputToTop(Input.ACCEPT));
	}

	@Test
	void sendInputToTop_routesToConsumer() {
		AtomicBoolean spy = new AtomicBoolean(false);
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void consumeKeyInput(Input input) {
				spy.set(true);
				super.consumeKeyInput(input);
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);

		stack.sendInputToTop(Input.ACCEPT);

		assertTrue(spy.get());
	}

	@Test
	void sendInputToTop_withMultipleConsumersOnSameLayer_sendsInputToTopmostConsumer() {
		AtomicBoolean bottomConsumerInputSpy = new AtomicBoolean(false);
		AtomicBoolean topConsumerInputSpy = new AtomicBoolean(false);
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void consumeKeyInput(Input input) {
				bottomConsumerInputSpy.set(true);
				super.consumeKeyInput(input);
			}
		};
		InputConsumerForTesting topConsumer = new InputConsumerForTesting() {
			@Override
			public void consumeKeyInput(Input input) {
				topConsumerInputSpy.set(true);
				super.consumeKeyInput(input);
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(topConsumer, MultipleStage.MAIN_STAGE_NAME);

		stack.sendInputToTop(Input.ACCEPT);

		assertFalse(bottomConsumerInputSpy.get());
		assertTrue(topConsumerInputSpy.get());
	}

	@Test
	void sendInputToTop_withMultipleConsumersOnDifferentLayers_sendsInputToTopmostConsumer() {
		AtomicBoolean bottomConsumerInputSpy = new AtomicBoolean(false);
		AtomicBoolean topConsumerInputSpy = new AtomicBoolean(false);
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void consumeKeyInput(Input input) {
				bottomConsumerInputSpy.set(true);
				super.consumeKeyInput(input);
			}
		};
		InputConsumerForTesting topConsumer = new InputConsumerForTesting() {
			@Override
			public void consumeKeyInput(Input input) {
				topConsumerInputSpy.set(true);
				super.consumeKeyInput(input);
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(topConsumer, MultipleStage.OPTIONS_STAGE_NAME);

		stack.sendInputToTop(Input.ACCEPT);

		assertFalse(bottomConsumerInputSpy.get());
		assertTrue(topConsumerInputSpy.get());
	}

	@Test
	void doesTopPauseGame_withMultipleConsumersOnDifferentLayers_onlyBottomConsumerPausesGame_returnsFalse() {
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public boolean isGamePausedWhileThisIsInFocus() {
				return true;
			}
		};
		InputConsumerForTesting topConsumer = new InputConsumerForTesting();
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(topConsumer, MultipleStage.OPTIONS_STAGE_NAME);

		boolean result = stack.doesTopPauseGame();

		assertFalse(result);
	}

	@Test
	void doesTopPauseGame_withMultipleConsumersOnDifferentLayers_onlyTopConsumerPausesGame_returnsTrue() {
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting();
		InputConsumerForTesting topConsumer = new InputConsumerForTesting() {
			@Override
			public boolean isGamePausedWhileThisIsInFocus() {
				return true;
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(topConsumer, MultipleStage.OPTIONS_STAGE_NAME);

		boolean result = stack.doesTopPauseGame();

		assertTrue(result);
	}

	@Test
	void getNameOfPausingStage_withNonPausingConsumer_returnsEmptyString() {
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public boolean isGamePausedWhileThisIsInFocus() {
				return false;
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);

		String result = stack.getNameOfPausingStage();

		assertEquals("", result);
	}

	@Test
	void getNameOfPausingStage_withSinglePausingConsumer_returnsConsumersStageName() {
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public boolean isGamePausedWhileThisIsInFocus() {
				return true;
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);

		String result = stack.getNameOfPausingStage();

		assertEquals(MultipleStage.MAIN_STAGE_NAME, result);
	}

	@Test
	void getNameOfPausingStage_withMultipleConsumersOnDifferentLayers_onlyTopConsumerPausesGame_returnsTopConsumersStageName() {
		InputPriorityStack stack = makeStack();
		InputConsumerForTesting consumer = new InputConsumerForTesting();
		InputConsumerForTesting topConsumer = new InputConsumerForTesting() {
			@Override
			public boolean isGamePausedWhileThisIsInFocus() {
				return true;
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(topConsumer, MultipleStage.OPTIONS_STAGE_NAME);

		String result = stack.getNameOfPausingStage();

		assertEquals(MultipleStage.OPTIONS_STAGE_NAME, result);
	}

	@Test
	void inputStrategyChanged_toKeyboardMode_selectsDefaultOnOnlyTopConsumer() {
		AtomicBoolean bottomConsumerSpy = new AtomicBoolean(false);
		AtomicBoolean topConsumerSpy = new AtomicBoolean(false);
		InputPriorityStack stack = makeStack();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void selectDefault() {
				bottomConsumerSpy.set(true);
				super.selectDefault();
			}
		};
		InputConsumerForTesting topConsumer = new InputConsumerForTesting() {
			@Override
			public void selectDefault() {
				topConsumerSpy.set(true);
				super.selectDefault();
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(topConsumer, MultipleStage.OPTIONS_STAGE_NAME);

		inputStrategySwitcher.setToKeyboardAndGamepadStrategy();
		stack.inputStrategyChanged(inputStrategySwitcher);

		assertFalse(bottomConsumerSpy.get());
		assertTrue(topConsumerSpy.get());
	}

	@Test
	void inputStrategyChanged_toMouseMode_clearsSelectedOnOnlyTopConsumer() {
		AtomicBoolean bottomConsumerSpy = new AtomicBoolean(false);
		AtomicBoolean topConsumerSpy = new AtomicBoolean(false);
		InputPriorityStack stack = makeStack();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void clearSelected() {
				bottomConsumerSpy.set(true);
				super.clearSelected();
			}
		};
		InputConsumerForTesting topConsumer = new InputConsumerForTesting() {
			@Override
			public void clearSelected() {
				topConsumerSpy.set(true);
				super.clearSelected();
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(topConsumer, MultipleStage.OPTIONS_STAGE_NAME);

		inputStrategySwitcher.setToMouseStrategy();
		stack.inputStrategyChanged(inputStrategySwitcher);

		assertFalse(bottomConsumerSpy.get());
		assertTrue(topConsumerSpy.get());
	}

	@Test
	void clearChildren_removesExistingConsumersAndDarkScreen() {
		AtomicBoolean darkScreenSpy = new AtomicBoolean(false);
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.OPTIONS_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerPopUpForTesting();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		DarkScreenForTesting darkScreen = new DarkScreenForTesting() {
			@Override
			public boolean remove() {
				darkScreenSpy.set(true);
				return super.remove();
			}
		};
		InputPriorityStack stack = new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, darkScreen);
		AtomicBoolean bottomConsumerSpy = new AtomicBoolean(false);
		AtomicBoolean topConsumerSpy = new AtomicBoolean(false);
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void consumeKeyInput(Input input) {
				bottomConsumerSpy.set(true);
				super.consumeKeyInput(input);
			}
		};
		InputConsumerForTesting topConsumer = new InputConsumerForTesting() {
			@Override
			public void consumeKeyInput(Input input) {
				topConsumerSpy.set(true);
				super.consumeKeyInput(input);
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(topConsumer, MultipleStage.OPTIONS_STAGE_NAME);

		stack.clearChildren();
		stack.sendInputToTop(Input.ACCEPT);

		assertTrue(darkScreenSpy.get());
		assertFalse(bottomConsumerSpy.get());
		assertFalse(topConsumerSpy.get());
	}

	@Test
	void hideDarkScreenForVeryExceptionalCircumstances_removesTheDarkScreen() {
		AtomicBoolean darkScreenSpy = new AtomicBoolean(false);
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.OPTIONS_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerPopUpForTesting();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		DarkScreenForTesting darkScreen = new DarkScreenForTesting() {
			@Override
			public boolean remove() {
				darkScreenSpy.set(true);
				return super.remove();
			}
		};
		InputPriorityStack stack = new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, darkScreen);

		stack.hideDarkScreenForVeryExceptionalCircumstances();

		assertTrue(darkScreenSpy.get());
	}

	@Test
	void shouldBeUnregistered_returnsFalse() {
		InputPriorityStack stack = makeStack();

		assertFalse(stack.shouldBeUnregistered());
	}

	@Test
	void resizeStackUI_tricklesDownToAllConsumersOnTheStack_evenWhenTopConsumerPauses() {
		AtomicBoolean optionsMenuSpy = new AtomicBoolean(false);
		AtomicBoolean bottomConsumerSpy = new AtomicBoolean(false);
		AtomicBoolean topConsumerSpy = new AtomicBoolean(false);
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.OPTIONS_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerPopUpForTesting() {
			@Override
			public void resizeUI() {
				optionsMenuSpy.set(true);
				super.resizeUI();
			}

			@Override
			public boolean isGamePausedWhileThisIsInFocus() {
				return true;
			}
		};
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		DarkScreenForTesting darkScreen = new DarkScreenForTesting();
		InputPriorityStack stack = new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, darkScreen);
		InputConsumerForTesting consumer = new InputConsumerForTesting() {
			@Override
			public void resizeUI() {
				bottomConsumerSpy.set(true);
				super.resizeUI();
			}
		};
		InputConsumerForTesting topConsumer = new InputConsumerForTesting() {
			@Override
			public void resizeUI() {
				topConsumerSpy.set(true);
				super.resizeUI();
			}
		};
		stack.claimPriority(consumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(topConsumer, MultipleStage.MAIN_STAGE_NAME);
		stack.claimPriority(optionsMenu, MultipleStage.OPTIONS_STAGE_NAME);

		stack.resizeStackUI();

		assertTrue(optionsMenuSpy.get());
		assertTrue(bottomConsumerSpy.get());
		assertTrue(topConsumerSpy.get());
	}

	private static InputPriorityStack makeStack() {
		List<StageLikeRenderable> allStagesInOrderForInput = new ArrayList<>();
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.MAIN_STAGE_NAME));
		allStagesInOrderForInput.add(new TestStageLikeRenderable(MultipleStage.OPTIONS_STAGE_NAME));
		InputConsumer optionsMenu = new InputConsumerPopUpForTesting();
		InputStrategySwitcher inputStrategySwitcher = new InputStrategySwitcher();
		return new InputPriorityStack(allStagesInOrderForInput, optionsMenu, inputStrategySwitcher, new DarkScreenForTesting());
	}

}
