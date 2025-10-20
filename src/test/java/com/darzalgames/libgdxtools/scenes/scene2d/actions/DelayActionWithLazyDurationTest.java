package com.darzalgames.libgdxtools.scenes.scene2d.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

class DelayActionWithLazyDurationTest {

	@Test
	void act_onADelayAction_hasTheSuppliedDuration() {
		DelayActionWithLazyDuration delayAction = new DelayActionWithLazyDuration(() -> 4f);

		delayAction.act(0);

		assertEquals(4, delayAction.getDuration());
	}

	@Test
	void act_onADelayActionWithChangedSupplier_setsTheDurationToTheChangedValue() {
		AtomicReference<Float> atomicFloat = new AtomicReference<>(5f);
		DelayActionWithLazyDuration delayAction = new DelayActionWithLazyDuration(atomicFloat::get);

		atomicFloat.set(3f);
		delayAction.act(0);

		assertEquals(3, delayAction.getDuration());
	}

	@Test
	void restartAndAct_onADelayActionWithChangedSupplier_setsTheDurationToTheChangedValue() {
		AtomicReference<Float> atomicFloat = new AtomicReference<>(5f);
		DelayActionWithLazyDuration delayAction = new DelayActionWithLazyDuration(atomicFloat::get);

		delayAction.act(0);
		delayAction.restart();
		atomicFloat.set(6f);
		delayAction.act(0);

		assertEquals(6, delayAction.getDuration());
	}

}
