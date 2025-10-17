package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.scenes.scene2d.actions.DelegateAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;

class OptionalActionTest {

	@Test
	void act_whenOptionalActionHasSupplierReturningFalse_hasNoEffect()  {
		InstantRepeatAction repeatAction = new InstantRepeatAction();
		repeatAction.setTotalCount(10);
		AtomicBoolean spy = new AtomicBoolean(false);
		RunnableAction runnableAction = new RunnableAction();
		runnableAction.setRunnable(() -> spy.set(true));
		DelegateAction optionalAction = new OptionalAction(() -> false);
		optionalAction.setAction(runnableAction);

		boolean actionDone = optionalAction.act(0);

		assertFalse(spy.get());
		assertTrue(actionDone);
	}

	@Test
	void act_whenOptionalActionHasSupplierReturningTrue_activatesAction()  {
		AtomicBoolean spy = new AtomicBoolean(false);
		RunnableAction runnableAction = new RunnableAction();
		runnableAction.setRunnable(() -> spy.set(true));
		DelegateAction optionalAction = new OptionalAction(() -> true);
		optionalAction.setAction(runnableAction);

		boolean actionDone = optionalAction.act(0);

		assertTrue(spy.get());
		assertTrue(actionDone);
	}

}
