package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.scenes.scene2d.Actor;

class ParallelActionBestTest {

	@Test
	void act_onAParallelActionOfSix_finishesinASingleAct() throws Exception {
		Actor actor = new Actor(); // Apparently parallel actions need an actor yet others don't?
		AtomicBoolean spy1 = new AtomicBoolean(false);
		AtomicBoolean spy2 = new AtomicBoolean(false);
		AtomicBoolean spy3 = new AtomicBoolean(false);
		AtomicBoolean spy4 = new AtomicBoolean(false);
		AtomicBoolean spy5 = new AtomicBoolean(false);
		AtomicBoolean spy6 = new AtomicBoolean(false);
		// Showing off my constructor that can take more than five actions (i.e. The whole reason I made ParallelActionBest)
		ParallelActionBest parallelAction = new ParallelActionBest(
				new RunnableActionBest(() -> spy1.set(true)),
				new RunnableActionBest(() -> spy2.set(true)),
				new RunnableActionBest(() -> spy3.set(true)),
				new RunnableActionBest(() -> spy4.set(true)),
				new RunnableActionBest(() -> spy5.set(true)),
				new RunnableActionBest(() -> spy6.set(true))
				);

		actor.addAction(parallelAction);
		boolean actionDoneAfter1 = parallelAction.act(0);

		assertTrue(spy1.get());
		assertTrue(spy2.get());
		assertTrue(spy3.get());
		assertTrue(spy4.get());
		assertTrue(spy5.get());
		assertTrue(spy6.get());
		assertTrue(actionDoneAfter1);
	}
}
