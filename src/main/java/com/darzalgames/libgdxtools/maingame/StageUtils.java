package com.darzalgames.libgdxtools.maingame;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class StageUtils {

	private StageUtils() {}

	/**
	 * Recursively counts all actors on a stage
	 */
	public static int countActorsInStage(Stage stage) {
		return countActorsInActor(stage.getRoot());
	}

	/**
	 * Recursively counts all actors in an actor, including itself
	 * @return the total number of actors in the actor, including all its descendants and itself
	 */
	public static int countActorsInActor(Actor actor) {
		if (actor instanceof Group group) {
			int children = group.getChildren().size;
			int totalDescendants = 1; // include the group as an actor
			for (int i = 0; i < children; i++) {
				totalDescendants += countActorsInActor(group.getChild(i));
			}
			return totalDescendants;
		} else {
			return 1;
		}
	}

}
