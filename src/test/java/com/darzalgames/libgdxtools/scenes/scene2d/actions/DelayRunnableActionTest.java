package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

class DelayRunnableActionTest {

	@Test
	void act_fullDuration_runsRunnable() {
		AtomicBoolean spy = new AtomicBoolean(false);
		DelayRunnableAction action = new DelayRunnableAction(1, () -> spy.set(true));

		action.act(1);

		assertTrue(spy.get());
	}

	@Test
	void act_halfDuration_doesNotYetRunRunnable() {
		AtomicBoolean spy = new AtomicBoolean(false);
		DelayRunnableAction action = new DelayRunnableAction(1, () -> spy.set(true));

		action.act(0.5f);

		assertFalse(spy.get());
	}

}
