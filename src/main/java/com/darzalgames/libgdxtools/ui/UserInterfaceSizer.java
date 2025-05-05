package com.darzalgames.libgdxtools.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.darzalgames.libgdxtools.maingame.GameInfo;

public class UserInterfaceSizer {

	private UserInterfaceSizer() {
	}

	private static Stage stage;
	private static float scaling = 1;
	private static Runnable updateFont;

	/**
	 * @param actor The actor to size, typically a nine/ten patch
	 * @param proportion The percentage of the world/stage WIDTH and HEIGHT that this actor should occupy [0-1]
	 */
	public static void sizeToPercentage(Actor actor, float proportion) {
		sizeToPercentage(actor, proportion, proportion);
	}

	/**
	 * @param actor The actor to size, typically a nine/ten patch
	 * @param width The percentage of the world/stage WIDTH that this actor should occupy [0-1]
	 * @param height The percentage of the world/stage HEIGHT that this actor should occupy [0-1]
	 */
	public static void sizeToPercentage(Actor actor, float width, float height) {
		actor.setSize(getWidthPercentage(width), getHeightPercentage(height));
	}

	/**
	 * @param actor The actor to resize, maintaining aspect ratio, typically an image
	 * @param percent The percentage of the world/stage WIDTH or HEIGHT (minimum) that this actor should occupy [0-1]
	 */
	public static void scaleToMinimumPercentage(Actor actor, float percent) {
		float multiplierWidth = getWidthPercentage(percent);
		float multiplierHeight = getHeightPercentage(percent);
		if (multiplierWidth <= multiplierHeight) {
			actor.setSize(multiplierWidth, multiplierWidth * (actor.getHeight()/actor.getWidth()));
		} else {
			actor.setSize(multiplierHeight * (actor.getWidth()/actor.getHeight()), multiplierHeight);
		}
	}

	/**
	 * Size an actor relative to the screen size without taking UI scaling into account. Useful for decorative actors like the splash screen.
	 * @param actor The actor to resize, maintaining aspect ratio, typically an image
	 * @param percent The percentage of the world/stage WIDTH or HEIGHT (minimum) that this actor should occupy [0-1]
	 */
	public static void sizeToMinimumPercentageWithoutUIScaling(Actor actor, float percent) {
		float multiplierWidth = getCurrentWidth() * percent;
		float multiplierHeight = getCurrentHeight() * percent;
		if (multiplierWidth <= multiplierHeight) {
			actor.setSize(multiplierWidth, multiplierWidth * (actor.getHeight()/actor.getWidth()));
		} else {
			actor.setSize(multiplierHeight * (actor.getWidth()/actor.getHeight()), multiplierHeight);
		}
	}

	/**
	 * @param actor The actor which will be resized to fill the screen and centered
	 */
	public static void scaleToFillScreenAndMakeCentered(Actor actor) {
		actor.setSize(UserInterfaceSizer.getCurrentWidth(), UserInterfaceSizer.getCurrentHeight());
		UserInterfaceSizer.makeActorCentered(actor);
	}

	/**
	 * @param drawable The drawable to set minimum width and height, maintaining aspect ratio, typically an image
	 * @param percent The percentage of the world/stage WIDTH or HEIGHT (minimum) that this actor should occupy [0-1]
	 */
	public static void scaleToMinimumPercentage(Drawable drawable, float percent, float originalWidth, float originalHeight) {
		float multiplierWidth = getWidthPercentage(percent);
		float multiplierHeight = getHeightPercentage(percent);
		if (multiplierWidth <= multiplierHeight) {
			drawable.setMinWidth(multiplierWidth);
			drawable.setMinHeight(multiplierWidth * (originalHeight/originalWidth));
		} else {
			drawable.setMinWidth(multiplierHeight * (originalWidth/originalHeight));
			drawable.setMinHeight(multiplierHeight);
		}
	}


	/**
	 * @return The percentage of the stage's WIDTH [0-1], useful for things like padding
	 */
	public static float getWidthPercentage(float percentage) {
		float maximum = getCurrentWidth();
		return Math.min(maximum, getCurrentWidth()*percentage*scaling);
	}

	/**
	 * @return The percentage of the stage's HEIGHT [0-1], useful for things like padding
	 */
	public static float getHeightPercentage(float percentage) {
		float maximum = getCurrentHeight();
		return Math.min(maximum, getCurrentHeight()*percentage*scaling);
	}
	/**
	 * @return The percentage of the stage's HEIGHT or WIDTH [0-1], whatever is smaller
	 */
	public static float getMinimumPercentage(float percentage) {
		return Math.min(getHeightPercentage(percentage), getWidthPercentage(percentage));
	}

	/**
	 * @return The current UNSCALED HEIGHT of the game screen. Note: this can change at any moment!
	 */
	public static float getCurrentHeight() {
		return stage.getViewport().getWorldHeight();
	}

	/**
	 * @return The current UNSCALED WIDTH of the game screen. Note: this can change at any moment!
	 */
	public static float getCurrentWidth() {
		return stage.getViewport().getWorldWidth();
	}

	/**
	 * @param actor The given actor will be centered on screen, call this every frame to stay centered
	 */
	public static void makeActorCentered(final Actor actor) {
		actor.setPosition(getCurrentWidth()/2f - (actor.getWidth()*actor.getScaleX()) / 2f,
				getCurrentHeight()/2f - (actor.getHeight()*actor.getScaleY()) / 2f);
	}



	public static void setStage(Stage stage) {
		UserInterfaceSizer.stage = stage;
		setScaling(GameInfo.getPreferenceManager().graphics().getUserInterfaceScaling());
	}

	public static void setScaling(float scaling) {
		UserInterfaceSizer.scaling = scaling;
		if (updateFont != null) {
			updateFont.run();
		}
		GameInfo.getPreferenceManager().graphics().setUserInterfaceScaling(scaling);
	}

	public static void setUpdateFont(Runnable updateFont) {
		UserInterfaceSizer.updateFont = updateFont;
	}

	public static float getScaling() {
		return scaling;
	}
}
