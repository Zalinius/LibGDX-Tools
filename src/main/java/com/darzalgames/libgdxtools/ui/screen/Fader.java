package com.darzalgames.libgdxtools.ui.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.maingame.GetOnStage;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantSequenceAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;

public class Fader {

	private static Image darkScreen;
	private static String topmostStageName;

	private static float fadeTime = 0.6f;

	private Fader() {}

	public static void initialize(String topmostStage) {
		topmostStageName = topmostStage;
		darkScreen = new Image(ColorTools.getColoredTexture(Color.BLACK, 2));
		darkScreen.setTouchable(Touchable.disabled);
		hideDarkScreen();
	}

	public static void doLongFadeIn() {
		doLongFadeIn(Runnables.nullRunnable());
	}

	public static void resizeUI() {
		UserInterfaceSizer.scaleToFillScreenAndMakeCentered(darkScreen);
	}

	public static void doLongFadeIn(Runnable doAfter) {
		GetOnStage.addActorToStage(darkScreen, topmostStageName);
		darkScreen.clearActions();
		darkScreen.setColor(Color.WHITE);
		darkScreen.addAction(new InstantSequenceAction(
				Actions.fadeOut(fadeTime),
				new RunnableActionBest(doAfter),
				Actions.removeActor()
				));
	}


	public static void doLongFadeOut() {
		doLongFadeOut(Runnables.nullRunnable());
	}

	public static void doLongFadeOut(Runnable doAfter) {
		GetOnStage.addActorToStage(darkScreen, topmostStageName);
		hideDarkScreen();
		darkScreen.clearActions();
		darkScreen.addAction(new InstantSequenceAction(
				Actions.fadeIn(fadeTime),
				new RunnableActionBest(doAfter)
				));
	}

	public static void hideDarkScreen() {
		darkScreen.setColor(Color.CLEAR);
	}

	public static void setFadeTime(float fadeTime) {
		Fader.fadeTime = fadeTime;
	}
}