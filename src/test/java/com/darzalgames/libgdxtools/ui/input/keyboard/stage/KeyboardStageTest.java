package com.darzalgames.libgdxtools.ui.input.keyboard.stage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

class KeyboardStageTest {

	@Test
	void isInTouchableBranch_TouchableEnabledInParentGroup_returnsTrue() throws Exception {
		Group group = new Group();
		Actor actor = new Actor();
		group.addActor(actor);

		group.setTouchable(Touchable.enabled);
		boolean isInTouchableBranch = KeyboardStage.isInTouchableBranch(actor);

		assertTrue(isInTouchableBranch);
	}
	
	@Test
	void isInTouchableBranch_TouchableChildrenOnlyInParentGroup_returnsTrue() throws Exception {
		Group group = new Group();
		Actor actor = new Actor();
		group.addActor(actor);

		group.setTouchable(Touchable.childrenOnly);
		boolean isInTouchableBranch = KeyboardStage.isInTouchableBranch(actor);

		assertTrue(isInTouchableBranch);
	}
	
	@Test
	void isInTouchableBranch_TouchableDisabledInParentGroup_returnsFalse() throws Exception {
		Group group = new Group();
		Actor actor = new Actor();
		group.addActor(actor);

		group.setTouchable(Touchable.disabled);
		boolean isInTouchableBranch = KeyboardStage.isInTouchableBranch(actor);

		assertFalse(isInTouchableBranch);
	}
	
	@Test
	void isInTouchableBranch_TouchableEnabled_returnsTrue() throws Exception {
		Actor actor = new Actor();

		actor.setTouchable(Touchable.enabled);
		boolean isInTouchableBranch = KeyboardStage.isInTouchableBranch(actor);

		assertTrue(isInTouchableBranch);
	}
	
	@Test
	void isInTouchableBranch_TouchableDisabled_returnsFalse() throws Exception {
		Actor actor = new Actor();

		actor.setTouchable(Touchable.disabled);
		boolean isInTouchableBranch = KeyboardStage.isInTouchableBranch(actor);

		assertFalse(isInTouchableBranch);
	}
	
	@Test
	void isInTouchableBranch_TouchableChildrenOnly_returnsTrueButTheActorItselfIsNotTouchable() throws Exception {
		Actor actor = new Actor();

		actor.setTouchable(Touchable.childrenOnly);
		boolean isInTouchableBranch = KeyboardStage.isInTouchableBranch(actor);

		assertTrue(isInTouchableBranch);
		assertFalse(actor.isTouchable());
	}
}
