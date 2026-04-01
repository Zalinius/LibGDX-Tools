package com.darzalgames.libgdxtools.maingame;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

class StageUtilsTest {

	@Test
	void countActorsInActor_withEmptyGroup_returns1() {
		Actor actor = new Group();

		assertEquals(1, StageUtils.countActorsInActor(actor));
	}

	@Test
	void countActorsInActor_withFilledGroup_returnsNumberOfActorsIncludingGroup() {
		Group group = new Group();
		group.addActor(new Actor());
		group.addActor(new Actor());
		group.addActor(new Actor());
		group.addActor(new Actor());

		assertEquals(5, StageUtils.countActorsInActor(group));
	}

	@Test
	void countActorsInActor_withNestedGroups_returnsNumberOfActors() {
		Group rootGroup = new Group();
		Group subGroup1 = new Group();
		Group subGroup2 = new Group();
		Group subSubGroup = new Group();
		Actor actor1 = new Actor();
		Actor actor2 = new Actor();
		Actor actor3 = new Actor();
		Actor actor4 = new Actor();
		Actor actor5 = new Actor();

		rootGroup.addActor(subGroup1);
		rootGroup.addActor(subGroup2);
		rootGroup.addActor(actor1);
		subGroup1.addActor(actor2);
		subGroup2.addActor(actor3);
		subGroup2.addActor(subSubGroup);
		subSubGroup.addActor(actor4);
		subSubGroup.addActor(actor5);

		assertEquals(9, StageUtils.countActorsInActor(rootGroup));
	}

}
