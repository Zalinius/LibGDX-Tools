package com.darzalgames.libgdxtools.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class UserInterfaceSizer {
	
	// TODO UI scaling multiplier here + the font (?)
	
	private static Stage stage;
	
	public static void setStage(Stage stage) {
		UserInterfaceSizer.stage = stage;
	}

	/**
	 * @param actor The actor to resize
	 * @param width The percentage of the world/stage WIDTH that this actor should occupy [0-1]
	 * @param height The percentage of the world/stage HEIGHT that this actor should occupy [0-1]
	 */
	public static void sizeToPercentage(Actor actor, float width, float height) {
		actor.setSize(stage.getViewport().getWorldWidth()*width, stage.getViewport().getWorldHeight()*height);
	}

	/**
	 * @param actor The actor to resize
	 * @param percent The percentage of the world/stage WIDTH and HEIGHT that this actor should occupy [0-1]
	 */
	public static void sizeToPercentage(Actor actor, float percent) {
		sizeToPercentage(actor, percent, percent);
	}
	
	/**
	 * @return The percentage of the stage's WIDTH [0-1], useful for things like padding
	 */
	public static float getWidthPercentage(float percentage) {
		return stage.getViewport().getWorldWidth()*percentage;
	}
	
	/**
	 * @return The percentage of the stage's HEIGHT [0-1], useful for things like padding
	 */
	public static float getHeightPercentage(float percentage) {
		return stage.getViewport().getWorldHeight()*percentage;
	}
	
	/**
	 * @return The current HEIGHT of the game screen. Note: this can change at any moment!
	 */
	public static float getCurrentHeight() {
		return getHeightPercentage(1);
	}
	
	/**
	 * @return The current WITH of the game screen. Note: this can change at any moment!
	 */
	public static float getCurrentWidth() {
		return getWidthPercentage(1);
	}
	
	/**
	 * @param actor
	 */
	public static void makeActorCentered(final Actor actor) {
		actor.setPosition(getWidthPercentage(0.5f) - actor.getWidth() / 2f,
				getHeightPercentage(0.5f) - actor.getHeight() / 2f);
	}
}
