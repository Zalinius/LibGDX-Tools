package com.darzalgames.libgdxtools.maingame;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

class StageBestTest {

	@Test
	void setShouldDrawAndAct_setsIt() {
		// TODO check both
		Gdx.graphics = new MockGraphics();
		StageBest stageBest = new StageBest(new TestViewport(), new TestBatch());

		stageBest.setShouldDrawAndAct(false);

		assertFalse(stageBest.shouldDrawAndAct());
	}

	@Test
	void isInTouchableBranch_TouchableEnabledInParentGroup_returnsTrue() {
		Group group = new Group();
		Actor actor = new Actor();
		group.addActor(actor);

		group.setTouchable(Touchable.enabled);
		boolean isInTouchableBranch = StageBest.isInTouchableBranch(actor);

		assertTrue(isInTouchableBranch);
	}

	@Test
	void isInTouchableBranch_TouchableChildrenOnlyInParentGroup_returnsTrue() {
		Group group = new Group();
		Actor actor = new Actor();
		group.addActor(actor);

		group.setTouchable(Touchable.childrenOnly);
		boolean isInTouchableBranch = StageBest.isInTouchableBranch(actor);

		assertTrue(isInTouchableBranch);
	}

	@Test
	void isInTouchableBranch_TouchableDisabledInParentGroup_returnsFalse() {
		Group group = new Group();
		Actor actor = new Actor();
		group.addActor(actor);

		group.setTouchable(Touchable.disabled);
		boolean isInTouchableBranch = StageBest.isInTouchableBranch(actor);

		assertFalse(isInTouchableBranch);
	}

	@Test
	void isInTouchableBranch_TouchableEnabled_returnsTrue() {
		Actor actor = new Actor();

		actor.setTouchable(Touchable.enabled);
		boolean isInTouchableBranch = StageBest.isInTouchableBranch(actor);

		assertTrue(isInTouchableBranch);
	}

	@Test
	void isInTouchableBranch_TouchableDisabled_returnsFalse() {
		Actor actor = new Actor();

		actor.setTouchable(Touchable.disabled);
		boolean isInTouchableBranch = StageBest.isInTouchableBranch(actor);

		assertFalse(isInTouchableBranch);
	}

	@Test
	void isInTouchableBranch_TouchableChildrenOnly_returnsTrueButTheActorItselfIsNotTouchable() {
		Actor actor = new Actor();

		actor.setTouchable(Touchable.childrenOnly);
		boolean isInTouchableBranch = StageBest.isInTouchableBranch(actor);

		assertTrue(isInTouchableBranch);
		assertFalse(actor.isTouchable());
	}
}
