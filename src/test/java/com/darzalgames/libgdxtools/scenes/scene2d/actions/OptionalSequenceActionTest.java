package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

class OptionalSequenceActionTest {
	
	private List<AtomicBoolean> spies;
	
	@BeforeEach
	void setup() {
		spies = Arrays.asList(new AtomicBoolean(), new AtomicBoolean(), new AtomicBoolean());
	}
	
	@Test
	void act_withAllActionsEnabled_onlyRunsFirstActionAndActingNotDone() throws Exception {
		OptionalAction optionalAction0 = makeOptionalActionWithSpy(spies.get(0), true);
		OptionalAction optionalAction1 = makeOptionalActionWithSpy(spies.get(1), true);
		OptionalAction optionalAction2 = makeOptionalActionWithSpy(spies.get(2), true);
		OptionalSequenceAction optionalSequenceAction = new OptionalSequenceAction(optionalAction0, optionalAction1, optionalAction2);
		
		boolean actingDone = optionalSequenceAction.act(0f);
		
		assertTrue(spies.get(0).get());		
		assertFalse(spies.get(1).get());		
		assertFalse(spies.get(2).get());
		assertFalse(actingDone);
	}

	@Test
	void act_withAllActionsEnabledAndAllRun_onlyRunsAllActionsAndActingDoneAtEnd() throws Exception {
		OptionalAction optionalAction0 = makeOptionalActionWithSpy(spies.get(0), true);
		OptionalAction optionalAction1 = makeOptionalActionWithSpy(spies.get(1), true);
		OptionalAction optionalAction2 = makeOptionalActionWithSpy(spies.get(2), true);
		OptionalSequenceAction optionalSequenceAction = new OptionalSequenceAction(optionalAction0, optionalAction1, optionalAction2);
		
		boolean actingDone0 = optionalSequenceAction.act(0f);
		boolean actingDone1 = optionalSequenceAction.act(0f);
		boolean actingDone2 = optionalSequenceAction.act(0f);
		
		assertTrue(spies.get(0).get());		
		assertTrue(spies.get(1).get());		
		assertTrue(spies.get(2).get());
		assertFalse(actingDone0);
		assertFalse(actingDone1);
		assertTrue(actingDone2);
	}

	@Test
	void act_withFirstActionDisabled_onlyRunsSecondActionAndActingNotDone() throws Exception {
		OptionalAction optionalAction0 = makeOptionalActionWithSpy(spies.get(0), false);
		OptionalAction optionalAction1 = makeOptionalActionWithSpy(spies.get(1), true);
		OptionalAction optionalAction2 = makeOptionalActionWithSpy(spies.get(2), true);
		OptionalSequenceAction optionalSequenceAction = new OptionalSequenceAction(optionalAction0, optionalAction1, optionalAction2);
		
		boolean actingDone = optionalSequenceAction.act(0f);
		
		assertFalse(spies.get(0).get());		
		assertTrue(spies.get(1).get());		
		assertFalse(spies.get(2).get());
		assertFalse(actingDone);
	}	

	@Test
	void act_withLastActionsEnabled_runsLastActionAndActingDone() throws Exception {
		OptionalAction optionalAction0 = makeOptionalActionWithSpy(spies.get(0), false);
		OptionalAction optionalAction1 = makeOptionalActionWithSpy(spies.get(1), false);
		OptionalAction optionalAction2 = makeOptionalActionWithSpy(spies.get(2), true);
		OptionalSequenceAction optionalSequenceAction = new OptionalSequenceAction(optionalAction0, optionalAction1, optionalAction2);
		
		boolean actingDone = optionalSequenceAction.act(0f);
		
		assertFalse(spies.get(0).get());		
		assertFalse(spies.get(1).get());		
		assertTrue(spies.get(2).get());
		assertTrue(actingDone);
	}	

	@Test
	void act_withAllActionsDisabled_runsNoActionsAndActingDone() throws Exception {
		OptionalAction optionalAction0 = makeOptionalActionWithSpy(spies.get(0), false);
		OptionalAction optionalAction1 = makeOptionalActionWithSpy(spies.get(1), false);
		OptionalAction optionalAction2 = makeOptionalActionWithSpy(spies.get(2), false);
		OptionalSequenceAction optionalSequenceAction = new OptionalSequenceAction(optionalAction0, optionalAction1, optionalAction2);
		
		boolean actingDone = optionalSequenceAction.act(0f);
		
		assertFalse(spies.get(0).get());		
		assertFalse(spies.get(1).get());		
		assertFalse(spies.get(2).get());
		assertTrue(actingDone);
	}	

	
	private static OptionalAction makeOptionalActionWithSpy(AtomicBoolean spy, boolean shouldExecuteAction) {
		RunnableAction runnableAction = new RunnableAction();
		runnableAction.setRunnable(() -> spy.set(true));
		
		OptionalAction optionalAction = new OptionalAction(() -> shouldExecuteAction);
		optionalAction.setAction(runnableAction);
		
		return optionalAction;
	}

}
