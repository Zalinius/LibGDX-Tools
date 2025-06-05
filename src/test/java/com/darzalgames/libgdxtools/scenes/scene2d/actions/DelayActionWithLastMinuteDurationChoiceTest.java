package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DelayActionWithLastMinuteDurationChoiceTest {

	@Test
	void act_onADelayAction_hasTheSuppliedDuration() {
		DelayActionWithLastMinuteDurationChoice delayAction = new DelayActionWithLastMinuteDurationChoice(() -> 4f);

		delayAction.act(0);

		assertEquals(4, delayAction.getDuration());
	}


	private float delay = 5f;
	@Test
	void act_onADelayActionWithChangedSupplier_setsTheDurationToTheChangedValue() {
		DelayActionWithLastMinuteDurationChoice delayAction = new DelayActionWithLastMinuteDurationChoice(() -> delay);

		delay = 3f;
		delayAction.act(0);

		assertEquals(3, delayAction.getDuration());
	}

	@Test
	void restartAndAct_onADelayActionWithChangedSupplier_setsTheDurationToTheChangedValue() {
		DelayActionWithLastMinuteDurationChoice delayAction = new DelayActionWithLastMinuteDurationChoice(() -> delay);

		delayAction.act(0);
		delayAction.restart();
		delay = 6f;
		delayAction.act(0);

		assertEquals(6, delayAction.getDuration());
	}

}
