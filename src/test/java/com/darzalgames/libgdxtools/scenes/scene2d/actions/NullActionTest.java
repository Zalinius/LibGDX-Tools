package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NullActionTest {

	@Test
	void act_onANullAction_finishesinASingleAct() throws Exception {
		NullAction action = new NullAction();

		boolean actionDoneAfter1 = action.act(0);

		assertTrue(actionDoneAfter1);
	}
}
