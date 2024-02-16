package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.darzalgames.darzalcommon.functional.Runnables;

class RunnableActionBestTest {

	@Test
	void act_onANullAction_finishesinASingleAct() throws Exception {
		Runnable testRunnable = Runnables.nullRunnable();

		RunnableActionBest runnableActionBest = new RunnableActionBest(testRunnable);

		assertEquals(testRunnable, runnableActionBest.getRunnable());
	}
}
