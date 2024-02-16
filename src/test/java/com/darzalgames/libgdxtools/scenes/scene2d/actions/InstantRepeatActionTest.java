package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.scenes.scene2d.Action;

class InstantRepeatActionTest {

	@Test
	void act_onARepeatActionThatRepeatsTwice_finishesinASingleAct() throws Exception {
		InstantRepeatAction repeatAction = new InstantRepeatAction();
		repeatAction.setTotalCount(2);
		repeatAction.setAction(new NullAction());

		boolean actionDoneAfter1 = repeatAction.act(0);
		
		assertEquals(0, repeatAction.getRemainingCount());
		assertTrue(actionDoneAfter1);
	}
	@Test
	void act_onARepeatActionThatRepeatsTenTimes_finishesinASingleAct() throws Exception {
		InstantRepeatAction repeatAction = new InstantRepeatAction();
		repeatAction.setTotalCount(10);
		repeatAction.setAction(new NullAction());

		boolean actionDoneAfter1 = repeatAction.act(0);
		
		assertEquals(0, repeatAction.getRemainingCount());
		assertTrue(actionDoneAfter1);
	}

	@Test
	void getTotalCount_onARepeatActionThatRepeatsTwice_isTwo() throws Exception {
		InstantRepeatAction repeatAction = new InstantRepeatAction();
		repeatAction.setTotalCount(2);
		repeatAction.setAction(new NullAction());

		int totalCount = repeatAction.getTotalCount();
		
		assertEquals(2, totalCount);
	}
	
	@Test
	void getTotalCount_onARepeatActionThatHasFinishedRepeatsTwice_isStillTwo() throws Exception {
		InstantRepeatAction repeatAction = new InstantRepeatAction();
		repeatAction.setTotalCount(2);
		repeatAction.setAction(new NullAction());

		repeatAction.act(0);
		repeatAction.act(0);
		int totalCount = repeatAction.getTotalCount();
		
		assertEquals(2, totalCount);
	}
	
	@Test
	void getExecutedCount_onARepeatActionThatRepeatsTwice_isCorrect() throws Exception {
		InstantRepeatAction repeatAction = new InstantRepeatAction();
		repeatAction.setTotalCount(2);
		repeatAction.setAction(new NullAction());

		int initialCount = repeatAction.getExecutedCount();
		repeatAction.act(0);
		int firstActCount = repeatAction.getExecutedCount();
		
		assertEquals(0, initialCount);
		assertEquals(2, firstActCount);
	}
	
	@Test
	void getRemainingCount_onARepeatActionThatRepeatsTwice_isCorrect() throws Exception {
		InstantRepeatAction repeatAction = new InstantRepeatAction();
		repeatAction.setTotalCount(2);
		repeatAction.setAction(getTwoFrameAction());

		int initialCount = repeatAction.getRemainingCount();
		repeatAction.act(0); // The first action gets its first hit
		int firstActCount = repeatAction.getRemainingCount();
		repeatAction.act(0); // The first action gets its second hit and finishes, and the second action gets its first hit
		int secondActCount = repeatAction.getRemainingCount();
		repeatAction.act(0); // The second action gets its second hit and finishes
		int thirdActCount = repeatAction.getRemainingCount();
		
		assertEquals(2, initialCount);
		assertEquals(2, firstActCount);
		assertEquals(1, secondActCount);
		assertEquals(0, thirdActCount);
	}
	
	public static Action getTwoFrameAction() {
		return new Action() {
			int hits = 0;

			@Override
			public boolean act(float delta) {
				hits++;
				return hits == 2;
			}
			@Override
			public void restart() {
				super.restart();
				hits = 0;
			}
		};
	}
	
}
