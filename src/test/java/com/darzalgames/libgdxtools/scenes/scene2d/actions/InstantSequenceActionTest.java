package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.scenes.scene2d.Actor;

class InstantSequenceActionTest {

	@Test
	void act_onASequenceActionOfTwoInstantActions_finishesinASingleAct() {
		Actor actor = new Actor(); // Apparently sequence actions need an actor yet others don't?
		AtomicBoolean spy = new AtomicBoolean(false);
		InstantSequenceAction sequenceAction = new InstantSequenceAction(new NullAction(), new RunnableActionBest(() -> spy.set(true)));

		actor.addAction(sequenceAction);
		boolean actionDoneAfter1 = sequenceAction.act(0);

		assertTrue(spy.get());
		assertTrue(actionDoneAfter1);
	}

	@Test
	void act_onASequenceActionOfSixInstantActions_finishesinASingleAct() {
		Actor actor = new Actor(); // Apparently sequence actions need an actor yet others don't?
		AtomicBoolean spy1 = new AtomicBoolean(false);
		AtomicBoolean spy2 = new AtomicBoolean(false);
		AtomicBoolean spy3 = new AtomicBoolean(false);
		AtomicBoolean spy4 = new AtomicBoolean(false);
		AtomicBoolean spy5 = new AtomicBoolean(false);
		AtomicBoolean spy6 = new AtomicBoolean(false);
		// Showing off my constructor that can take more than five actions (i.e. The whole reason I made InstantSequenceAction)
		InstantSequenceAction sequenceAction = new InstantSequenceAction(
				new RunnableActionBest(() -> spy1.set(true)),
				new RunnableActionBest(() -> spy2.set(true)),
				new RunnableActionBest(() -> spy3.set(true)),
				new RunnableActionBest(() -> spy4.set(true)),
				new RunnableActionBest(() -> spy5.set(true)),
				new RunnableActionBest(() -> spy6.set(true))
				);

		actor.addAction(sequenceAction);
		boolean actionDoneAfter1 = sequenceAction.act(0);

		assertTrue(spy1.get());
		assertTrue(spy2.get());
		assertTrue(spy3.get());
		assertTrue(spy4.get());
		assertTrue(spy5.get());
		assertTrue(spy6.get());
		assertTrue(actionDoneAfter1);
	}

	@Test
	void act_onASequenceActionOfTwo_isCorrect() {
		Actor actor = new Actor(); // Apparently sequence actions need an actor yet others don't?
		InstantSequenceAction sequenceAction = new InstantSequenceAction(InstantRepeatActionTest.getTwoFrameAction(), InstantRepeatActionTest.getTwoFrameAction());

		actor.addAction(sequenceAction);
		boolean actionDoneAfter1 = sequenceAction.act(0);  // The first action gets its first hit
		boolean actionDoneAfter2 = sequenceAction.act(0);  // The first action gets its second hit and finishes, and the second action gets its first hit
		boolean actionDoneAfter3 = sequenceAction.act(0);  // The second action gets its second hit and finishes

		assertFalse(actionDoneAfter1);
		assertFalse(actionDoneAfter2);
		assertTrue(actionDoneAfter3);
	}

	@Test
	void constructor_doesNotCallBegin() {
		AtomicBoolean spy = new AtomicBoolean(false);
		new InstantSequenceAction() {
			@Override
			protected void begin() {
				spy.set(true);
			}
		};

		assertFalse(spy.get());
	}

	@Test
	void act_onASequenceAction_callsBegin() {
		AtomicBoolean spy = new AtomicBoolean(false);
		InstantSequenceAction sequenceAction = new InstantSequenceAction() {
			@Override
			protected void begin() {
				spy.set(true);
			}
		};

		sequenceAction.act(0);

		assertTrue(spy.get());
	}

	@Test
	void act_twiceOnASequenceAction_callsBeginExactlyOnce() {
		AtomicInteger spy = new AtomicInteger(0);
		InstantSequenceAction sequenceAction = new InstantSequenceAction() {
			@Override
			protected void begin() {
				spy.set(spy.get() + 1);
			}
		};
		Actor actor = new Actor(); // Apparently sequence actions need an actor yet others don't?

		actor.addAction(sequenceAction);
		sequenceAction.act(0.1f);
		sequenceAction.act(0.1f);

		assertEquals(1, spy.get());
	}

	@Test
	void restartAndAct_onASequenceAction_callsBeginExactlyTwice() {
		AtomicInteger spy = new AtomicInteger(0);
		InstantSequenceAction sequenceAction = new InstantSequenceAction() {
			@Override
			protected void begin() {
				spy.set(spy.get() + 1);
			}
		};
		Actor actor = new Actor(); // Apparently sequence actions need an actor yet others don't?

		actor.addAction(sequenceAction);
		sequenceAction.act(0);
		sequenceAction.restart();
		sequenceAction.act(0);

		assertEquals(2, spy.get());
	}
}
